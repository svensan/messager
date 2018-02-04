package userclasses;

import serverclasses.*;


public class TestDriver {

    public static void main(String[] args) throws Exception {
        Client ove = new Client("Ove");
        Client sven = new Client("Sven");
        ClientRunner svenRunner = new ClientRunner(sven, "Hej Sven");
        ClientRunner oveRunner = new ClientRunner(ove,"Hej Ove");

        new Thread(svenRunner).start();
        new Thread(oveRunner).start();

        Server myServer = new Server();

        //myServer.closeEverything();
    }

    private static class ClientRunner implements Runnable {
        private Client client;
        private String message;

        public ClientRunner(Client client, String message) {

            this.client = client;
            this.message = message;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException("run", e);
            }
            connect();
            testMessage();
        }

        private void testMessage() {
            System.out.println("Client:Send message");
            client.sendMsg(message);
        }

        private void connect() {
            try {
                client.connect("localhost",1742);
            } catch (Exception e) {
                throw new RuntimeException("connect", e);
            }
        }
    }
}
