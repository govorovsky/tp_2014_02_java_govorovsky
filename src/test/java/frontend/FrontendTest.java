package frontend;

import db.AccountService;
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

/**
 * Created by Andrew Govorovsky on 08.03.14
 */
public class FrontendTest {
    private Frontend frontend;
    private AccountService accountService;
    private StringWriter stringWriter;
    private static final HttpServletRequest request = mock(HttpServletRequest.class);
    private static final HttpServletResponse response = mock(HttpServletResponse.class);
    private static final HttpSession session = mock(HttpSession.class);

    private String getRandomString(int len) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append((char) (Math.random() * (122 - 97 + 1) + 97)); /* a - z */
        }
        return builder.toString();
    }

    @Before
    public void setUp() throws Exception {
        stringWriter = new StringWriter();
        frontend = new Frontend();
        accountService = new AccountService();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(request.getSession()).thenReturn(session);


    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetMainPage() throws Exception {
        when(request.getPathInfo()).thenReturn(Pages.MAIN_PAGE);
        frontend.doGet(request,response);
        Assert.assertTrue(stringWriter.toString().contains("Main Page"));
    }

    @Test
    public void testPostMainPage() throws Exception {
        when(request.getPathInfo()).thenReturn(Pages.MAIN_PAGE);
        frontend.doPost(request, response);
        verify(response,atLeastOnce()).sendRedirect(Pages.MAIN_PAGE);
    }

    @Test
    public void testGetAuthPage() throws Exception {
        when(request.getPathInfo()).thenReturn(Pages.AUTH_PAGE);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Auth Page"));
        Assert.assertFalse(stringWriter.toString().contains("#error"));
    }

    @Test
    public void testPostAuthPageError() throws Exception {
        when(request.getParameter("username")).thenReturn(getRandomString(13));
        when(request.getParameter("password")).thenReturn(getRandomString(13));
        when(request.getPathInfo()).thenReturn(Pages.AUTH_PAGE);
        frontend.doPost(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Auth Page"));
        Assert.assertTrue(stringWriter.toString().contains("error"));
    }

    @Test
    public void testGetRegPage() throws Exception {
        when(request.getPathInfo()).thenReturn(Pages.REG_PAGE);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Registration Page"));
        Assert.assertFalse(stringWriter.toString().contains("#error"));
    }




}

