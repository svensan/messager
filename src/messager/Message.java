package messager;

import java.awt.*;
import static java.awt.Color.BLACK;

public class Message {
    /*
    En klass som är ett medellande. Kan  vara en filerequest eller liknande
    Ganska självförklarligt, det mesta här.
    
    VÄLDIGT många konstruktorer för olika typer av mesg.
    
    massa get o set.
    */

    private Color color=BLACK;
    private String senderName;
    private String text;
    private boolean isFileRequest = false;
    private FileRequest fileRequest;
    private boolean isFileResponse = false;
    private FileResponse fileResponse;
    private boolean isKeyRequest = false;
    private String keyRequestType;
    private boolean disconnect;
    private boolean isConnectRequest = false;

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

    public Message(String senderName, String text) {
        this.senderName=senderName;
        this.text = text;
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

    protected boolean isKeyRequest() {
        return isKeyRequest;
    }

    public boolean isConnectRequest(){
        
        return isConnectRequest;
    }
    
    public void setConnectRequest(){
        isConnectRequest = true;
    }

    protected String getKeyRequestType() {
        return keyRequestType;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIsFileRequest(boolean fileRequest) {
        isFileRequest = fileRequest;
    }

    public void setFileRequest(FileRequest fileRequest) {
        this.fileRequest = fileRequest;
    }

    public void setIsFileResponse(boolean fileResponse) {
        isFileResponse = fileResponse;
    }

    public void setFileResponse(FileResponse fileResponse) {
        this.fileResponse = fileResponse;
    }

    public void setIsKeyRequest(boolean keyRequest) {
        isKeyRequest = keyRequest;
    }

    public void setKeyRequestType(String keyRequestType) {
        this.keyRequestType = keyRequestType;
    }

    public void setDisconnect(boolean disconnect) {
        this.disconnect = disconnect;
    }

    public void setConnectRequest(boolean connectRequest) {
        isConnectRequest = connectRequest;
    }
}
