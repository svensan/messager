package messager;

import java.awt.*;

public class Message {

    private Color color;
    private String senderName;
    private String text;
    private boolean isFileRequest = false;
    private FileRequest fileRequest;
    private boolean isFileResponse = false;
    private FileResponse fileResponse;
    
    
    private boolean disconnect;

    public Message(Color color, String senderName, String text) {
        this.color = color;
        this.senderName = senderName;
        this.text = text;

        this.isFileRequest = false;
    }

    public Message(Color color, String senderName, String text,
                       FileRequest fileRequest) {
        this(color, senderName, text);

        this.fileRequest = fileRequest;
        this.isFileRequest = true;
    }

    public Message(String senderName, String text,
                       FileRequest fileRequest) {
        this(Color.BLACK, senderName, text, fileRequest);
    }

    public Message(Color color, String senderName, String text,
                       FileResponse fileResponse) {
        this(color, senderName, text);

        this.fileResponse = fileResponse;
        this.isFileResponse = true;
    }

    public Message(String senderName, String text,
                       FileResponse fileResponse) {
        this(Color.BLACK, senderName, text, fileResponse);
    }
    
    public Message(Color color,String senderName, String text, boolean Disconnect){
        this.color = color;
        this.senderName = senderName;
        this.text = text;
        this.disconnect=Disconnect;

        this.isFileRequest = false;
        
    }

    public Color getColor() {
        return color;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getText() {
        return text;
    }

    public boolean isFileRequest() {
        return isFileRequest;
    }

    public FileRequest getFileRequest() {
        return fileRequest;
    }

    public boolean isFileResponse() {
        return isFileResponse;
    }

    public FileResponse getFileResponse() {
        return fileResponse;
    }
    
    public boolean isDisconnectMessage(){
        return disconnect;
    }
}
