package messager;



public class TestRunnerServer {

    public static void main(String[] args) {
        ServerMultipart server = createServer();
        server.startServer();
    }

    private static ServerMultipart createServer() {
        ServerMultipart server = new ServerMultipart();
        server.setPort(1742);
        return server;
    }
}
