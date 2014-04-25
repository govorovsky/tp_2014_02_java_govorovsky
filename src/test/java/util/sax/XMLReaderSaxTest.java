package util.sax;

import org.junit.Assert;
import org.junit.Test;
import resource.TestResource;

/**
 * Created by Andrew Govorovsky on 25.04.14
 */
public class XMLReaderSaxTest {
    private static final String TEST_XML_PATH = "test_data/";

    @Test
    public void readXMLTest() throws Exception {
        String testXML = "test.xml";
        int testInt = 123;
        char testChr = 'T';
        String testStr = "Test";
        Object testRes = XMLReaderSax.readXML(TEST_XML_PATH + testXML);
        Assert.assertNotNull(testRes);
        Assert.assertTrue(testRes instanceof TestResource);
        Assert.assertTrue(((TestResource) testRes).getTestInt() == testInt);
        Assert.assertTrue(((TestResource) testRes).getTestChr() == testChr);
        Assert.assertTrue(((TestResource) testRes).getTestStr().equals(testStr));
    }
}
