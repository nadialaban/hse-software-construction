package nlaban.hw7;

import java.io.*;

public class ClientReader extends Thread {
    /**
     * Размер массива байтов
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * Поток чтения сообщений с сервера
     */
    private static BufferedReader in;
    /**
     * Поток чтения байтов с сервера
     */
    private static DataInputStream inData;

    /**
     * Установка потоков
     *
     * @param in     - Поток чтения сообщений с сервера
     * @param inData - Поток чтения байтов с сервера
     */
    public static void setReader(BufferedReader in, DataInputStream inData) {
        ClientReader.in = in;
        ClientReader.inData = inData;
    }

    /**
     * Запуск чтения сообщений с сервера
     */
    @Override
    public void run() {
        String str;
        try {
            while (true) {
                if (!Client.getState().equals("sending file")) {
                    str = in.readLine();

                    if (str.equals("stop")) {
                        Client.downService();
                        break;
                    }
                    if (str.startsWith("STATE:")) {
                        var st = str.split(":");
                        Client.setState(st[1]);
                        if (Client.getState().equals("start sending file")) {
                            download(st[2], Integer.parseInt(st[3]));
                        }
                        continue;
                    }
                    System.out.println(str);
                }
            }
        } catch (IOException e) {
            Client.downService();
        }
    }

    /**
     * Загрузка файла
     *
     * @param filename - имя файла
     * @param size     - рамер файла
     */
    private void download(String filename, int size) {
        Client.showProgress(0, size);
        int nRead, n = 0;
        byte[] buff = new byte[BUFFER_SIZE];
        try {
            FileManager.startDownloading(filename);
            do {
                nRead = inData.read(buff, 0, BUFFER_SIZE);
                FileManager.download(buff, nRead);
                n += nRead;
                Client.showProgress(n, size);
            } while (n < size);

            FileManager.stopDownloading();
        } catch (IOException e) {
            Client.downService();
        }
    }

}
