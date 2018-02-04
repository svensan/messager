package teststuff;

public interface TestMessageReceiver {
    void receive(Message message, TestClientRep sender);
}
