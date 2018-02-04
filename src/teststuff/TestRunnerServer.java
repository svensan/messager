package teststuff;

public class TestRunnerServer {

    public static void main(String[] args) {
        TestServerMultipart server = createServer();
        server.startServer();
    }

    private static TestServerMultipart createServer() {
        TestServerMultipart server = new TestServerMultipart();
        server.setPort(1742);
        return server;
    }
}
