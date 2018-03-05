package messager;

import java.awt.*;

public class MessageFactory {

    /*
    Lägger till denna klass för att minska pressen lite på Message-klassen. Allt denna klass gör är att skapa medelanden,
    men istället för att ha helt odrägliga konstruktorer i Message så kan vi använda denna factory istället.
     */

    private String senderName;
    private Color color = Color.BLACK;

    public Message getChatMessage(String text) {
        return new Message(color, senderName, text);
    }

    public Message getKeyRequest(String algorithm, String text) {
        Message returnMessage = this.getChatMessage(text);
        returnMessage.setIsKeyRequest(true);
        returnMessage.setKeyRequestType(algorithm);
        return returnMessage;
    }

    public Message getFileRequest(FileRequest fileRequest, String text) {
        Message returnMessage = this.getChatMessage(text);
        returnMessage.setFileRequest(fileRequest);
        returnMessage.setIsFileRequest(true);
        return returnMessage;
    }

    public Message getFileResponse(FileResponse fileResponse, String text) {
        Message returnMessage = this.getChatMessage(text);
        returnMessage.setFileResponse(fileResponse);
        returnMessage.setIsFileResponse(true);
        return returnMessage;
    }

    public Message getDisconnect(String text) {
        Message returnMessage = this.getChatMessage(text);
        returnMessage.setDisconnect(true);
        return returnMessage;
    }

    public Message getConnect(String text) {
        Message returnMessage = this.getChatMessage(text);
        returnMessage.setConnectRequest(true);
        return returnMessage;
    }

    public Message getChatError(String text) {
        return new Message(Color.RED, "ERROR", text);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setSenderName(String name) {
        this.senderName = name;
    }

}
