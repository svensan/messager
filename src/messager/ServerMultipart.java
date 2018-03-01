package messager;

import static java.awt.Color.BLACK;
import messager.ChatWindow.ConnectionWindow;

public class ServerMultipart extends Server {
    /*
    Multipart version av servern. Bouncar basically medellanden till alla 
    clientreps, när ett medellande är mottaget
    
    TODO sven ta en titt
    måste även gutta all debug skit
    */

    public void receive(Message message, ClientRep sender) {
        /*
        Vad som händer när servern blir passed ett message.
        */
        if(!sender.acceptedConnection()){
            /*
            om skickaren inte blivit whitelistad händer de här
            */
            if(message.isConnectRequest()){
                /*
                är de en connecto öppnar en connectarone
                */
                sender.setFirstMessage(true);
                this.getOwner().getWindow().createConnectionWindow(
                        message,sender);
                return;
            }
            else if(!sender.firstMessage()){
                /*
                Om de är första medellandet från
                en klient men inte en connecto öppnar en
                connectarone extraordinaree
                */
                sender.setFirstMessage(true);
                this.getOwner().getWindow().createConnectionWindow(sender);
                Message deniedMessage = new Message(BLACK, "Server", "Your "
                        + "client seems like garbage fam i wont lie");
                this.sendMessage(deniedMessage,sender);
                return;
            }
            /*
            Ifall connectionen ej blivit accepted av servern o e andra sakerna
            ej är true får skickaren de här skickat till sig.
            */
            Message deniedMessage = new Message(BLACK, "Server", "Your connection"
                    + " has not been accepted by the server owner");
            this.sendMessage(deniedMessage,sender);
            return;}
        
        System.out.println("msg received");
        if(message.isFileRequest()){
            /*
            ifall de är en filereq så passas den clientreppen direkt till
            admin med ip så filskick kan skötas
            */
            System.out.println("file req received");
            this.getOwner().receiveFR(message, sender.getConnectionAdress());
            return;
        }

        this.getClients().forEach(p -> {
            /*
            
            om inget av ovanstående bouncas medellandet till alla connected.
            */
            
            this.sendMessage(message, p);
            
        });
        
        if(message.isDisconnectMessage()){
            /*
            om de är ett disconnecto så disconnecterineras klienten
            */
            System.out.println("connection closed");
            System.out.println("connection closed");
            System.out.println("connection closed");
            System.out.println("connection closed");
            System.out.println("connection closed");
            System.out.println("connection closed");
            System.out.println("connection closed");
            
            sender.closeConnection();
            if(sender.isHost){
                this.getClients().forEach(p -> {
            
                    p.closeConnection();
            
        });
            }

        }
    }

    public void sendMessageToGroupChat(Message message) {
        /*
        Outdated eller oanvänd ?

        Tror jag tänkte använda denna i multipart, men du använde en annan lösning istället.
        */
        this.getClients().forEach(p -> {
            if(p.acceptedConnection()){
                this.sendMessage(message, p);}
        });
    }
    
    public void sendFileRequest(Message message, String IP){
        /*
        Metod för att skicka en filereq till en specifik ansluten klient
        */
                System.out.println("Looking for: "+ IP);
                this.getClients().forEach(p -> {
                    System.out.println("current: "+ p.getConnectionAdress());
                    if(p.getConnectionAdress().equals(IP)){
                        System.out.println("IP found" +p.getConnectionAdress());
                        this.sendMessage(message, p);
            }
        });
    }
        
    }

