package messager;

import java.awt.*;

public class TestRunnerClient {

    public static void main(String[] args) {
        Client sven = createClient("Sven");
        Client max = createClient("Max");
        Client ove = createClient("Ove");

//        ServerMultipart server = createServer();
//        server.startServer();

        sven.getConnection("localhost",10000 );


        //server.sendMessageToGroupChat("Hello World!");

        Message myMessage = new Message(Color.BLACK,"Sven","Hello World!");


        sven.sendMessage(myMessage);


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

    private static ServerMultipart createServer() {
        ServerMultipart server = new ServerMultipart();
        server.setPort(1742);
        return server;
    }

    private static Client createClient(String name) {
        Client client = new Client(false);
        client.setName(name);
        return client;
    }
}
