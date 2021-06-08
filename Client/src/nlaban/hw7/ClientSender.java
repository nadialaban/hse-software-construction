package nlaban.hw7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ClientSender extends Thread {
    private static BufferedReader reader;
    private static BufferedWriter out;

    public static void setThreads(BufferedReader reader, BufferedWriter out) {
        ClientSender.reader = reader;
        ClientSender.out = out;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String request = reader.readLine();
                if (Client.getState().equals("get path")) {
                    Client.getPath(request);
                    out.write("ok\n");
                    out.flush();
                    continue;
                }
                if (request.equals("stop")) {
                    out.write("stop\n");
                    out.flush();
                    Client.downService();
                    break;
                } else {
                    out.write(request);
                    out.newLine();
                    out.flush();
                }
            } catch (IOException e) {
                Client.downService();
            }

        }
    }

}
