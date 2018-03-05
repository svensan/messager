package messager;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Stream;

import static java.awt.Color.BLACK;

public abstract class Server implements MessageReceiver {
    /*
    En klass som håller koll på massa client reps som håller koll på massa
    comms som kommunicerar med andra comms. Denna klass har två jobb, det första är att ta emot requests om att få
    connecta till chatten. Det andra är att den tar emot och skickar medelanden. Alla medelanden som skickas kommer
    att gå igenom servern någong gång.

    För mer information av implementationen av Server-klassen, läs ServerMultipart.
    
    TODO SVEN kommentera
    */

    private ServerSocket server;
    private ArrayList<ClientRep> myClients = new ArrayList<>();
    private int amountOfClients = 0;
    private int port;
    private boolean haveSetPort;
    private Client owner;
    private AbstractMessageConverter messageConverter;

    public Server() {
        /*
        En ointressant konstruktor, det ända den gör när den startas är att säga åt sig själv att användaren måste
        bestämma vilken port vi ska leta efter connections på.
         */

        haveSetPort = false;
    }

    public void startServer() {
        /*
        Denna metod startar servern. Observera att den låter servern köras (metoden runServer()) på en separat tråd.
         */

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
        /*
        Denna metod är ganska selfexplanatory, den servern ska göra när den körs är att leta efter nya clients, se
        getNewClient för att se hur det funkar.
         */

        while (true) {
            this.getNewClient();
        }
    }

    private void getNewClient() {
        /*
        Här letar vi efter nya klienter till chatten. Vi börjar med ett specialfall då den första användern joinar,
        eftersom att vi vet att den som hostar servern kommer joina först så ger vi den första som joinar "admin-status".
        Notera att vi inte här ger nya klienter egna trådar, detta är eftersom att deras egna trådar får dom i Comm-klassen.
        Eller mer exakt så skapar en ClientRep en Comm åt sig själv, det är den Comm-instansen som kommer ha en separat
        tråd åt klienten.
         */

        try {
            Socket newClientCon = server.accept();
            ClientRep newClient = new ClientRep(newClientCon, this);

            if (myClients.size() == 0) {
                newClient.setHost(true);
                newClient.acceptConnection();
                newClient.setHaveSentConReq(true);
            } else {
                AbstractMessageConverter clientMessageConverter = this.messageConverter.clone();
                newClient.registerMessageConverter(clientMessageConverter);
            }

            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!newClient.getHaveSentConReq()) {
                    this.getOwner().getWindow().createConnectionWindow(newClient);
                    Message deniedMessage = new Message(BLACK, "Server", "Your "
                            + "client seems like garbage fam i wont lie");
                    this.sendMessage(deniedMessage, newClient);
                }
            });

            t.start();

            myClients.add(newClient);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setPort(int port) {
        /*
        Vi väljer här vilken port servern ska leta efter klienter på.
         */

        this.port = port;
        this.haveSetPort = true;
    }

    public void sendMessage(Message message, ClientRep receiver) {
        /*
        Skickar ett medelande till en specifik klient.
         */

        receiver.sendString(message);
    }

    public void setOwner(Client own) {

        owner = own;

        System.out.println("owner set to " + owner.getName());
    }

    public Client getOwner() {
        System.out.println("Owner is " + owner.getName());
        return owner;
    }

    public void closeEverything() {
        /*
        Försöker stänga ner servern på ett kontrollerat sätt. Vi börjar med att stänga ner alla klienters sockets. Sedan
        stänger vi serversocketen.
         */

        for (ClientRep client : myClients) {
            try {
                client.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            server.close();
            //System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getIPs() {

        ArrayList<String> retList = new ArrayList();

        for (int i = 0; i < myClients.size(); i++) {
            //System.out.println(myClients.get(i).getIP());
            retList.add(myClients.get(i).getIP());
        }
        return retList;
    }

    public Stream<ClientRep> getClients() {
        /*
        Ger oss alla klienter i en ström, denna metod är oerhört användbar i multipartservern då vi måste skicka ut
        medelanden vi får till alla andra klienter.
         */

        return myClients.stream();
    }

    public ClientRep getOwnerRep() {
        ClientRep ret = null;
        for (int i = 0; i < myClients.size(); i++) {
            if (myClients.get(i).isHost()) {
                ret = myClients.get(i);
            }

        }
        return ret;
    }

    public void removeRep(ClientRep rep) {

        /*
        Tar bort en klient från servern.
         */

        myClients.remove(rep);

    }

    public void registerServerEncryption(String algorithm) {

        if (algorithm.equalsIgnoreCase("Caesar") || algorithm.equalsIgnoreCase("AES")) {
            try {
                this.messageConverter = new EncryptedMessageConverter(algorithm);
                this.getClients().forEach(p -> {
                    p.registerMessageConverter(this.messageConverter.clone());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (algorithm.equalsIgnoreCase("none")) {
            this.messageConverter = new DefaultMessageConverter();
            this.getClients().forEach(p -> {
                p.registerMessageConverter(this.messageConverter.clone());
            });
        }
    }

    public abstract void receive(Message message, ClientRep sender);
}