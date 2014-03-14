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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.StringGenerator.getRandomString;

/**
 * Created by Andrew Govorovsky on 12.03.14
 */
public class AccountServiceTest {
    private AccountServiceImpl ac = new AccountServiceImpl();

    private static final HttpServletRequest request = mock(HttpServletRequest.class);
    private static final HttpSession session = mock(HttpSession.class);

    private static final String TEST_USER = getRandomString(9);
    private static final String TEST_PASSWORD = getRandomString(9);


    @Rule
    public final ExpectedException exp = ExpectedException.none();


    private void authorizationData(boolean isEmpty, boolean isRight) {
        if (isEmpty) {
            when(request.getParameter("username")).thenReturn("");
            when(request.getParameter("password")).thenReturn("");
        } else {
            when(request.getParameter("username")).thenReturn(TEST_USER);
            when(request.getParameter("password")).thenReturn(isRight ? TEST_PASSWORD : TEST_PASSWORD.concat("32132"));
        }
    }


    private void deleteTestUser() {
        try {
            ac.delete(TEST_USER);
        } catch (Exception e) {

        }
    }

    @BeforeClass
    public static void setUp() throws Exception {
        when(request.getSession()).thenReturn(session);
    }

    @After
    public void tearDown() throws Exception {
        deleteTestUser();
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        authorizationData(false, true);
        boolean success = true;
        try {
            ac.register(request);
        } catch (AccountServiceException | EmptyDataException e) {
            success = false;
        }
        Assert.assertTrue(success);
    }


    @Test
    public void testRegisterDuplicationName() throws Exception {
        authorizationData(false, true);
        ac.register(request);
        exp.expect(AccountServiceException.class);
        exp.expectMessage(ExceptionMessages.USER_ALREADY_EXISTS);
        ac.register(request);
    }


    @Test
    public void testAuthenticateSuccess() throws Exception {
        authorizationData(false, true);
        boolean success = true;
        ac.register(request);
        try {
            ac.authenticate(request);
        } catch (AccountServiceException | EmptyDataException e) {
            success = false;
        }
        Assert.assertTrue(success);
    }

    @Test
    public void testAuthenticateFail() throws Exception {
        authorizationData(false, true);
        ac.register(request);
        authorizationData(false, false);
        exp.expect(AccountServiceException.class);
        exp.expectMessage(ExceptionMessages.FAILED_AUTH);
        ac.authenticate(request);
    }


    @Test
    public void testCheckLoginPasswordFail() throws Exception {
        authorizationData(true, true);
        exp.expect(EmptyDataException.class);
        exp.expectMessage(ExceptionMessages.EMPTY_DATA);
        ac.authenticate(request);
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        authorizationData(false, true);
        boolean success = true;
        ac.register(request);
        try {
            ac.delete(TEST_USER);
        } catch (AccountServiceException e) {
            success = false;
        }
        Assert.assertTrue(success);
    }

    @Test
    public void testDeleteFail() throws Exception {
        authorizationData(false, true);
        boolean success = true;
        ac.register(request);
        try {
            ac.delete(TEST_USER.concat("1337"));
        } catch (AccountServiceException e) {
            success = false;
            Assert.assertTrue(e.getMessage().equals(ExceptionMessages.NO_SUCH_USER_FOUND));
        }
        Assert.assertFalse(success);
    }
}
