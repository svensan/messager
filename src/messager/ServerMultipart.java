package messager;

public class ServerMultipart extends Server {

    public void receive(Message message, ClientRep sender) {
        this.getClients().forEach(p -> {
            if (p != sender) {
                this.sendMessage(message, p);
            }
        });
    }

    public void sendMessageToGroupChat(Message message) {
        this.getClients().forEach(p -> {
            this.sendMessage(message, p);
        });
    }
}
