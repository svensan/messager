package messager;

public class ServerMultipart extends Server {

    public void receive(Message message, ClientRep sender) {
        System.out.println("msg received");
        if(message.isFileRequest()){
            System.out.println("file req received");
            this.getOwner().receiveFR(message, sender.getConnectionAdress());
            return;
        }

        this.getClients().forEach(p -> {
            
            this.sendMessage(message, p);
            
        });
        
        if(message.isDisconnectMessage()){
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
        this.getClients().forEach(p -> {
            this.sendMessage(message, p);
        });
    }
    
    public void sendFileRequest(Message message, String IP){
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

