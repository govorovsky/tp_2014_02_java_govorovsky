package db;

import util.UserState;
import junit.framework.Assert;
import messageSystem.AddressService;
import messageSystem.MessageSystem;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import util.Result;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.StringGenerator.getRandomString;

/**
 * Created by Andrew Govorovsky on 12.03.14
 */
public class AccountServiceTest {
    private AccountService ac = new AccountServiceImpl(new HsqlDatabase(), messageSystem);

    private static final MessageSystem messageSystem = mock(MessageSystem.class);
    private static final AddressService addressService = mock(AddressService.class);

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
        when(messageSystem.getAddressService()).thenReturn(addressService);
    }

    @After
    public void tearDown() throws Exception {
        deleteTestUser();
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        UserState res = ac.register(TEST_USER, TEST_PASSWORD);
        Assert.assertTrue(res.isSuccess());
    }

    @Test
    public void testRegisterDuplicationName() throws Exception {
        ac.register(TEST_USER, TEST_PASSWORD);
        Assert.assertTrue(ac.register(TEST_USER, TEST_PASSWORD) == UserState.USER_ALREADY_EXISTS);
    }


    @Test
    public void testAuthenticateSuccess() throws Exception {
        ac.register(TEST_USER, TEST_PASSWORD);
        Result<Long> result = ac.authenticate(TEST_USER, TEST_PASSWORD);
        Assert.assertTrue(result.getData() > 0);
    }

    @Test
    public void testAuthenticateFail() throws Exception {
        ac.register(TEST_USER, TEST_PASSWORD);
        Result<Long> result = ac.authenticate(TEST_USER, TEST_PASSWORD.concat("32"));
        Assert.assertTrue(result.getData() == null);
        Assert.assertTrue(result.getStatus() == UserState.FAILED_AUTH);
    }


    @Test
    public void testCheckLoginPasswordFail() throws Exception {
        Assert.assertTrue(ac.authenticate("", "").getStatus() == UserState.EMPTY_DATA);
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        ac.register(TEST_USER, TEST_PASSWORD);
        Assert.assertTrue(ac.delete(TEST_USER).isSuccess());
    }

    @Test
    public void testDeleteFail() throws Exception {
        ac.register(TEST_USER, TEST_PASSWORD);
        Assert.assertTrue(ac.delete(TEST_USER.concat("1337")) == UserState.NO_SUCH_USER_FOUND);
    }
}
