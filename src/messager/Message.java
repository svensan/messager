package messager;

import java.awt.*;

public class Message {

    private Color color;
    private String senderName;
    private String text;
    private boolean isEncrypted = false;
    private String key;

    public Message(Color color, String senderName, String text) {
        this.color = color;
        this.senderName = senderName;
        this.text = text;
    }

    public Message(Color color, String senderName, String text, 
            boolean isEncrypted, String key) {
        this(color, senderName, text);
        this.isEncrypted = isEncrypted;
        this.key = key;
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

    public String getKey() {
        return key;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }
}
