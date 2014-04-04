package functional;

import db.AccountService;
import db.AccountServiceImpl;
import frontend.Frontend;
import junit.framework.Assert;
import messageSystem.MessageSystem;
import messageSystem.MsgRegister;
import org.junit.Test;

import static util.StringGenerator.getRandomString;

/**
 * Created by Andrew Govorovsky on 03.04.14
 */
public class FunctionalMessageTest {
    private final MessageSystem messageSystem;
    private final Frontend frontend;
    private final AccountService accountService;

    Thread fe, as;

    private static final String USER = getRandomString(7);
    private static final String PASS = getRandomString(7);

    public FunctionalMessageTest() throws Exception {
        messageSystem = new MessageSystem();
        frontend = new Frontend(messageSystem);
        accountService = new AccountServiceImpl(messageSystem);
        fe = new Thread(frontend);
        as = new Thread(accountService);
        fe.start();
        as.start();
    }

    @Test
    public void testSendMsgRegister() throws Exception {
        messageSystem.sendMessage(new MsgRegister(frontend.getAddress(), accountService.getAddress(), USER, PASS, getRandomString(7)));
        Thread.sleep(2000);
        Assert.assertTrue(accountService.authenticate(USER, PASS).getResult() > 0);
        accountService.delete(USER);
    }
}
