package util;

import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andrew Govorovsky on 13.04.14
 */
public class VFSImpl implements VFS {

    private static final int BUF_SIZE = 4096;
    private String root;

    public VFSImpl(String root) throws FileNotFoundException {
        if (root == null || "".equals(root)) throw new IllegalArgumentException();
        if (!new File(root).exists()) throw new FileNotFoundException();
        this.root = root;
    }

    @Override
    public boolean isExist(String path) {
        return new File(root + path).exists();
    }

    @Override
    public boolean isDirectory(String path) {
        return new File(root + path).isDirectory();
    }

    @Override
    public String getAbsolutePath(String file) {
        return new File(root + file).getAbsolutePath();
    }

    @Override
    public byte[] getBytes(String file) throws IOException {
        InputStream inputStream = new FileInputStream(root + file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUF_SIZE];
        int count;
        while ((count = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        inputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    public String getUFT8Text(String file) throws IOException {
        FileInputStream inputStream = new FileInputStream(root + file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        return builder.toString();
    }

    @Override
    public Iterator<String> getIterator(String startDir) throws FileNotFoundException {
        File start = new File(root + startDir);
        if (!start.exists()) throw new FileNotFoundException();
        return new FileIterator(start);
    }

    private class FileIterator implements Iterator<String> {

        private Queue<File> files = new LinkedList<>();
        private File[] files_in_dir;

        private FileIterator(File start) {
            files.add(start);
        }

        @Override
        public boolean hasNext() {
            return !files.isEmpty();
        }

        @Override
        public String next() {
            File current = files.peek();
            if (current.isDirectory() && ((files_in_dir = current.listFiles()) != null)) {
                Collections.addAll(files, files_in_dir);
            }
            return files.poll().getAbsolutePath();
        }

        @Override
        public void remove() {

        }
    }
}
