package teststuff;



import messager.Message;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Stream;
import messager.Message;
import messager.Message;

public abstract class TestServer implements TestMessageReceiver {

    private ServerSocket server;
    private ArrayList<TestClientRep> myClients = new ArrayList<>();
    private int amountOfClients = 0;
    private int port;
    private boolean haveSetPort;

    public TestServer() {
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
            TestClientRep newClient = new TestClientRep(newClientCon, this);
            myClients.add(newClient);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setPort(int port) {
        this.port = port;
        this.haveSetPort = true;
    }

    public void sendMessage(Message message, TestClientRep receiver) {
        receiver.sendString(message);
    }

    public void closeEverything() {
        for (TestClientRep client : myClients) {
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

    public Stream<TestClientRep> getClients() {
        return myClients.stream();
    }

    public abstract void receive(Message message, TestClientRep sender);
}