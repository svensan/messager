package teststuff;

import java.awt.*;

public class Message {

    private Color color;
    private String senderName;
    private String text;

    public Message(Color color, String senderName, String text) {
        this.color = color;
        this.senderName = senderName;
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
}
