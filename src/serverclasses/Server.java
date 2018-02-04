package serverclasses;

import java.net.*;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Server {

    private ServerSocket server;
    private ArrayList<ClientRepresentative> myClients = new ArrayList<>();
    private Socket myNextClient;
    private int amountOfClients = 0;
    private ChatRoom myTestRoom;

    public Server() {
        myTestRoom = new ChatRoom();
        this.startServer();
    }

    private void translateAndSendMessage(String inText) {
        //Skriv om till xml och läs av om vi vill ha kryptering och dylikt.
    }

    private void startServer() {
        try {
            server = new ServerSocket(1742, 5);
            while (true) {
                try {
                    this.getClient();
                } catch (Exception e) {
                    this.onScreenMessage("serverclasses.Server is down.");

                    //e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("startServer", e);
        } finally {
            this.closeEverything();
        }
    }

    private void getClient() throws Exception {
        //this.onScreenMessage("Waiting for client.");
        if (amountOfClients < 3) {
            Socket newClient = server.accept();
            if (newClient.isConnected()) {
                ClientRepresentative client = new ClientRepresentative(newClient);
                client.setMyCurrentChatRoom(myTestRoom);
                myClients.add(client);
                myTestRoom.addClient(myClients.get(amountOfClients));
                amountOfClients++;
            }
        } else {
            try {
                System.out.println("Du har connectat två users");
                System.console().readLine();
                System.out.println("Done reading");
                Thread.sleep(5000);
            } catch (Exception e) {
                throw new RuntimeException("getClient", e);
            }
        }
    }

    private void onScreenMessage(String showText) throws Exception {

    }

    public void closeEverything() {
        for (ClientRepresentative client : myClients
                ) {
            try {
                client.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class ClientRepresentative implements MessageReceiver {
        private String name;
        private Comm connection;
        private int hej;

        public ClientRepresentative setMyCurrentChatRoom(ChatRoom myCurrentChatRoom) {
            this.myCurrentChatRoom = myCurrentChatRoom;
            return this;
        }

        private ChatRoom myCurrentChatRoom = null;

        private ClientRepresentative(Socket socketConnection) {
            try {
                connection = new Comm(socketConnection);
                connection.registerMessageReceiver(this);
            } catch (Exception e) {
                throw new IllegalStateException("Oooops", e);
            }
        }

        public void run() {
            //Do stuff.
        }

        @Override
        public void receive(String msg) {
            System.out.println("Server:receive " + msg);
            this.sendMessage(msg);
            System.out.println("Server:receive handled");
        }

        public String getName() {
            return this.name;
        }

        public void messageMe(String message) {
            connection.sendMsg(message);
        }

        public void sendMessage(String message) {
            if (myCurrentChatRoom != null) {
                myCurrentChatRoom.getAllClients().forEach(p -> {
                    if (p != this) {
                        p.messageMe(message);
                    }
                });
            }
        }

        public void closeConnection() throws Exception {
            connection.closeStreamsAndSockets();
        }
    }

    private class ChatRoom {
        private ArrayList<ClientRepresentative> myClients;
        private ClientRepresentative creator;

        private ChatRoom(ClientRepresentative myGod) {
            creator = myGod;
            myClients = new ArrayList<>();
            this.addClient(creator);
        }

        private ChatRoom() {
            myClients = new ArrayList<>();
        }

        public String getName() {
            return creator.getName() + "s chat room";
        }

        public void addClient(ClientRepresentative newClient) {
            if (!myClients.contains(newClient)) {
                myClients.add(newClient);
            }
        }

        public void removeClient(ClientRepresentative client) {
            if (myClients.contains(client)) {
                myClients.remove(client);
            }
        }

        public Stream<ClientRepresentative> getAllClients() {
            return myClients.stream();
        }
    }
}
