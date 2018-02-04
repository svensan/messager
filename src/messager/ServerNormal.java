package messager;

public class ServerNormal extends Server {

    public void receive(Message message, ClientRep sender) {
        System.out.println("Jag är server och jag läste: "+message);
    }

}
