package util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Created by Andrew Govorovsky on 20.04.14
 */
public class StringGeneratorTest {
    private static Random random = new Random();

    @Test
    public void testGetRandomString() throws Exception {
        int len = random.nextInt(100);
        Assert.assertTrue(StringGenerator.getRandomString(len).length() == len);
    }
}
