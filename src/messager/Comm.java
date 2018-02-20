package messager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Comm implements Runnable {
    private MessageReceiver receiver;
    private Socket myConnection;
    private PrintWriter output;
    private BufferedReader input;
    private ClientRep myOwner;
    private AtomicBoolean running;
    private Thread runThread;

    public Comm(Socket connection, ClientRep myOwner) {
        myConnection = connection;
        this.myOwner = myOwner;
        running = new AtomicBoolean(true);
        this.setUpStreams();
        Thread runThread = new Thread(this);
        runThread.start();
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

        while (getRunning()) {
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
            } catch(SocketException e){
                System.out.println("just as planned");
            }
            catch (Exception e) {
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

    
    public boolean getRunning(){
        
        return running.get();
    }
    public void close() {
        System.out.println("You closed your socket");
        try {
            running.set(false); 
            System.out.println("1");
//            runThread.interrupt();
            System.out.println("2");

            myConnection.close();
            System.out.println("5");
            try{
            input.close();
            System.out.println("3");
            output.close();
            System.out.println("4");}catch(SocketException e){
                System.out.println("fackit");
                
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
