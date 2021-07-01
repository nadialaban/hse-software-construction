package nlaban.hw7;

import java.io.*;
import java.util.Objects;

public class FileManager {
    /**
     * Размер массива байтов
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * Номер клиента
     */
    private final int index;

    /**
     * Директория сервера
     */
    private static File directory;
    /**
     * Скачиваемый файл
     */
    private File file;

    /**
     * Конструктор
     *
     * @param index - номер клиента
     */
    public FileManager(int index) {
        this.index = index;
    }

    /**
     * Отправка файла клиенту
     */
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


    /**
     * Получение списка файлов директории
     *
     * @return строку со списком
     */
    public static String getFileList() {
        return "- " + String.join("\n- ", Objects.requireNonNull(directory.list()));
    }

    /**
     * Установка директории
     *
     * @param path - путь
     * @return получилось ли
     */
    public static boolean setDirectory(String path) {
        FileManager.directory = new File(path);
        return checkDirectory(path);
    }

    /**
     * Проверка директории
     *
     * @param path - путь
     * @return есть ли папка
     */
    private static boolean checkDirectory(String path) {
        if (!FileManager.directory.exists()) {
            System.out.printf("'%s' does not exist. Please, try again!\n > ", path);
            return false;
        }
        if (!FileManager.directory.isDirectory()) {
            System.out.printf("'%s' is not a directory. Please, try again!\n > ", path);
            return false;
        }
        return true;
    }

    /**
     * Установка фвйла для сачивания
     *
     * @param filename - имя файла
     * @return - получилось ли
     */
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

    /**
     * @return размер файла скачивания
     */
    public long getSize() {
        return file.length();
    }

    /**
     * @return имя файла скачивания
     */
    public String getFilename() {
        return file.getName();
    }

    /**
     * удаляем выбор файла
     */
    public void cleanFile() {
        file = null;
    }
}
