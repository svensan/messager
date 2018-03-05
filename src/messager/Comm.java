package messager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Comm implements Runnable {
    
    /*
    A class that wraps the socket to simplify sending and receiving messages.
    This class is dynamic in the sense that it
    contains a interface which chooses how one receives messages (receiver) 
    so that the class that want to use Comm
    can decide how messages are received.
    
    TODO SVEN kommentera
    */

    private MessageReceiver receiver;
    private Socket myConnection;
    private PrintWriter output;
    private BufferedReader input;
    private ClientRep myOwner;
    private AtomicBoolean running;

    public Comm(Socket connection, ClientRep myOwner) {
        /*
        Denna konstruktor är högst relevant eftersom att det är här vi trådar
        av separata användare. Alltså när en ny
        klient joinar servern så skapar vi en ClientRep åt dom, och det är här
        vi ger den ClientRep-instansen en egen
        tråd.
         */

        myConnection = connection;
        this.myOwner = myOwner;
        running = new AtomicBoolean(true);
        this.setUpStreams();
        Thread runThread = new Thread(this);
        runThread.start();
    }

    public Socket getSocket() {
        return myConnection;
    }

    public String getIP() {
        String retIP = myConnection.getInetAddress().toString().substring(1);
        return retIP;
    }

    private void setUpStreams() {
        /*
        Sets up the output and input stream. To simplify message sending we
        use a BufferedReader on the inputstream
        and a printwriter on the output stream.
         */
        try {
            output = new PrintWriter(this.myConnection.getOutputStream());
            input = new BufferedReader(
                    new InputStreamReader(this.myConnection.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (getRunning()) {
            /*
            Sitter o lyssnar på strömmen efter medellanden. After a message is
            received the instance calls it's owner to
            handle the inputstring, sending the message up in the class
            hierarchy.
            */
            try {
                //System.out.println("1 ");
                System.out.flush();
                String stringMessage = input.readLine();
                if (stringMessage == null) {
                    /*
                    Om connectionen bryts så stänger vi för att undvika galna
                    exceptions. Ifall servern märker att någon droppat så tas
                    de ur klient listan, så de kan ansluta igen eller så.
                    */
                    this.close();
                    if (receiver instanceof Server || receiver instanceof
                            ServerMultipart) {
                        Server sReceiver = (Server) receiver;
                        sReceiver.removeRep(this.myOwner);
                    }
                    System.out.println("Peer dead.");

                } else {
                    //System.out.println("2 " + stringMessage);
                    System.out.flush();
                    sleep(10);
                    Message message = myOwner.handleInputMessage(stringMessage);

                    receiveMessage(message);
                    System.out.flush();
                }
            } catch (SocketException e) {
                /*
                Har o göra med disconnecten för o undvika trådlås typ ?
                */
                System.out.println("just as planned");
                this.close();
            } catch (Exception e) {
                this.close();
                e.printStackTrace();
            }
        }
    }

    private void sleep(int sleepInterval) {
        /*
        forces the thread to sleep.
         */
        try {
            Thread.sleep(sleepInterval);
        } catch (InterruptedException e) {
        }
    }

    private void receiveMessage(Message message) {
        /*
        The class name feels selfexplanatory.
         */
        receiver.receive(message, myOwner);
    }

    public void putStringOnStream(String message) {
        /*
        Skriver en medellande sträng på output strömmen  så servern får tag i
        den
        */
        output.println(message);
        output.flush();
        // System.out.println("test comm - sent " + message);
        System.out.flush();
    }

    public void registerMessager(MessageReceiver messager) {
        /*
        The receiver is needed to handle a incomming message, 
        here we register the receive method. That is, we register
        what the Comm should do when it receives a message.
         */
        this.receiver = messager;
    }

    public boolean getRunning() {
        /*
        Kollar om den lyssnar
        */
        return running.get();
    }

    public void close() {
        System.out.println("You closed your socket");
        try {
            /*
            Stänger connectionen på ett sätt så vi ej får trådlås
            todo ta bort all debug skiten
            */
            running.set(false);

            myConnection.close();

            try {
                input.close();

                output.close();

            } catch (SocketException e) {
                System.out.println("fackit");

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
