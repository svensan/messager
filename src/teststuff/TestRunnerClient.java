package teststuff;

import messager.Message;
import java.awt.*;
import messager.Message;
import messager.Message;
import messager.Message;
import teststuff.TestClient;

public class TestRunnerClient {

    public static void main(String[] args) {
        TestClient sven = createClient("Sven");
        TestClient max = createClient("Max");
        TestClient ove = createClient("Ove");

//        TestServerMultipart server = createServer();
//        server.startServer();

        sven.getConnection("localhost", 1742);
        max.getConnection("localhost", 1742);
        ove.getConnection("localhost", 1742);

        //server.sendMessageToGroupChat("Hello World!");

        Message myMessage = new Message(Color.BLACK,"Sven","Hello World!");


        sven.sendMessage(myMessage);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        sven.sendMessage("Blää!");

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ove.sendMessage("Hoppas detta funkar");

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Du kom igenom");
        System.out.flush();
*/
//        server.closeEverything();
    }

    private static TestServerMultipart createServer() {
        TestServerMultipart server = new TestServerMultipart();
        server.setPort(1742);
        return server;
    }

    private static TestClient createClient(String name) {
        TestClient client = new TestClient();
        client.setName(name);
        return client;
    }
}
