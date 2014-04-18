package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Andrew Govorovsky on 13.04.14
 */
public interface VFS {

    boolean isExist(String path);

    boolean isDirectory(String path);

    String getAbsolutePath(String file);

    byte[] getBytes(String file) throws IOException;

    String getUFT8Text(String file) throws IOException;

    Iterator<String> getIterator(String startDir) throws FileNotFoundException;

}
