package resource;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Andrew Govorovsky on 24.04.14
 */
public class ResourceSystemTest {
    @Test
    public void testGetInstance() throws Exception {
        Assert.assertNotNull(ResourceSystem.getInstance());
    }

    @Test
    public void testGetResource() throws Exception {

    }
}
