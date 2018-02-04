package messager;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ClientRep {

    private Comm connection;
    private SAXParserFactory saxFactory = SAXParserFactory.newInstance();
    private SAXParser parser;

    public ClientRep(Socket connection, MessageReceiver messager) throws Exception {
        this.connection = new Comm(connection, this);
        this.connection.registerMessager(messager);
        this.parser = saxFactory.newSAXParser();
    }

    public void sendString(Message message) {
        String outPutString = handleOutputMessage(message);
        connection.putStringOnStream(outPutString);
    }

    public void closeConnection() {
        connection.close();
    }

    public String handleOutputMessage(Message message) {
        String name = message.getSenderName();
        String hexColor = getHexColor(message.getColor());
        String text = message.getText();

        String holdString = addTagWithAttribute("text","color",hexColor,text);
        return addTagWithAttribute("message","name",name,holdString);
    }

    public Message handleInputMessage(String messageString) throws Exception {
        MyParceHandler myHandler = new MyParceHandler();
        InputStream stream = new ByteArrayInputStream(messageString.getBytes(StandardCharsets.UTF_8.name()));

        try {
            parser.parse(stream, myHandler);
        } catch (Exception e) {
            System.out.println("OOOOps");
        }

        return myHandler.getMessage();
    }

    private String addTagWithAttribute(String tagName, String attributeName, String attributeValue, String text) {
        return String.format("<%1$2s %2$2s=\"%3$2s\">%4$2s</%1$2s>",tagName,attributeName,attributeValue,text);
    }

    private String getHexColor(Color myColor) {
        int r = myColor.getRed();
        int g = myColor.getGreen();
        int b = myColor.getBlue();
        String hexColor = String.format("#%02X%02X%02X", r, g, b);
        return hexColor;
    }

    private static class MyParceHandler extends DefaultHandler {

        private String messageSender;
        private String textColor;
        private String text;
        private boolean haveSetName = false;
        private boolean haveSetColor;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equalsIgnoreCase("message") && !haveSetName) {
                if (attributes.getLocalName(0).equalsIgnoreCase("name")) {
                    messageSender = attributes.getValue(0);
                    haveSetName = true;
                }
            } else if (qName.equalsIgnoreCase("message") && haveSetName) {
                throw new IllegalArgumentException();
            }
            if (qName.equalsIgnoreCase("text") && !haveSetColor) {
                if (attributes.getLocalName(0).equalsIgnoreCase("color")) {
                    textColor = attributes.getValue(0);
                    haveSetColor = true;
                }
            } else if (qName.equalsIgnoreCase("text") && haveSetColor) {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            text = new String(ch, start, length);
        }

        public Message getMessage() {
            Color myColor = createColorFromHex(this.textColor);
            return new Message(myColor, messageSender, text);
        }

        private Color createColorFromHex(String hexColor) {
            int r, g, b;
            r = Integer.valueOf(hexColor.substring(1, 3), 16);
            g = Integer.valueOf(hexColor.substring(3, 5), 16);
            b = Integer.valueOf(hexColor.substring(5, 7), 16);

            return new Color(r,g,b);
        }
    }
}
