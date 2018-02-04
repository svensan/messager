package teststuff;

import messager.Message;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;


public class JavaParcing {

    public static void main(String[] args) throws Exception {
        testSAX();
    }

    static void testSAX() throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser parser = saxFactory.newSAXParser();
        MyHandler myHandler = new MyHandler();
        String hexColor;
        Color myColor = Color.BLACK;
        int r = myColor.getRed();
        int g = myColor.getGreen();
        int b = myColor.getBlue();
        hexColor = String.format("#%02X%02X%02X", r, g, b);
        InputStream stream = new ByteArrayInputStream(("<message name=\"Sven\"><text color=\""+hexColor+"\">Hello World!</text></message>").getBytes(StandardCharsets.UTF_8.name()));
        try {
            parser.parse(stream, myHandler);
        } catch (Exception e) {
            System.out.println("OOOOps");
        }

        Message myMessage = myHandler.getMessage();

        System.out.println(myMessage.getSenderName());
        System.out.println(myMessage.getColor().toString());
        System.out.println(myMessage.getText());


    }

    static void testRGB() {
        int r = 23, g = 42, b = 56;
        String hex = String.format("#%02X%02X%02X", r, g, b);
        System.out.println("Hex " + hex);

        int r1, g1, b1;
        r1 = Integer.valueOf(hex.substring(1, 3), 16);
        g1 = Integer.valueOf(hex.substring(3, 5), 16);
        b1 = Integer.valueOf(hex.substring(5, 7), 16);

        System.out.println(String.format("r %d, g %d, b %d", r1, g1, b1));
    }

    private static class MyHandler extends DefaultHandler {
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
