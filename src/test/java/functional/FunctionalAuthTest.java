package functional;

import com.sun.istack.internal.NotNull;
import db.AccountService;
import db.AccountServiceImpl;
import db.MysqlDatabase;
import util.UserState;
import messageSystem.AddressService;
import messageSystem.MessageSystem;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import serverMain.GServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.StringGenerator.getRandomString;

/**
 * Created by Andrew Govorovsky on 14.03.14
 */
public class FunctionalAuthTest {

    private static GServer gServer;
    private static HtmlUnitDriver driver;
    private static AccountService accountService;
    private static HttpServletRequest request = mock(HttpServletRequest.class);
    private static final HttpSession session = mock(HttpSession.class);

    private static final MessageSystem messageSystem = mock(MessageSystem.class);
    private static final AddressService addressService = mock(AddressService.class);

    private static final String TEST_USER = getRandomString(7);
    private static final String TEST_PASSWORD = getRandomString(7);


    @BeforeClass
    public static void setUp() throws Exception {
        gServer = new GServer(8090);
        gServer.start();
        driver = new HtmlUnitDriver();
        driver.setJavascriptEnabled(true);


        when(request.getSession()).thenReturn(session);
        when(request.getParameter("username")).thenReturn(TEST_USER);
        when(request.getParameter("password")).thenReturn(TEST_PASSWORD);

        when(messageSystem.getAddressService()).thenReturn(addressService);

        accountService = new AccountServiceImpl(new MysqlDatabase(), messageSystem);
    }

    @Before
    public void registerTestUser() throws Exception {
        accountService.register(TEST_USER, TEST_PASSWORD);
    }


    @After
    public void tearDown() throws Exception {

    }

    @AfterClass
    public static void stopServ() throws Exception {
        gServer.stop();
        gServer.join();
        driver.quit();
    }


    private void deleteTestUser() {
        try {
            accountService.delete(TEST_USER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean Auth(String username, String password, final boolean isOk) {
        driver.get("http://localhost:8090/auth");
        final String elementToFind = (isOk) ? "userId" : "error";
        WebElement element = driver.findElement(By.name("username"));
        element.sendKeys(username);
        element = driver.findElement(By.name("password"));
        element.sendKeys(password);
        element.submit();
        boolean result;
        try {
            result = (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
                @Override
                @NotNull
                public Boolean apply(@NotNull WebDriver d) {
                    boolean res;
                    WebElement id = d.findElement(By.id(elementToFind));
                    if (isOk) res = id.getText().contains("Your id");
                    else res = id.getText().contains(UserState.NO_SUCH_USER_FOUND.getMessage());
                    return res;
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result = false;
        }
        if (isOk) driver.findElement(By.id("quit")).click(); /* Log out ! */
        return result;
    }

    @Test
    public void testAuthSuccess() {
        Assert.assertTrue(Auth(TEST_USER, TEST_PASSWORD, true));
    }

    @Test
    public void testAuthFail() {
        deleteTestUser();
        Assert.assertTrue(Auth(TEST_USER, TEST_PASSWORD, false));
    }
}
