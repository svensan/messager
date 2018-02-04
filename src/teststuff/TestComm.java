package teststuff;

import messager.Message;
import serverclasses.MessageReceiver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestComm implements Runnable {
    private TestMessageReceiver receiver;
    private Socket myConnection;
    private PrintWriter output;
    private BufferedReader input;
    private TestClientRep myOwner;

    public TestComm(Socket connection, TestClientRep myOwner) {
        myConnection = connection;
        this.myOwner = myOwner;

        this.setUpStreams();
        Thread myThread = new Thread(this);
        myThread.start();
    }

    private void setUpStreams() {
        try {
            output = new PrintWriter(this.myConnection.getOutputStream());
            input = new BufferedReader(new InputStreamReader(this.myConnection.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                System.out.println("1 ");
                System.out.flush();
                String stringMessage = input.readLine();
                if (stringMessage == null) {
                    throw new RuntimeException("Peer dead");
                }
                System.out.println("2 " + stringMessage);
                System.out.flush();
                sleep(10);
                Message message = myOwner.handleInputMessage(stringMessage);
                receiveMessage(message);
                System.out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sleep(int sleepInterval) {
        try {
            Thread.sleep(sleepInterval);
        } catch (InterruptedException e) { }
    }

    private void receiveMessage(Message message) {
        receiver.receive(message, myOwner);
    }

    public void putStringOnStream(String message) {
        output.println(message);
        output.flush();
        System.out.println("test comm - sent " + message);
        System.out.flush();
    }

    public void registerMessager(TestMessageReceiver messager) {
        this.receiver = messager;
    }

    public void close() {
        try {
            input.close();
            output.close();
            myConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
