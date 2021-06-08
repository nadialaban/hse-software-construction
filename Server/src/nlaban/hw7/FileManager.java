package nlaban.hw7;

import java.io.*;
import java.util.Objects;

public class FileManager {
    private static final int BUFFER_SIZE = 8192;

    private final int index;

    private static File directory;
    private File file;

    public FileManager(int index) {
        this.index = index;
    }

    public void sendFile() {
        var client = Server.getClient(index);
        if (client == null)
            return;
        try {
            FileInputStream fileReader = new FileInputStream(file);

            byte[] buffer = new byte[BUFFER_SIZE];
            int nRead;
            while ((nRead = fileReader.read(buffer, 0, BUFFER_SIZE)) >= 0) {
                client.sendPart(buffer, nRead);
            }

            fileReader.close();
            client.setState("file sent");
        } catch (IOException ignored) {
        }
    }


    public static String getFileList() {
        return "- " + String.join("\n- ", Objects.requireNonNull(directory.list()));
    }

    public static boolean setDirectory(String path) {
        FileManager.directory = new File(path);
        return checkDirectory(path, directory);
    }

    private static boolean checkDirectory(String path, File dir) {
        if (!dir.exists()) {
            System.out.printf("'%s' does not exist. Please, try again!\n > ", path);
            return false;
        }
        if (!dir.isDirectory()) {
            System.out.printf("'%s' is not a directory. Please, try again!\n > ", path);
            return false;
        }
        return true;
    }

    public String setFile(String filename) {
        file = new File(directory.getPath() + File.separator + filename);
        if (!file.exists())
            return String.format("file '%s' does not exist\n", filename);
        if (!file.isFile())
            return String.format("'%s' is not a file\n", filename);
        if (file.length() > 137_438_883_103L)
            return String.format("'%s' file's size is over the limit (128 GB)\n", filename);
        return "ok";
    }

    public long getSize() {
        return file.length();
    }

    public String getFilename() {
        return file.getName();
    }

    public void cleanFile() {
        file = null;
    }
}
