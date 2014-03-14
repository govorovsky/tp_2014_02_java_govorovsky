package functional;

import com.sun.istack.internal.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import serverMain.GServer;

/**
 * Created by Andrew Govorovsky on 14.03.14
 */
public class FunctionalTests {

    private GServer gServer;
    private WebDriver driver;


    @Before
    public void setUp() throws Exception {
        gServer = new GServer(8080);
        gServer.start();
        driver = new HtmlUnitDriver();
    }

    @After
    public void tearDown() throws Exception {
        gServer.stop();
        gServer.join();
        driver.quit();
    }

    @Test
    public void testAuth() {
        driver.get("http://localhost:8080/auth");
        WebElement element = driver.findElement(By.name("username"));
        element.sendKeys("test");
        element = driver.findElement(By.name("password"));
        element.sendKeys("test");
        element.submit();
        boolean result;
        try {
            result = (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
                @Override
                @NotNull
                public Boolean apply(@NotNull WebDriver d) {
                    WebElement id = d.findElement(By.id("userId"));
                    return id.getText().contains("Your id");
                }
            });
        } catch (Exception e) {
            result = false;
        }
        Assert.assertTrue(result);
    }
}
