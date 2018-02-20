package messager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Comm implements Runnable {
    private MessageReceiver receiver;
    private Socket myConnection;
    private PrintWriter output;
    private BufferedReader input;
    private ClientRep myOwner;
    private boolean running = true;

    public Comm(Socket connection, ClientRep myOwner) {
        myConnection = connection;
        this.myOwner = myOwner;

        this.setUpStreams();
        Thread myThread = new Thread(this);
        myThread.start();
    }

    public Socket getSocket(){
        return myConnection;
    }
    
    public String getIP(){
        String retIP = myConnection.getInetAddress().toString().substring(1);
        return retIP;
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

        while (running) {
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
                this.close();
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

    public void registerMessager(MessageReceiver messager) {
        this.receiver = messager;
    }

    public void close() {
        System.out.println("You closed your socket");
        try {
            input.close();
            output.close();
            myConnection.close();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
