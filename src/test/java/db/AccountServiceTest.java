package db;

import exceptions.AccountServiceException;
import exceptions.EmptyDataException;
import exceptions.ExceptionMessages;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static util.StringGenerator.getRandomString;

/**
 * Created by Andrew Govorovsky on 12.03.14
 */
public class AccountServiceTest {
    private AccountService ac;

    private static final String TEST_LOGIN = getRandomString(7);
    private static final String TEST_PASS = getRandomString(7);

    @Before
    public void setUp() throws Exception {
        ac = new AccountService();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testRegisterSuccess() throws Exception {
        boolean success = true;
        try {
            ac.register(TEST_LOGIN, TEST_PASS);
        } catch (AccountServiceException e) {
            success = false;
        }
        Assert.assertTrue(success);
        ac.delete(TEST_LOGIN);
    }

    @Test
    public void testRegisterDuplicationName() throws Exception {
        try {
            ac.register(TEST_LOGIN, TEST_PASS);
            ac.register(TEST_LOGIN, TEST_PASS);
        } catch (AccountServiceException e) {
            Assert.assertTrue(e.getMessage().equals(ExceptionMessages.USER_ALREADY_EXISTS));
        }
        ac.delete(TEST_LOGIN);
    }


    @Test
    public void testAuthenticateSuccess() throws Exception {
        boolean success = true;
        ac.register(TEST_LOGIN, TEST_PASS);
        try {
            ac.authenticate(TEST_LOGIN, TEST_PASS);
        } catch (AccountServiceException e) {
            success = false;
        }
        ac.delete(TEST_LOGIN);
        Assert.assertTrue(success);
    }

    @Test
    public void testAuthenticateFail() throws Exception {
        boolean success = true;
        ac.register(TEST_LOGIN, TEST_PASS);
        try {
            ac.authenticate(TEST_LOGIN, TEST_PASS.concat("1337"));
        } catch (AccountServiceException e) {
            Assert.assertTrue(e.getMessage().equals(ExceptionMessages.FAILED_AUTH));
            success = false;
        }
        ac.delete(TEST_LOGIN);
        Assert.assertFalse(success);
    }


    @Test
    public void testCheckLoginPassword() throws Exception {
        boolean success = true;
        try {
            ac.checkLoginPassword(getRandomString(4), getRandomString(4));
        } catch (EmptyDataException e) {
            success = false;
        }
        Assert.assertTrue(success);
    }

    @Test
    public void testCheckLoginPasswordFail() throws Exception {
        boolean success = true;
        try {
            ac.checkLoginPassword("", "");
        } catch (EmptyDataException e) {
            success = false;
            Assert.assertTrue(e.getMessage().equals(ExceptionMessages.EMPTY_DATA));
        }
        Assert.assertFalse(success);
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        boolean success = true;
        ac.register(TEST_LOGIN, TEST_PASS);
        try {
            ac.delete(TEST_LOGIN);
        } catch (AccountServiceException e) {
            success = false;
        }
        Assert.assertTrue(success);
    }

    @Test
    public void testDeleteFail() throws Exception {
        boolean success = true;
        ac.register(TEST_LOGIN, TEST_PASS);
        try {
            ac.delete(TEST_LOGIN.concat("1337"));
        } catch (AccountServiceException e) {
            success = false;
            Assert.assertTrue(e.getMessage().equals(ExceptionMessages.NO_SUCH_USER_FOUND));
        }
        Assert.assertFalse(success);
    }
}
