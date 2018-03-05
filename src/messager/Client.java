package messager;

import java.awt.Color;

import static java.awt.Color.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements MessageReceiver, Runnable {
    /*
    Även om linjerna blivit lite blurry i det här projektet skulle man kunna
    kalla klienten för en kontroller. Den tar hand om data och skickar den 
    mellan guin och modellen.
    */

    private boolean haveSetName;
    private String name;
    private ClientRep myRepresentation;
    private boolean isAdmin;
    private ChatWindow window;
    Color textColor;
    private ServerMultipart server;
    private String connectedIP;
    private MessageFactory messageFactory;

    private int sentFilePort;//oanvända ?
    private int sentFileSize;
    private String sentFileName;
    private String sentFilePath;

    private boolean usingEncryption = false;
    private String type;
    private byte[] key;
    private boolean delayedEncryption;

    private boolean waitingForFile;


    public Client(boolean admin) {
        /*
        konstruktor, inget spännande.
        */
        messageFactory = new MessageFactory();
        haveSetName = false;
        isAdmin = admin;
        textColor = black;
    }

    public void setSendInfo(String fPath) {
        /*
        Oanvändt i vår implentation?
        */
        sentFilePath = fPath;
    }

    public boolean isWaiting() {
        return waitingForFile;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Color getColor() {
        return textColor;

    }

    public void close() {
        /*
        Skickar ett disconnect medellanden till servern, och stänger sedan
        connection efter att ha chillat ett ögonblick (varför? vi bör ha fel
        hantering i servern)
        
        
        */
        //System.out.println("WAKE ME UP");
        if (this.isAdmin) {
            Message adminDiscM = new Message(Color.red, name, "I'm out, and I'm host so you're going to lose your " +
                    "connection.");

            Message discM = new Message(textColor, name, "Im out lol", true);

            sendMessage(adminDiscM);
            sendMessage(discM);
        } else {
            Message discM = new Message(textColor, name, "Im out lol", true);
            sendMessage(discM);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        myRepresentation.closeConnection();
        if (this.isAdmin) {
            myRepresentation.closeConnection();
        }
    }

    public void setColor(Color aC) {

        messageFactory.setColor(aC);
        textColor = aC;
    }

    public void setServer(ServerMultipart aServer) {
        /*
        Den som är admin måste kunna hålla koll på sin server för o calla 
        olika kommandon, och för att skicka saker mellan client o server
        */
        server = aServer;
    }

    public ServerMultipart getServer() {
        return server;
    }

    public boolean checkIP(String ip) {
        /*
        Kollar om en ip finns med bland de uppkopplade till servern
        Tror den bara användes i debugsyfte under konstruktionen av programmet
        men jag kan ha fel på det.
        */
        ArrayList<String> repList = server.getIPs();

        // System.out.println("replist size: " +repList.size());
        for (int i = 0; i < repList.size(); i++) {
            //System.out.println("replist size: " +repList.size());
            // System.out.println(repList.get(i));
            if (repList.get(i).equals(ip)) {
                //  System.out.println("ip found");
                return true;
            }
        }
        System.out.println("ip not found");
        return false;

    }

    public void getConnection(String host, int port) {
        /*
        Öppnar nytt chattfönster och försöker ansluta till en server, baserat
        på indata. Skickar hardcodeat connect request medellande.
        */
        if (haveSetName) {
            window = new ChatWindow(this);
            try {
                Socket mySocket = new Socket();
                mySocket.connect(new InetSocketAddress(host, port));
                connectedIP = host;
                if (mySocket.isConnected()) {
                    myRepresentation = new ClientRep(mySocket, this);

                    if (delayedEncryption) {
                        delayedEncryption = false;
                        this.setEncryption(this.type);
                    }

                    if (isAdmin) {
                        myRepresentation.setHost(true);
                    }
                    
                    /*Message connectMessage = new Message(red,this.getName(),
                            "Successfully connected to server!");
                    sendMessage(connectMessage);*/


                    if (!this.isAdmin) {
                        Message request = new Message(Color.BLACK,
                                this.name, "hey man id like to connect");
                        request.setConnectRequest();
                        sendMessage(request);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Must set name to connect to a server.");
        }
    }

    public String getConnectedIP() {
        /*
        hämtar server ip
        */
        return connectedIP;
    }

    public void setName(String name) {
        messageFactory.setSenderName(name);
        this.name = name;
        haveSetName = true;
    }

    public String getName() {
        return name;
    }

    public void sendFileRequest(Message message, String IP) {
        /*
        Olika sub-metoder för om användaren är admin eller ej
        */

        if (message.getFileRequest().isUsingEncryption()) {
            this.key = message.getFileRequest().getKey();
            this.type = message.getFileRequest().getType();
        }

        waitingForFile = true;
        if (this.isAdmin) {
            sendServerFileRequest(message, IP);
        } else {
            sendClientFileRequest(message, IP);
        }

        Thread t = new Thread(this);
        t.start();

    }

    private void sendServerFileRequest(Message message, String IP) {
        /*
        Är man admin så använder man serverns metod för o passa pm till 
        mottagare
        */
        server.sendFileRequest(message, IP);
    }

    private void sendClientFileRequest(Message message, String IP) {
        /*
        är man inte admin så skickar man till servern helt enkelt.
        */
        this.sendMessage(message);

    }

    public void sendMessage(Message message) {
        /*
        Passar upp medellandet genom the chain of abstraction så den till slut
        läggs på strömmen
        */
        System.out.println("Client: putStringOnStream");
        myRepresentation.sendString(message);
    }

    public void receiveFR(Message message, String ip) {
        /*
        Specialare för ifall man får en filerequest, så det öppnas fönster.
        */

        if (message.isFileRequest()) {
            System.out.println("ay wtf");

            window.createReceiveWindow(message.getFileRequest(), message.getSenderName(), connectedIP,
                    message.getText());
/*
            if (message.getFileRequest().isUsingEncryption()) {
                window.createReceiveWindow(message.getFileRequest().getFileName(),
                        message.getSenderName(),
                        message.getFileRequest().getFileSize(), connectedIP,
                        message.getText(),
                        message.getFileRequest().getKey(), message.getFileRequest().getType());
            } else {
                window.createReceiveWindow(message.getFileRequest().getFileName(),
                        message.getSenderName(),
                        message.getFileRequest().getFileSize(), ip,
                        message.getText());
            }
*/
        }


    }

    public void receive(Message message, ClientRep sender) {
        /*
        Kollar vad som skall göras med ett mottaget message. Troligen trycks de
        in i chattfönster

        Denna klass tillåter Client-klassen att kontrollera hur medelanden ska
        tas emot. Detta kan göras då Comm-klassen
        använder en receiver för att ta emot medelanden, vi kan alltså låta 
        Comm använda denna receiver för att hantera
        medelanden som vi vill.
        */

        if (message.isFileRequest()) {
            /*
            Används de här när vi har special metoden? 
            
            TODO kolla om detta används
            */
            System.out.println("ay wtf");
            if (message.getFileRequest().isUsingEncryption()) {
                window.createReceiveWindow(message.getFileRequest().getFileName(),
                        message.getSenderName(),
                        message.getFileRequest().getFileSize(), connectedIP,
                        message.getText(),
                        message.getFileRequest().getKey(), message.getFileRequest().getType());
            } else {
                window.createReceiveWindow(message.getFileRequest().getFileName(),
                        message.getSenderName(),
                        message.getFileRequest().getFileSize(), connectedIP,
                        message.getText());
            }

        }
        if (message.isFileResponse() &&
                waitingForFile) {
            /*
            väntar man på en filrespons så öppnar man en filesender o skickar 
            över filen
            
            TODO fixa timeout för filesend
            */
            waitingForFile = false;
            try {
                if (message.getFileResponse().acceptedFileRequest()) {
                    if (usingEncryption) {
                        FileSender sendo = new FileSender(
                                message.getFileResponse().getPort(), sentFilePath, key, type);
                    } else {
                        FileSender sendo = new FileSender(
                                message.getFileResponse().getPort(), sentFilePath);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(
                        Client.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            window.handleMessage(message); // printa i chattfönster
            System.out.flush();
        }
    }

    public void setEncryption(String type) {

        /*
        Denna metod väljer krypterin genom att bestämma hur dennes ClienRep ska konvertera medelanden. Notera att den
        kryptering vi sätter här endast bestämmer vilken kryptering vi skickar med, vi kan fortfarande ta emot
        medelanden med all kryptering som vi stödjer. Indatan för denna metod är en sträng som ska vara namnet på
        krypteringsalgoritmen, t ex AES eller Caesar.
         */

        if (myRepresentation == null) {
            this.delayedEncryption = true;
            this.type = type;
            return;
        }

        if (type.equalsIgnoreCase("AES") || type.equalsIgnoreCase("Caesar")) {
            usingEncryption = true;

            try {
                System.out.println(myRepresentation);

                if (isAdmin) {
                    this.server.registerServerEncryption(type);
                }
                this.myRepresentation.registerMessageConverter(new EncryptedMessageConverter(type));

                //this.sendMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (type.equalsIgnoreCase("none")) {
            if (isAdmin) {
                this.server.registerServerEncryption(type);
            }
            this.myRepresentation.registerMessageConverter(new DefaultMessageConverter());
        } else {

            /*
            Om typen (krypteringsalgoritmen) inte stödjs, så skickar vi ett medelande i loggen, tänker att detta inte ska
            hända, men vi har nån slags felhantering nu.
             */

            System.err.println("The program does not support encryptoin algorithm: " + type + ".");
        }
    }

    public ClientRep getMyRep() {
        return myRepresentation;
    }

    public ChatWindow getWindow() {
        return window;
    }

    public boolean isUsingEncryption() {
        return usingEncryption;
    }

    public String getType() {
        return type;
    }

    public byte[] getKey() {
        return key;
    }

    public void run() {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (waitingForFile) {
            Message noAnswer = messageFactory.getChatError("Receipient did not answer filerequest in 60 s.");
            window.handleMessage(noAnswer);
            waitingForFile = false;
        }
    }
}
