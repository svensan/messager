package teststuff;

import messager.Message;
import messager.Message;
import messager.Message;
import messager.Message;
import teststuff.TestClientRep;

public class TestServerNormal extends TestServer {

    public void receive(Message message, TestClientRep sender) {
        System.out.println("Jag är server och jag läste: "+message);
    }

}
