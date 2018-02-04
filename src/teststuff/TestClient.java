package teststuff;

import messager.Message;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TestClient implements TestMessageReceiver {

    private boolean haveSetName;
    private String name;
    private TestClientRep myRepresentation;

    public TestClient() {
        haveSetName = false;
    }

    public void getConnection(String host, int port) {
        if (haveSetName) {
            try {
                Socket mySocket = new Socket();
                mySocket.connect(new InetSocketAddress(host, port));
                if (mySocket.isConnected()) {
                    myRepresentation = new TestClientRep(mySocket, this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Must set name to connect to a server.");
        }
    }

    public void setName(String name) {
        this.name = name;
        haveSetName = true;
    }

    public void sendMessage(Message message) {
        System.out.println("TestClient: putStringOnStream");
        myRepresentation.sendString(message);
    }

    public void receive(Message message, TestClientRep sender) {
        System.out.println("receive - my name is "+name+" and I just read: "+message.getText()+"\n and it's from: " +
                message.getSenderName());
        System.out.flush();
    }

}
