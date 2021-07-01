package nlaban.hw7;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {
    /**
     * Дирктория для загрузки
     */
    private static File directory;
    /**
     * Поток для записи в файл
     */
    private static FileOutputStream fileWriter;

    /**
     * Установка директории загрузки
     *
     * @param path - путь
     * @return получилось ли
     */
    public static boolean setDirectory(String path) {
        directory = new File(path);
        return checkDirectory(path);
    }

    /**
     * Проерка директории
     *
     * @param path - путь
     * @return получилось ли
     */
    private static boolean checkDirectory(String path) {
        if (!directory.exists()) {
            System.out.printf("'%s' does not exist. Please, try again!\n", path);
            return false;
        }
        if (!directory.isDirectory()) {
            System.out.printf("'%s' is not a directory. Please, try again!\n", path);
            return false;
        }
        return true;
    }

    /**
     * Начало загрузки файла
     *
     * @param filename - название файла
     * @throws IOException если что-то не так
     */
    public static void startDownloading(String filename) throws IOException {
        String path = directory.getAbsolutePath() + File.separator + filename;
        String newFilename = filename;
        File file = new File(path);
        int i = 1;
        while (file.exists()) {
            path = path.replace(newFilename, String.format("(%d)%s", i, filename));
            file = new File(path);
            newFilename = String.format("(%d)%s", i, filename);
            i++;
        }
        fileWriter = new FileOutputStream(file);
    }

    /**
     * Загрузка части файа
     *
     * @param bytes - массив байтов
     * @param n     - количество байтов
     * @throws IOException если что-то не так
     */
    public static void download(byte[] bytes, int n) throws IOException {
        fileWriter.write(bytes, 0, n);
    }

    /**
     * Завершение загрузки
     *
     * @throws IOException еслм что-то не так
     */
    public static void stopDownloading() throws IOException {
        fileWriter.close();
    }
}
