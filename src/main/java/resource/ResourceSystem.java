package resource;

import util.vfs.VFS;
import util.vfs.VFSImpl;
import util.sax.XMLReaderSax;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Andrew Govorovsky on 23.04.14
 */
public class ResourceSystem {
    private final Map<String, Resource> resourceMap = new HashMap<>();
    private static volatile ResourceSystem instance;

    private ResourceSystem() {
        loadResources();
    }

    public static ResourceSystem getInstance() {
        if (instance == null) {
            synchronized (ResourceSystem.class) {
                if (instance == null) {
                    instance = new ResourceSystem();
                }
            }
        }
        return instance;
    }

    private void loadResources() {
        try {
            VFS vfs = new VFSImpl();
            String resource_dir = "/data";
            Iterator<String> resources = vfs.getIterator(resource_dir);
            while (resources.hasNext()) {
                String res = resources.next();
                if (!vfs.isDirectory(res)) {
                    Object resource = XMLReaderSax.readXML(res);
                    if (resource instanceof Resource)
                        resourceMap.put(vfs.getFileName(res), (Resource) resource);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Resource getResource(String name) {
        return resourceMap.get(name);
    }
}
