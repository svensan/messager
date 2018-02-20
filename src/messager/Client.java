package messager;

import java.awt.Color;
import static java.awt.Color.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements MessageReceiver {

    private boolean haveSetName;
    private String name;
    private ClientRep myRepresentation;
    private boolean isAdmin;
    private ChatWindow window;
    Color textColor;
    private ServerMultipart server;
    private String connectedIP;
    
    private int sentFilePort;
    private int sentFileSize;
    private String sentFileName;
    private String sentFilePath;
    
    private boolean waitingForFile;
    
    

    public Client(boolean admin) {
        haveSetName = false;
        isAdmin = admin;
        textColor = black;
    }
    
    public void setSendInfo(int port, int size, String fName, String fPath){
        
        sentFilePort = port;
        sentFileSize = size;
        sentFileName = fName;
        sentFilePath = fPath;
    }

    public boolean isAdmin(){
        return isAdmin;
    }
    
    public Color getColor(){
        return textColor;
        
    }
    
    public void close(){
        System.out.println("WAKE ME UP");
        Message discM = new Message(textColor,name, "Im out lol",true);
        sendMessage(discM);
        try{
        Thread.sleep(5);}
        catch(InterruptedException e){
        }
        
        myRepresentation.closeConnection();
    }
    public void setColor(Color aC){
        
        textColor = aC;
    }
    
    public void setServer(ServerMultipart aServer){
        server = aServer;
    }
    public ServerMultipart getServer(){
        return server;
    }
    
    public boolean checkIP(String ip){
        
        ArrayList<String> repList = server.getIPs();
        
       // System.out.println("replist size: " +repList.size());
        for(int i = 0; i < repList.size();i++){
            //System.out.println("replist size: " +repList.size());
           // System.out.println(repList.get(i));
            if(repList.get(i).equals(ip)){
              //  System.out.println("ip found");
                return true;
            }
        }
        System.out.println("ip not found");
        return false;
        
    }
    public void getConnection(String host, int port) {
        if (haveSetName) {
            window = new ChatWindow(this);
            try {
                Socket mySocket = new Socket();
                mySocket.connect(new InetSocketAddress(host, port));
                connectedIP = host;
                if (mySocket.isConnected()) {
                    myRepresentation = new ClientRep(mySocket, this);
                    
                    if(isAdmin){myRepresentation.setHost(true);}
                    
                    /*Message connectMessage = new Message(red,this.getName(),
                            "Successfully connected to server!");
                    sendMessage(connectMessage);*/
                    
                    Message request = new Message(Color.BLACK, 
                            this.name, "hey man id like to connect");
                    request.setConnectRequest();
                    sendMessage(request);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Must set name to connect to a server.");
        }
    }

    public String getConnectedIP(){
        return connectedIP;
    }
    
    public void setName(String name) {
        this.name = name;
        haveSetName = true;
    }
    
    public String getName(){
        return name;
    }
    
    public void sendFileRequest(Message message,String IP){
        waitingForFile = true;
        if(this.isAdmin){
            sendServerFileRequest(message, IP);
        }
        else{
            sendClientFileRequest(message, IP);
        }
        
    }
    
    private void sendServerFileRequest(Message message,String IP){
            server.sendFileRequest(message, IP);
    }
    private void sendClientFileRequest(Message message,String IP){
            this.sendMessage(message);
        
    }

    public void sendMessage(Message message) {
        System.out.println("Client: putStringOnStream");
        myRepresentation.sendString(message);
    }

    public void receiveFR(Message message, String ip){
        
            if(message.isFileRequest()){
            System.out.println("ay wtf");
            window.createReceiveWindow(message.getFileRequest().getFileName(),
                    message.getSenderName(),
                    message.getFileRequest().getFileSize(),ip);
            
        }
        
        
    }
    public void receive(Message message, ClientRep sender) {
        /*System.out.println("receive - my name is "+name+" and I just read: "+
                message.getText()+"\n and it's from: " +
                message.getSenderName());*/
        if(message.isFileRequest()){
            System.out.println("ay wtf");
            window.createReceiveWindow(message.getFileRequest().getFileName(),
                    message.getSenderName(),
                    message.getFileRequest().getFileSize(),connectedIP);
            
        }
        if(message.isFileResponse()&&
                waitingForFile){
            waitingForFile = false;
            try {
                FileSender sendo = new FileSender(
                        message.getFileResponse().getPort(),sentFilePath);
            } catch (IOException ex) {
                Logger.getLogger(
                        Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else{
            window.handleMessage(message);
            System.out.flush();
        }
    }
    
    public ClientRep getMyRep(){
        return myRepresentation;
    }

    public ChatWindow getWindow(){
        return window;
    }
}
