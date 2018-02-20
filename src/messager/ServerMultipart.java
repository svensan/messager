package messager;

public class ServerMultipart extends Server {

    public void receive(Message message, ClientRep sender) {
        System.out.println("msg received");
        if(message.isFileRequest()){
            System.out.println("file req received");
            this.sendMessage(message,this.getOwnerRep());
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
                this.getClients().forEach(p -> {
            if(p.getIP()==IP){
                this.sendMessage(message, p);
            }
        });
    }
        
    }

