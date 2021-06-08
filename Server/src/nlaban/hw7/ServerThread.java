package nlaban.hw7;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    /**
     * Сокет
     */
    private final Socket socket;

    /**
     * Поток для получения сообщений от клиента
     */
    private final BufferedReader in;
    /**
     * Поток для отправки сообщений клиенту
     */
    private final BufferedWriter out;
    /**
     * Поток для отправки массивов байтов клиенту
     */
    private final DataOutputStream outData;

    /**
     * Текущее состояние клиента
     */
    private String state = "choose";
    /**
     * Номер потока
     */
    private final int index;

    /**
     * Объект для работы с файлами
     */
    private final FileManager fileManager;

    /**
     * Конструктор
     *
     * @param socket - сокет
     * @param index  - номер потока
     * @throws IOException при ошибках в создании потоков
     */
    public ServerThread(Socket socket, int index) throws IOException {
        this.socket = socket;
        this.index = index;

        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        this.outData = new DataOutputStream(this.socket.getOutputStream());

        this.fileManager = new FileManager(index);

        start();
    }

    /**
     * Запуск общения между сервером и клиентом
     */
    @Override
    public void run() {
        String request;
        try {
            sendChooseForm(true);

            while (true) {
                request = in.readLine();
                if (request == null || request.isBlank())
                    continue;

                if (request.equals("stop")) {
                    downService();
                    break;
                }

                if (request.equals("cancel")) {
                    out.write("Server:\tok\n");
                    out.flush();
                    sendChooseForm(false);
                } else {
                    handleRequest(request);
                }
            }
        } catch (IOException e) {
            downService();
        }
    }

    /**
     * Обработка запросов клиента
     * @param request - сообщение клиента
     */
    private void handleRequest(String request) {
        switch (state) {
            case "choose":
                System.out.println("Requested file:\t" + request + "\t[Client " + index + "]");
                sendConfirmation(request);
                break;
            case "confirm":
                sendPathForm(request.toLowerCase());
                break;
            case "get path":
                sendFile();
                break;
            case "file sent":
                System.out.println("File sent:\t" + fileManager.getFilename() + "\t[Client " + index + "]");
                sendChooseForm(true);
                break;
        }
    }

    /**
     * Смена состояния клиента
     * @param st - новое состояние
     */
    void setState(String st) {
        try {
            state = st;
            out.write("STATE:" + state + "\n");
            out.flush();
            System.out.printf("Set state:\t%s\t[Client %d]\n", state, index);
        } catch (IOException ignored) {
        }
    }


    /**
     * Выбор файла для скачивания
     * @param showList - показывать ли список файлов
     */
    private void sendChooseForm(boolean showList) {
        try {
            fileManager.cleanFile();
            String msg = "Server:\t";
            if (showList) {
                msg += String.format("file list\n\n%s\n\n", FileManager.getFileList());
            }
            msg += "Which file do you want to download?\n";
            out.write(msg);
            out.flush();
            setState("choose");
        } catch (IOException ignored) {
        }
    }

    /**
     * Подтверждение скачивания
     * @param filename - название файла
     */
    private void sendConfirmation(String filename) {
        try {
            String msg = "Server:\t";
            String res = fileManager.setFile(filename);
            if (!res.equals("ok")) {
                msg += res;
                out.write(msg);
                out.flush();
                return;
            }
            msg += String.format("'%s' file's size is %d bytes\n" +
                    "Do you want to download it? [yes/no]\n", filename, fileManager.getSize());

            out.write(msg);
            out.flush();

            setState("confirm");
        } catch (IOException ignored) {
        }
    }

    /**
     * Запрос папки скачивания
     * @param answer - ответ на подтверждение
     */
    private void sendPathForm(String answer) {
        try {
            if (answer.equals("yes") || answer.equals("y")) {
                out.write("Server:\tWhere do you want to download file? [send path]\n");
                setState("get path");
            } else if (answer.equals("no") || answer.equals("n")) {
                out.write("Server:\tok\n");
                sendChooseForm(false);
            } else {
                out.write("Server:\tIncorrect answer. Please, try again.\n");
            }
            out.flush();
        } catch (IOException ignored) {
        }
    }

    private void sendFile() {
        try {
            out.write("Server:\tstart sending file...\n");
            out.flush();

            setState(String.format("start sending file:%s:%d", fileManager.getFilename(), fileManager.getSize()));
            fileManager.sendFile();

            Thread.sleep(1000);

            out.write("Server:\tDone!\n");
            out.flush();

            System.out.println("File sent:\t" + fileManager.getFilename() + "\t[Client " + index + "]");
            sendChooseForm(true);
        } catch (IOException | InterruptedException ignored) {
        }
    }

    public void sendPart(byte[] buffer, int nRead) {
        try {
            outData.write(buffer, 0, nRead);
            outData.flush();
        } catch (IOException e) {
            System.out.printf("ERROR:\t%s\t[Client %d]\n", e.getMessage(), index);
        }
    }

    public void downService() {
        try {
            if (!socket.isClosed()) {
                out.write("Server: Disconnected\n");
                out.flush();
                socket.close();
                in.close();
                out.close();
                this.interrupt();
                Server.clientList.remove(this);

                System.out.println("Disconnect:\tSUCCESS!\t[Client " + index + "]");
            }
        } catch (IOException e) {
            System.out.println("Disconnect:\tFAILED!\t[Client " + index + "]");
        }
    }

    public int getIndex() {
        return index;
    }
}
