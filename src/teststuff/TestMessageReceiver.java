package teststuff;

import messager.Message;

public interface TestMessageReceiver {
    void receive(Message message, TestClientRep sender);
}
