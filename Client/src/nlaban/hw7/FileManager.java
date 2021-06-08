package nlaban.hw7;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    private static File directory;
    private static FileOutputStream fileWriter;

    public static boolean setDirectory(String path) {
        FileManager.directory = new File(path);
        return checkDirectory(path, directory);
    }

    private static boolean checkDirectory(String path, File dir) {
        if (!dir.exists()) {
            System.out.printf("'%s' does not exist. Please, try again!\n", path);
            return false;
        }
        if (!dir.isDirectory()) {
            System.out.printf("'%s' is not a directory. Please, try again!\n", path);
            return false;
        }
        return true;
    }

    public static void startDownloading(String filename) throws IOException {
        String path = directory.getAbsolutePath() + File.separator + filename;
        String newFilename = filename;
        File file = new File(path);
        int i = 1;
        while (file.exists()){
            path = path.replace(newFilename, String.format("(%d)%s",i, filename));
            file = new File(path);
            newFilename = String.format("(%d)%s",i, filename);
            i++;
        }
        fileWriter = new FileOutputStream(file);
    }

    public static void download(byte[] bytes, int n) throws IOException {
        fileWriter.write(bytes, 0, n);
    }

    public static void stopDownloading() throws IOException {
        fileWriter.close();
    }
}
