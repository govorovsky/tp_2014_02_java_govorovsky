import db.AccountServiceTest;
import frontend.FrontendTest;
import functional.FunctionalTests;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by Andrew Govorovsky on 13.03.14
 */
public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(AccountServiceTest.class, FrontendTest.class, FunctionalTests.class);
        if (result.wasSuccessful()) {
            System.out.println("All tests passed successful");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println("Failed test:" + failure.toString());
            }
        }
    }
}
