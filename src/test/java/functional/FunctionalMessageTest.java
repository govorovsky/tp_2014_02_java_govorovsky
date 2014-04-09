package functional;

import db.AccountService;
import db.AccountServiceImpl;
import db.HsqlDatabase;
import frontend.Frontend;
import frontend.Pages;
import junit.framework.Assert;
import messageSystem.MessageSystem;
import messageSystem.MsgLogin;
import messageSystem.MsgRegister;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.StringGenerator.getRandomString;

/**
 * Created by Andrew Govorovsky on 03.04.14
 */
public class FunctionalMessageTest {
    private final MessageSystem messageSystem;
    private final Frontend frontend;
    private final AccountService accountService;
    private final StringWriter stringWriter = new StringWriter();

    private static final HttpServletRequest request = mock(HttpServletRequest.class);
    private static final HttpServletResponse response = mock(HttpServletResponse.class);
    private static final HttpSession session = mock(HttpSession.class);

    Thread fe, as;

    private static final String USER = getRandomString(7);
    private static final String PASS = getRandomString(7);
    private static final String SSID = getRandomString(7);

    public FunctionalMessageTest() throws Exception {

        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(session.getId()).thenReturn(SSID);

        messageSystem = new MessageSystem();
        frontend = new Frontend(messageSystem);
        accountService = new AccountServiceImpl(new HsqlDatabase(), messageSystem);
        fe = new Thread(frontend);
        as = new Thread(accountService);
        fe.start();
        as.start();
    }

    @Test
    public void testRegisterAndAuth() throws Exception {
        messageSystem.sendMessage(new MsgRegister(frontend.getAddress(), accountService.getAddress(), USER, PASS, SSID));
        Thread.sleep(3000);
        messageSystem.sendMessage(new MsgLogin(frontend.getAddress(), accountService.getAddress(), USER, PASS, SSID));
        Thread.sleep(3000);
        when(request.getPathInfo()).thenReturn(Pages.MAIN_PAGE);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains(USER));
        accountService.delete(USER);
    }
}
