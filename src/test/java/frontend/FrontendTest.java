package frontend;

import db.AccountService;
import db.AccountServiceMessages;
import exceptions.ExceptionMessages;
import junit.framework.Assert;
import messageSystem.AddressService;
import messageSystem.MessageSystem;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static util.StringGenerator.getRandomString;

/**
 * Created by Andrew Govorovsky on 08.03.14
 */
public class FrontendTest {
    private Frontend frontend = new Frontend(messageSystem);
    private StringWriter stringWriter = new StringWriter();

    private static final MessageSystem messageSystem = mock(MessageSystem.class);
    private static final AddressService addressService = mock(AddressService.class);

    private static final HttpServletRequest request = mock(HttpServletRequest.class);
    private static final HttpServletResponse response = mock(HttpServletResponse.class);
    private static final HttpSession session = mock(HttpSession.class);
    private static final AccountService accountService = mock(AccountService.class);

    private static final String TEST_USER = getRandomString(7);
    private static final String SSID = getRandomString(7);
    private static final String TEST_PASSWORD = getRandomString(7);

    public FrontendTest() {
        try {
            when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        } catch (Exception e) {

        }
    }

    @BeforeClass
    public static void setUp() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(messageSystem.getAddressService()).thenReturn(addressService);
        when(session.getId()).thenReturn(SSID);
    }

    private void authorized(boolean isAuthorized, String destination) {
        when(request.getPathInfo()).thenReturn(destination);
        frontend.updateUserSession(new UserSession(TEST_USER, SSID, isAuthorized ? AccountServiceMessages.AUTHORIZED : "", isAuthorized ? 12L : null));
    }


    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void test404Page() throws Exception {
        when(request.getPathInfo()).thenReturn(getRandomString(10));
        frontend.doGet(request, response);
        verify(response, atLeastOnce()).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void testGetMainPageNoAuth() throws Exception {
        authorized(false, Pages.MAIN_PAGE);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Main Page"));
    }

    @Test
    public void testGetMainPageAuthorized() throws Exception {
        authorized(true, Pages.MAIN_PAGE);
        when(request.getPathInfo()).thenReturn(Pages.MAIN_PAGE);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("User Page"));
    }

    @Test
    public void testGetAuthPage() throws Exception {
        when(request.getPathInfo()).thenReturn(Pages.AUTH_PAGE);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Auth Page"));
        Assert.assertFalse(stringWriter.toString().contains("error"));
    }

    @Test
    public void testPostAuthPageError() throws Exception {
        when(request.getParameter("username")).thenReturn(getRandomString(13));
        when(request.getParameter("password")).thenReturn(getRandomString(13));
        when(request.getPathInfo()).thenReturn(Pages.AUTH_PAGE);
        frontend.doPost(request, response);
        verify(response, atLeastOnce()).sendRedirect(Pages.AUTH_PAGE);
        frontend.updateUserSession(new UserSession(TEST_USER, SSID, ExceptionMessages.FAILED_AUTH));
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Auth Page"));
        Assert.assertTrue(stringWriter.toString().contains(ExceptionMessages.FAILED_AUTH));
    }

    @Test
    public void testPostAuthPageSuccess() throws Exception {
        when(request.getParameter("username")).thenReturn(TEST_USER);
        when(request.getParameter("password")).thenReturn(TEST_PASSWORD);
        when(request.getPathInfo()).thenReturn(Pages.AUTH_PAGE);
        frontend.doPost(request, response);
        frontend.updateUserSession(new UserSession(TEST_USER, SSID, AccountServiceMessages.AUTHORIZED));
        frontend.doGet(request, response);
        verify(response, atLeastOnce()).sendRedirect(Pages.TIMER_PAGE);
        Assert.assertFalse(stringWriter.toString().contains("error"));
    }

    @Test
    public void testGetRegPage() throws Exception {
        when(request.getPathInfo()).thenReturn(Pages.REG_PAGE);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Registration Page"));
        Assert.assertFalse(stringWriter.toString().contains("error"));
    }

    @Test
    public void testPostRegPageError() throws Exception {
        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(request.getPathInfo()).thenReturn(Pages.REG_PAGE);
        when(accountService.register(TEST_USER, TEST_PASSWORD)).thenReturn(new Result<>(false, ExceptionMessages.USER_ALREADY_EXISTS));
        frontend.doPost(request, response);
        frontend.updateUserSession(new UserSession(TEST_USER, SSID, ExceptionMessages.USER_ALREADY_EXISTS));
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Registration Page"));
        Assert.assertTrue(stringWriter.toString().contains("error"));
    }

    @Test
    public void testPostRegPageSuccess() throws Exception {
        when(request.getParameter("login")).thenReturn(TEST_USER);
        when(request.getParameter("password")).thenReturn(TEST_USER);
        when(request.getPathInfo()).thenReturn(Pages.REG_PAGE);
        frontend.doPost(request, response);
        frontend.updateUserSession(new UserSession(TEST_USER, SSID, AccountServiceMessages.USER_ADDED));
        frontend.doGet(request, response);
        Assert.assertFalse(stringWriter.toString().contains("error"));
        Assert.assertTrue(stringWriter.toString().contains(AccountServiceMessages.USER_ADDED));
    }

    @Test
    public void testGetTimerPageNoAuthorized() throws Exception {
        authorized(false, Pages.TIMER_PAGE);
        when(request.getPathInfo()).thenReturn(Pages.TIMER_PAGE);
        frontend.doGet(request, response);
        verify(response, atLeastOnce()).sendRedirect(Pages.MAIN_PAGE);
    }

    @Test
    public void testGetTimerPageAuthorized() throws Exception {
        authorized(true, Pages.TIMER_PAGE);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Timer Page"));
    }

    @Test
    public void testDoLogout() throws Exception {
        authorized(true, Pages.QUIT_PAGE);
        frontend.doGet(request, response);
        verify(response, atLeastOnce()).sendRedirect(Pages.MAIN_PAGE);
    }
}

