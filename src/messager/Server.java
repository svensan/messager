package messager;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Stream;

public abstract class Server implements MessageReceiver {

    private ServerSocket server;
    private ArrayList<ClientRep> myClients = new ArrayList<>();
    private int amountOfClients = 0;
    private int port;
    private boolean haveSetPort;

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

    public abstract void receive(Message message, ClientRep sender);
}