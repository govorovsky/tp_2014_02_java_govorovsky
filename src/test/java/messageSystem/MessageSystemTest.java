package messageSystem;

import db.AccountService;
import frontend.Frontend;
import frontend.UserSession;
import util.UserState;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import util.Result;
import util.StringGenerator;

import static org.mockito.Mockito.*;
import static util.StringGenerator.getRandomString;

/**
 * Created by Andrew Govorovsky on 03.04.14
 */
public class MessageSystemTest {
    private static final MessageSystem messageSystem = new MessageSystem();
    private static final AccountService accountService = mock(AccountService.class);
    private static final Frontend frontend = mock(Frontend.class);
    private static final Address frontendAddress = new Address();
    private static final Address accserviceAddress = new Address();

    private ArgumentCaptor<String> loginCaptor = ArgumentCaptor.forClass(String.class);
    private ArgumentCaptor<String> passCaptor = ArgumentCaptor.forClass(String.class);
    private ArgumentCaptor<UserSession> sessionCaptor = ArgumentCaptor.forClass(UserSession.class);

    private static final String USERNAME = getRandomString(7);
    private static final String PASS = getRandomString(7);
    private static final String SSID = getRandomString(7);

    @Before
    public void prepare() {
    }

    @BeforeClass
    public static void setUp() throws Exception {
        when(accountService.getMessageSystem()).thenReturn(messageSystem);
        when(frontend.getAddress()).thenReturn(frontendAddress);
        when(accountService.getAddress()).thenReturn(accserviceAddress);
        messageSystem.addService(frontend);
        messageSystem.addService(accountService);
        messageSystem.getAddressService().addAddress(frontend);
        messageSystem.getAddressService().addAddress(accountService);
    }

    @After
    public void tearDown() throws Exception {

    }

    private boolean verifySession(String username, String ssid, UserState status, Long id) {
        boolean res = sessionCaptor.getValue().getStatus() == status &&
                sessionCaptor.getValue().getName().equals(username) &&
                sessionCaptor.getValue().getSsid().equals(ssid);
        if (id != null) res &= sessionCaptor.getValue().getId().equals(id);
        return res;
    }

    @Test
    public void testSendLoginMessage() throws Exception {

        Long id = (long) StringGenerator.getRandomString(1).charAt(0);

        when(accountService.authenticate(USERNAME, PASS)).thenReturn(new Result<>(id, UserState.AUTHORIZED));

        messageSystem.sendMessage(new MsgLogin(messageSystem.getAddressService().getAddress(frontend.getClass()), messageSystem.getAddressService().getAddress(accountService.getClass()), USERNAME, PASS, SSID));
        messageSystem.execForAbonent(accountService);
        messageSystem.execForAbonent(frontend);

        verify(accountService).authenticate(loginCaptor.capture(), passCaptor.capture());
        Assert.assertEquals(loginCaptor.getValue(), USERNAME);
        Assert.assertEquals(passCaptor.getValue(), PASS);
        verify(frontend, atLeastOnce()).updateUserSession(sessionCaptor.capture());

        Assert.assertTrue(verifySession(USERNAME, SSID, UserState.AUTHORIZED, id));

    }

    @Test
    public void testRegisterMessage() throws Exception {
        when(accountService.register(USERNAME, PASS)).thenReturn(UserState.USER_ADDED);
        messageSystem.sendMessage(new MsgRegister(messageSystem.getAddressService().getAddress(frontend.getClass()), messageSystem.getAddressService().getAddress(accountService.getClass()), USERNAME, PASS, SSID));
        messageSystem.execForAbonent(accountService);
        messageSystem.execForAbonent(frontend);

        verify(accountService).register(loginCaptor.capture(), passCaptor.capture());
        Assert.assertEquals(loginCaptor.getValue(), USERNAME);
        Assert.assertEquals(passCaptor.getValue(), PASS);
        verify(frontend, atLeastOnce()).updateUserSession(sessionCaptor.capture());

        Assert.assertTrue(verifySession(USERNAME, SSID, UserState.USER_ADDED, null));
    }
}
