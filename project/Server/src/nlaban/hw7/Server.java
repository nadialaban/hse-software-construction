package nlaban.hw7;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Server {
    private static ServerSocket server;
    public static LinkedList<ServerThread> clientList = new LinkedList<>();
    private static int index = 0;


    /**
     * Инициализация серера
     *
     * @param port - порт
     * @return получилось ли
     */
    private static boolean init(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Запуск сервера
     *
     * @throws IOException если что-то пошло не так
     */
    private static void run() throws IOException {
        while (true) {
            Socket socket = server.accept();

            try {
                clientList.add(new ServerThread(socket, index));
                System.out.println("Connect client:\tSUCCESS!\t[Client " + index + "]");
                index++;
            } catch (IOException e) {
                socket.close();
                System.out.println("Connect client:\tFAILED!\t[Client " + index + "]");
            }
        }
    }

    /**
     * Получение клиента по номеру
     *
     * @param i - номер
     * @return клиент
     */
    public static ServerThread getClient(int i) {
        for (var client : clientList) {
            if (client.getIndex() == i)
                return client;
        }
        return null;
    }

    /**
     * Считываение порта с клавиатуры
     *
     * @param scanner - поток ввода в консоль
     * @return номер порта
     */
    private static int getPort(Scanner scanner) {
        System.out.print("Please, enter port [1023;65353]\n > ");
        int port = -1;
        do {
            while (!scanner.hasNext()) {
                scanner.next();
                System.out.print("Please, enter port[1023;65353]\n > ");
            }
            try {
                port = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter port correctly [1023;65353]\n > ");
                continue;
            }
            if (port < 1023 || port > 65353) {
                System.out.print("Please enter port correctly [1023;65353]\n > ");
            }
        } while (port < 1023 || port > 65353);
        return port;
    }

    /**
     * Утсановка директории сервера
     *
     * @param scanner - поток ввода в консоль
     */
    private static void setDirectory(Scanner scanner) {
        System.out.print("Enter server directory\n > ");

        do {
            while (!scanner.hasNext()) {
                scanner.next();
                System.out.print("Please, enter server directory\n > ");
            }
        } while (!FileManager.setDirectory(scanner.nextLine()));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int port = getPort(scanner);
        setDirectory(scanner);

        try {
            if (init(port)) {
                System.out.println("Start Server:\tSUCCESS!");
                run();
                for (var srv : clientList) {
                    srv.downService();
                }
            } else {
                System.out.println("Start Server:\tFAILED");
            }
        } catch (IOException e) {
            System.out.println("Close Server:\tFAILED");
            System.out.println(e.getMessage());
        }
    }
}
