import db.AccountServiceTest;
import frontend.FrontendTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by Andrew Govorovsky on 13.03.14
 */
public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(AccountServiceTest.class, FrontendTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println("Failed test:" + failure.toString());
        }
    }
}
