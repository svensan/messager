package messager;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ClientRep {

    private Encryptor encryption;
    private MessageConverter messageConverter;
    private String encryptionType = "none";
    private Comm connection;
    private SAXParserFactory saxFactory = SAXParserFactory.newInstance();
    private SAXParser parser;
    boolean isHost = false;
    byte[] key;
    String connectionAdress;
    boolean acceptedConnection = false;

    public ClientRep(Socket connection, MessageReceiver messager) throws Exception {
        this.connection = new Comm(connection, this);
        this.connectionAdress = connection.getInetAddress().getHostAddress();
        this.connection.registerMessager(messager);
        this.parser = saxFactory.newSAXParser();
        this.messageConverter = new DefaultMessageConverter();
    }

    public String toString() {

        return connection.getIP();
    }

    public Socket getSocket() {

        return connection.getSocket();
    }

    public String getIP() {
        //System.out.println("ip fetched: "+connection.getIP());
        return connection.getIP();
    }

    protected byte[] getKey() {
        return key;
    }

    public void sendString(Message message) {
        String outPutString = handleOutputMessage(message);
        connection.putStringOnStream(outPutString);
    }

    public void closeConnection() {

        System.out.println("got so far to lose it all");
        connection.close();
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean b) {
        isHost = b;
    }

    public void registerMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    private String handleOutputMessage(Message message) {
        return messageConverter.convertMessage(message);
    }

    public Message handleInputMessage(String messageString) throws Exception {
        MyParceHandler myHandler = new MyParceHandler();
        InputStream stream = new ByteArrayInputStream(messageString.getBytes(StandardCharsets.UTF_8.name()));

        try {
            this.useParser(stream, myHandler);
        } catch (Exception e) {
            System.out.println("OOOOps");
        }

        return myHandler.getMessage();
    }

    private void useParser(InputStream stream, DefaultHandler handler) {
        try {
            parser.parse(stream, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SAXParser getParser() {
        try {
            return saxFactory.newSAXParser();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected String getConnectionAdress() {
        return connectionAdress;
    }

    public boolean acceptedConnection() {
        return acceptedConnection;
    }

    private class MyParceHandler extends DefaultHandler {

        private String messageSender;
        private Color color = Color.BLACK;
        private String text;
        private boolean haveSetName = false;
        private boolean haveSetColor = false;
        private boolean isEncrypted = false;
        private String type;
        private boolean messageContainsFileRequest = false;
        private FileRequest fileRequest;
        private boolean messageContainsFileResponse = false;
        private FileResponse fileResponse;
        private boolean messageContainsConnectRequest = false;
        private EncryptionFactory encryptionFactory = new EncryptionFactory();

        private boolean messageIsDisconnect = false;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {

            this.handleStartTag(qName, attributes);

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            
            
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            if (isEncrypted) {
                try {
                    isEncrypted = false;
                    String encryptedMessageHex = new String(ch, start, length);

                    byte[] encryptedBytes = DatatypeConverter.parseHexBinary(encryptedMessageHex);

                    Encryptor encryptor = encryptionFactory.getEncryptor(type);
                    byte[] decryptedBytes = encryptor.decrypt(key, encryptedBytes);

                    SAXParser newParser = ClientRep.this.getParser();
                    InputStream stream = new ByteArrayInputStream(decryptedBytes);

                    newParser.parse(stream, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                text = new String(ch, start, length);
            }
        }

        public Message getMessage() {
            if (messageContainsConnectRequest) {
                Message ret = new Message(messageSender, text);
                ret.setConnectRequest();
                return ret;
            } else if (messageIsDisconnect) {
                return new Message(color, messageSender, text, messageIsDisconnect);
            }
            if (messageContainsFileRequest) {
                return new Message(messageSender, text, fileRequest);
            } else if (messageContainsFileResponse) {
                return new Message(messageSender, text, fileResponse);
            } else return new Message(color, messageSender, text);
        }

        private void handleStartTag(String tagName, Attributes attributes) {

            if (tagName.equalsIgnoreCase("message")) {
                this.handleMessageTag(attributes);
            }

            if (tagName.equalsIgnoreCase("text")) {
                this.handleTextTag(attributes);
            }

            if (tagName.equalsIgnoreCase("disconnect")) {
                System.out.print("disc tag detected");
                this.messageIsDisconnect = true;
            }

            if (tagName.equalsIgnoreCase("encrypted")) {
                this.handleEncryptedTag(attributes);
            }

            if (tagName.equalsIgnoreCase("filerequest")) {
                this.handleFileRequestTag(attributes);
            }

            if (tagName.equalsIgnoreCase("fileresponse")) {
                this.handleFileResponseTag(attributes);
            }
        }

        private void handleTextTag(Attributes attributes) {
            color = createColorFromHex(attributes.getValue("color"));
            haveSetColor = true;
        }

        private void handleEncryptedTag(Attributes attributes) {
            isEncrypted = true;
            type = attributes.getValue("type");
            key = DatatypeConverter.parseHexBinary(attributes.getValue("key"));
        }

        private void handleMessageTag(Attributes attributes) {
            messageSender = attributes.getValue("sender");
            haveSetName = true;
        }

        private void handleFileRequestTag(Attributes attributes) {
            String fileName = attributes.getValue("name");
            int fileSize = Integer.valueOf(attributes.getValue("size"));

            fileRequest = new FileRequest(fileName, fileSize);
            messageContainsFileRequest = true;
        }

        private void handleFileResponseTag(Attributes attributes) {
            boolean acceptedFileRequest = attributes.getValue("reply").equalsIgnoreCase("yes");

            int port = Integer.valueOf(attributes.getValue("port"));

            fileResponse = new FileResponse(acceptedFileRequest, port);
            messageContainsFileResponse = true;
        }

        private void handeRequestTag(Attributes attributes) {
            messageContainsConnectRequest = true;
        }

        private Color createColorFromHex(String hexColor) {
            int r, g, b;
            r = Integer.valueOf(hexColor.substring(1, 3), 16);
            g = Integer.valueOf(hexColor.substring(3, 5), 16);
            b = Integer.valueOf(hexColor.substring(5, 7), 16);

            return new Color(r, g, b);
        }

        public String toText(String hex) {
            try {
                byte[] bytes = DatatypeConverter.parseHexBinary(hex);
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    
        public void acceptConnection(){
            acceptedConnection = true;
        }
       
    }
    
}
