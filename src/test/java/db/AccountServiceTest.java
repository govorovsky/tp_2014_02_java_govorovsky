package db;

import exceptions.AccountServiceException;
import exceptions.EmptyDataException;
import exceptions.ExceptionMessages;
import junit.framework.Assert;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static util.StringGenerator.getRandomString;

/**
 * Created by Andrew Govorovsky on 12.03.14
 */
public class AccountServiceTest {
    private AccountService ac = new AccountServiceImpl();

    private static final String TEST_USER = getRandomString(9);
    private static final String TEST_PASSWORD = getRandomString(9);

    @Rule
    public final ExpectedException exp = ExpectedException.none();

    private void deleteTestUser() {
        try {
            ac.delete(TEST_USER);
        } catch (Exception e) {

        }
    }

    @BeforeClass
    public static void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        deleteTestUser();
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        boolean success = true;
        try {
            ac.register(TEST_USER, TEST_PASSWORD);
        } catch (AccountServiceException | EmptyDataException e) {
            success = false;
        }
        Assert.assertTrue(success);
    }


    @Test
    public void testRegisterDuplicationName() throws Exception {
        ac.register(TEST_USER, TEST_PASSWORD);
        exp.expect(AccountServiceException.class);
        exp.expectMessage(ExceptionMessages.USER_ALREADY_EXISTS);
        ac.register(TEST_USER, TEST_PASSWORD);
    }


    @Test
    public void testAuthenticateSuccess() throws Exception {
        boolean success = true;
        ac.register(TEST_USER, TEST_PASSWORD);
        try {
            ac.authenticate(TEST_USER, TEST_PASSWORD);
        } catch (AccountServiceException | EmptyDataException e) {
            success = false;
        }
        Assert.assertTrue(success);
    }

    @Test
    public void testAuthenticateFail() throws Exception {
        ac.register(TEST_USER, TEST_PASSWORD);
        exp.expect(AccountServiceException.class);
        exp.expectMessage(ExceptionMessages.FAILED_AUTH);
        ac.authenticate(TEST_USER, TEST_PASSWORD.concat("32"));
    }


    @Test
    public void testCheckLoginPasswordFail() throws Exception {
        exp.expect(EmptyDataException.class);
        exp.expectMessage(ExceptionMessages.EMPTY_DATA);
        ac.authenticate("", "");
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        boolean success = true;
        ac.register(TEST_USER, TEST_PASSWORD);
        try {
            ac.delete(TEST_USER);
        } catch (AccountServiceException e) {
            success = false;
        }
        Assert.assertTrue(success);
    }

    @Test
    public void testDeleteFail() throws Exception {
        boolean success = true;
        ac.register(TEST_USER, TEST_PASSWORD);
        try {
            ac.delete(TEST_USER.concat("1337"));
        } catch (AccountServiceException e) {
            success = false;
            Assert.assertTrue(e.getMessage().equals(ExceptionMessages.NO_SUCH_USER_FOUND));
        }
        Assert.assertFalse(success);
    }
}
