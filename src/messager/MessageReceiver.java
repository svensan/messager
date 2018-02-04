package messager;

public interface MessageReceiver {
    void receive(Message message, ClientRep sender);
}
