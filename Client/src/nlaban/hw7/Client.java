package nlaban.hw7;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static Socket client;

    private static BufferedReader reader;

    private static BufferedReader in;
    private static BufferedWriter out;

    private static DataInputStream inData;

    private static String state = "choose";
    private static int pbLength = 0;

    private static boolean init(String host, int port) {
        try {
            client = new Socket(InetAddress.getByName(host), port);

            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            inData = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private static void run() {
        ClientReader.setReader(in, inData);
        ClientSender.setThreads(reader, out);

        new ClientReader().start();
        new ClientSender().start();
    }

    public static void downService() {
        try {
            if (!client.isClosed()) {
                client.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {
        }
    }

    public static void getPath(String path) {
        while (!FileManager.setDirectory(path)) {
            try {
                path = reader.readLine();
            } catch (IOException e) {
                System.out.println("Some problems. Please, try again.");
                System.out.println(e.getMessage());
            }
        }
    }

    private static String getHost() {
        String host;
        System.out.print("Please, enter host\n > ");
        try {
            host = reader.readLine();
        } catch (IOException e) {
            System.out.println("Some problems. Set host = 'localhost'");
            host = "localhost";
        }
        return host;
    }

    private static int getPort() {
        int port = -1;
        System.out.print("Please, enter port [1023;65353]\n > ");
        do {
            try {
                port = Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                System.out.print("Please enter port correctly [1023;65353]\n > ");
                continue;
            }
            if (port < 1023 || port > 65353) {
                System.out.print("Please enter port correctly [1023;65353]\n > ");
            }
        } while (port < 1023 || port > 65353);
        return port;
    }

    public static String getState() {
        return state;
    }

    public static void setState(String state) {
        Client.state = state;
    }

    public static void showProgress(long progress, long size) {
        int percent = (int)((1.0 * progress)/(1.0 * size) * 100);
        char[] progressBar = String.format("|"+"#".repeat(100)+"|\t%d %%\t%d/%d bytes", percent, progress, size).toCharArray();

        for (int i = 1 + percent; i < 101; i++)
            progressBar[i] = '-';

        System.out.print("\b".repeat(pbLength));
        System.out.print(progressBar);

        pbLength = progressBar.length;
    }

    public static void main(String[] args) {
        reader = new BufferedReader(new InputStreamReader(System.in));

        String host = getHost();
        int port = getPort();

        if (init(host, port)) {
            System.out.println("Connect Server:\tSUCCESS!");
            run();
        } else {
            System.out.println("Connect server:\tFAILED");
        }

    }
}
