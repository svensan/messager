package messager;

import java.awt.Color;
import static java.awt.Color.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client implements MessageReceiver {

    private boolean haveSetName;
    private String name;
    private ClientRep myRepresentation;
    boolean isAdmin;
    private ChatWindow window;
    Color textColor;

    public Client(boolean admin) {
        haveSetName = false;
        isAdmin = admin;
        textColor = black;
    }

    public Color getColor(){
        return textColor;
        
    }
    public void setColor(Color aC){
        
        textColor = aC;
    }
    public void getConnection(String host, int port) {
        if (haveSetName) {
            window = new ChatWindow(this);
            try {
                Socket mySocket = new Socket();
                mySocket.connect(new InetSocketAddress(host, port));
                if (mySocket.isConnected()) {
                    myRepresentation = new ClientRep(mySocket, this);
                    Message connectMessage = new Message(red,this.getName(),
                            "Successfully connected to server!");
                    sendMessage(connectMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Must set name to connect to a server.");
        }
    }

    public void setName(String name) {
        this.name = name;
        haveSetName = true;
    }
    
    public String getName(){
        return name;
    }

    public void sendMessage(Message message) {
        System.out.println("Client: putStringOnStream");
        myRepresentation.sendString(message);
    }

    public void receive(Message message, ClientRep sender) {
        /*System.out.println("receive - my name is "+name+" and I just read: "+
                message.getText()+"\n and it's from: " +
                message.getSenderName());*/
        window.handleMessage(message);
        System.out.flush();
    }

}
