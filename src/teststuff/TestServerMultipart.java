package teststuff;

public class TestServerMultipart extends TestServer {

    public void receive(Message message, TestClientRep sender) {
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
