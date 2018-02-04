package userclasses;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import serverclasses.*;

public class Client implements MessageReceiver {
    private String name;
    private Comm connection;

    public Client(String name) {
        this.name = name;
    }

    public void connect(String hostName, int portNr) throws Exception {
        Socket mySocket = new Socket();
        mySocket.connect(new InetSocketAddress(hostName, portNr));
        if (mySocket.isConnected()) {
            connection = new Comm(mySocket);
            connection.registerMessageReceiver(this);
        }
    }

    public void receive(String message) {
        System.out.println("Jag heter " + name + "och läste: " + message);
    }

    public void sendMsg(String message) {
        connection.sendMsg(message);
    }
/*
    public void receiveMessage() {

    }
    */
}
