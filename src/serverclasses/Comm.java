package serverclasses;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class Comm implements Runnable {
    private MessageReceiver receiver;
    private Socket myConnection;
    private PrintWriter output;
    private BufferedReader input;

    public Comm(Socket conn) throws Exception {
        myConnection = conn;
        this.setUpStreams();
        Thread myThread = new Thread(this);
        myThread.start();
    }

    private void setUpStreams() throws Exception {
        output = new PrintWriter(this.myConnection.getOutputStream());
        input = new BufferedReader(new InputStreamReader(this.myConnection.getInputStream()));
    }

    @Override
    public void run() {
        try {
            System.out.println("Comm:run(read)");
            String message = input.readLine();
            receiveMsg(message);
            System.out.println("Comm:run(message received)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerMessageReceiver(MessageReceiver receiver) {
        this.receiver = receiver;
    }

    public void receiveMsg(String message) throws Exception {
        if (receiver != null) {
            receiver.receive(message);
        }
    }

    public void sendMsg(String message) {
        output.println(message);
        output.flush();
    }

    public void closeStreamsAndSockets() throws Exception {
        output.close();
        input.close();
        myConnection.close();
    }
}
