package frontend;

import db.AccountService;
import exceptions.AccountServiceException;
import exceptions.ExceptionMessages;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    private Frontend frontend;
    private StringWriter stringWriter;

    private static final HttpServletRequest request = mock(HttpServletRequest.class);
    private static final HttpServletResponse response = mock(HttpServletResponse.class);
    private static final HttpSession session = mock(HttpSession.class);
    private static final AccountService accountService = mock(AccountService.class);

    private static final String TEST_USER = getRandomString(7);
    private static final String TEST_PASSWORD = getRandomString(7);


    private void authorized(boolean isAuthorized, String destination) {
        when(request.getPathInfo()).thenReturn(destination);
        when(session.getAttribute("userId")).thenReturn(isAuthorized ? 0L : null);
    }

    @Before
    public void setUp() throws Exception {
        frontend = new Frontend(accountService);
        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(request.getSession()).thenReturn(session);
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
        doThrow(new AccountServiceException(ExceptionMessages.FAILED_AUTH)).when(accountService).authenticate(request);
        frontend.doPost(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Auth Page"));
        Assert.assertTrue(stringWriter.toString().contains("error"));
    }

    @Test
    public void testPostAuthPageSuccess() throws Exception {
        when(request.getParameter("username")).thenReturn(TEST_USER);
        when(request.getParameter("password")).thenReturn(TEST_PASSWORD);
        when(request.getPathInfo()).thenReturn(Pages.AUTH_PAGE);
        doNothing().when(accountService).authenticate(request);
        frontend.doPost(request, response);
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
        doThrow(new AccountServiceException(ExceptionMessages.EMPTY_DATA)).when(accountService).register(request);
        frontend.doPost(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Registration Page"));
        Assert.assertTrue(stringWriter.toString().contains("error"));
    }

    @Test
    public void testPostRegPageSuccess() throws Exception {
        when(request.getParameter("login")).thenReturn(TEST_USER);
        when(request.getParameter("password")).thenReturn(TEST_USER);
        when(request.getPathInfo()).thenReturn(Pages.REG_PAGE);
        doNothing().when(accountService).register(request);
        frontend.doPost(request, response);
        Assert.assertFalse(stringWriter.toString().contains("error"));
        verify(response, atLeastOnce()).sendRedirect(Pages.TIMER_PAGE);
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
        verify(accountService, atLeastOnce()).logout(session);
    }

    @After
    public void tearDown() throws Exception {
    }

}

