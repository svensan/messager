package messager;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Stream;

public abstract class Server implements MessageReceiver {
    /*
    En klass som håller koll på massa client reps som håller koll på massa
    comms som kommunicerar med andra comms.
    
    TODO SVEN kommentera
    */

    private ServerSocket server;
    private ArrayList<ClientRep> myClients = new ArrayList<>();
    private int amountOfClients = 0;
    private int port;
    private boolean haveSetPort;
    private Client owner;

    public Server() {
        haveSetPort = false;
    }

    public void startServer() {
        if (haveSetPort) {
            try {
                server = new ServerSocket(port);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runServer();
                    }
                });
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("You need to set port before starting server.");
        }
    }

    private void runServer() {
        while (true) {
            this.getNewClient();
        }
    }

    private void getNewClient() {
        try {
            Socket newClientCon = server.accept();
            ClientRep newClient = new ClientRep(newClientCon, this);
            if(myClients.size()==0){
                newClient.setHost(true);
                newClient.acceptConnection();
            }
            myClients.add(newClient);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setPort(int port) {
        this.port = port;
        this.haveSetPort = true;
    }

    public void sendMessage(Message message, ClientRep receiver) {
        receiver.sendString(message);
    }

    public void sendMessage(Message message, ClientRep receiver, String ip) {
        receiver.sendString(message);
    }

    public void setOwner(Client own){ 
        
        owner = own;
        
        System.out.println("owner set to "+owner.getName());
    }
    
    public Client getOwner(){
        System.out.println("Owner is " + owner.getName());
        return owner;
    }
    
    public void closeEverything() {
        for (ClientRep client : myClients) {
            try {
                client.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            server.close();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getIPs(){
        
        ArrayList<String> retList = new ArrayList();
 
        for(int i =0;i<myClients.size();i++){
            //System.out.println(myClients.get(i).getIP());
            retList.add(myClients.get(i).getIP());
        }
        return retList;
    }
    public Stream<ClientRep> getClients() {
        return myClients.stream();
    }

    public ClientRep getOwnerRep(){
        ClientRep ret = null;
        for(int i = 0;i<myClients.size();i++){
            if(myClients.get(i).isHost()){
                ret = myClients.get(i);
            }
        
        }
    return ret;
    }
    
    public void removeRep(ClientRep rep){
        
        myClients.remove(rep);
        
    }
    
    public abstract void receive(Message message, ClientRep sender);
}