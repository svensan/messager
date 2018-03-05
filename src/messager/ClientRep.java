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
    /*

    En klass som kan representera en klient. Den kan läsa XML och konvertera
    XML till medelande-klassen. Den kan också
    konvertera ett medelande till XML. Det är denna klass som innehåller en
    Comm, alltså har denna klass kontakt med
    input och output strömmen på socketen, all information som ska skickas
    från/till servern måste gå genom en instans
    av denna klass.
    
    TODO SVEN KOLLA ÖVER DE HÄR
    
    */

    private Encryptor encryption;
    private AbstractMessageConverter messageConverter;
    private String encryptionType = "none";
    private Comm connection;
    private SAXParserFactory saxFactory = SAXParserFactory.newInstance();
    private SAXParser parser;
    boolean isHost = false;
    boolean firstMessage = false;
    byte[] key;
    String connectionAdress;
    boolean acceptedConnection = false;
    private boolean haveSentConReq = false;

    public ClientRep(Socket connection, MessageReceiver messager)
            throws Exception {
        this.connection = new Comm(connection, this);
        this.connectionAdress = connection.getInetAddress().getHostAddress();
        this.connection.registerMessager(messager);
        this.parser = saxFactory.newSAXParser();
        this.messageConverter = new DefaultMessageConverter();
    }

    public String toString() {

        return connection.getIP();
    }

    public boolean getHaveSentConReq() {
        return haveSentConReq;
    }

    public void setHaveSentConReq(boolean b) {
        haveSentConReq = b;
    }

    public Socket getSocket() {

        return connection.getSocket();
    }

    public boolean firstMessage() {
        /*
        returnerar om en client rep har skickat ett medellande tidigare.
        relevant för server.
        */
        return firstMessage;
    }

    public void setFirstMessage(boolean b) {
        firstMessage = b;
    }

    public String getIP() {
        //System.out.println("ip fetched: "+connection.getIP());
        return connection.getIP();
    }

    protected byte[] getKey() {
        return key;
    }

    public void sendString(Message message) {
        /*
        Konverterar till en sträng o sätter ett medellande på strömmen
        */
        String outPutString = handleOutputMessage(message);
        connection.putStringOnStream(outPutString);
    }

    public void closeConnection() {
        /*
        SHUT IT DOWN
        */
        //System.out.println("got so far to lose it all");
        connection.close();
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean b) {
        isHost = b;
    }

    public void registerMessageConverter(AbstractMessageConverter messageConverter) {
        /*
        Registrerar hur vi ska konvertera medelanden från medelande-klassen 
        till strängar. I defaultfallet så gör vi
         detta genom att endast översätta till XML, men i fallet med kryptering 
        så måste också medelandet krypteras.
         */
        this.messageConverter = messageConverter;
    }

    private String handleOutputMessage(Message message) {
        /*
        Konverterar ett medelande från medelande-klassen till en sträng. 
        För att göra detta så använder vi en
        konverterare. En kan läsa mer om konverterare i den klassen 
        (messageConverter), men i stort så är det en klass
        som konverterar medelande från medelande-klassen till XML, 
        detta görs på olika sätt beroende av vilken
        kryptering som används.
        */
        return messageConverter.convertMessage(message);
    }

    public Message handleInputMessage(String messageString) throws Exception {

        /*
        Parsear ett inkommet string och gör om de till ett message helt enkelt.
        För att kunna göra detta så måste vi
        använda en parser, vi använder parsern SAXParser denna kan läsas mer om 
        i dennes dokumentation. För att använda
        en SAXParser så måste vi ha en Handler åt parsern, denna handler är en 
        inre klass och en kan läsa mer om den
        nedan. Parsern parsar heller inte strängar direkt, så vi måst konvertera
        från sträng till en InputStream.
        */

        MyParceHandler myHandler = new MyParceHandler();
        InputStream stream =
                new ByteArrayInputStream(
                        messageString.getBytes(StandardCharsets.UTF_8.name()));

        try {
            this.useParser(stream, myHandler); /*För att använda parsern krävs
            en handler och en inputström.*/
        } catch (Exception e) {
            System.out.println("OOOOps");
            return new Message(Color.RED, "ERROR",
                    "Sender: " + this.getIP() + " sent broken XML.");
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

    private void handleKeyRequest() {
        connection.putStringOnStream(
                "<message name=\"DefaultAnswer\" color=\"#000000\"><encryption>This program does not support" +
                        " asymmetric encryption.</encryption></message>"
        );
    }

    protected String getConnectionAdress() {
        return connectionAdress;
    }

    public boolean acceptedConnection() {
        return acceptedConnection;
    }

    public void acceptConnection() {
        acceptedConnection = true;
    }


    private class MyParceHandler extends DefaultHandler {

        /*
        Denna klass är en klass som vi måste ha för att kunna använda en
        SAXParser. Klassen har ett jobb, vilket är att
        läsa igenom en sträng och spara den information som vi måste ha för
        att skapa ett medelande. Sedan har denna klass
        också en metod som kan hämta medelandet som vi parsat fram.
         */

        private String messageSender;
        private Color color = Color.BLACK;
        private StringBuilder textBuilder = new StringBuilder();
        private String text;
        private boolean haveSetName = false;
        private boolean haveSetColor = false;
        private boolean pickUpText = false;
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
        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes) {
            /*
            Denna metod bestämmer vad vi ska göra när vi stöter på en starttag.
            Eftersom vi vill kunna hantera en mängd
            olika taggar på olika unika sätt så har vi valt att splitta upp 
            detta jobb i ett flertal mindre metoder.
             */

            this.handleStartTag(qName, attributes);

        }

        @Override
        public void endElement(String uri, String localName,
                               String qName) throws SAXException {
            /*
            Denna klass bestämmer vad som ska hända när vi stöter på en sluttag.
            Notera att vi inte gör något, detta kommer
            sig av att vi inte är intresserade av sluttaggar i denna chattapp.
             */

            if (pickUpText) {
                text = textBuilder.toString();
            }

        }

        @Override
        public void characters(
                char ch[], int start, int length) throws SAXException {

            /*
            Denna klass bestämmer vad som ska hända med text emellan start och 
            sluttag. Metoden fungerar egentligen på
            två olika sätt beroende på om vi använder kryptering eller inte. 
            Om kryptering används så dekrypteras medelandet
            och vi parsar igenom den krypterade informationen. Om medelandet
            inte är krypterat så sparar vi bara texten.
             */

            if (isEncrypted) {
                try {
                    /*
                    Då kryptering används så hämtar vi nyckeln och dekrypterar 
                    och parsar sedan igenom den krypterade
                    informationen.
                     */
                    isEncrypted = false;
                    String encryptedStringMessage = new String(ch, start, length);

                    byte[] encryptedBytes = DatatypeConverter.parseHexBinary(encryptedStringMessage);

                    Encryptor encryptor = encryptionFactory.getEncryptor(type);
                    byte[] decryptedBytes =
                            encryptor.decrypt(key, encryptedBytes);

                    SAXParser newParser = ClientRep.this.getParser();
                    InputStream stream =
                            new ByteArrayInputStream(decryptedBytes);

                    newParser.parse(stream, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                /*
                Om kryptering inte används så sparar vi endast texten.
                 */

                textBuilder.append(this.convertXMLsymbols(new String(ch, start, length)));
                pickUpText = true;
            }
        }

        public Message getMessage() {
            if (messageContainsConnectRequest) {
                Message ret = new Message(messageSender, text);
                ret.setConnectRequest();
                return ret;
            } else if (messageIsDisconnect) {
                return new Message(
                        color, messageSender, text, messageIsDisconnect);
            }
            if (messageContainsFileRequest) {
                return new Message(messageSender, text, fileRequest);
            } else if (messageContainsFileResponse) {
                return new Message(messageSender, text, fileResponse);
            } else return new Message(color, messageSender, text);
        }

        private void handleStartTag(String tagName, Attributes attributes) {
            /*
            Denna metod känner igen alla taggar som vi kan hantera, och kan
            skicka en tag till rätt hanterare. Till
            exempel om vi får en text-tagg så skickar vi det till text-taggs
            hanteraren.
             */
            if (tagName.equalsIgnoreCase("keyrequest")) {
                ClientRep.this.handleKeyRequest();
            }

            if (tagName.equalsIgnoreCase("message")) {
                this.handleMessageTag(attributes);
            }

            if (tagName.equalsIgnoreCase("text")) {
                this.handleTextTag(attributes);
            }

            if (tagName.equalsIgnoreCase("disconnect")) {
                System.out.print("disc tag detected");
                this.messageIsDisconnect = true;
                this.text = "I'm out";
                this.color = Color.RED;
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

            if (tagName.equalsIgnoreCase("request")) {
                this.handleRequestTag(attributes);
            }
        }

        private void handleTextTag(Attributes attributes) {
            /*
            Hanterar en texttagg genom att plocka det relevanta attributet och
            spara det. Eftersom att färgen skickas
            som en Hex-kod så måste vi först konvertera Hex till den vanliga
            Color klassen.
             */

            color = createColorFromHex(attributes.getValue("color"));
            haveSetColor = true;
        }

        private void handleEncryptedTag(Attributes attributes) {
            /*
            Hanterar encryption-taggar.
             */

            isEncrypted = true;
            type = attributes.getValue("type");
            key = DatatypeConverter.parseHexBinary(attributes.getValue("key"));
        }

        private void handleMessageTag(Attributes attributes) {
            /*
            Hanterar meddelande-taggar.
             */

            messageSender = attributes.getValue("sender");
            haveSetName = true;
        }

        private void handleFileRequestTag(Attributes attributes) {
            /*
            Hanterar filerequest-taggar genom att konvertera informationen
            från attributen till en instans av
            FileRequest-klassen. För mer information om FileRequest så läs
            under den klassen.
             */

            String fileName = attributes.getValue("name");
            int fileSize = Integer.valueOf(attributes.getValue("size"));
            String hexKey = attributes.getValue("key");
            String type = attributes.getValue("type");

            if (type != null) {
                fileRequest = new FileRequest(fileName, fileSize,
                        DatatypeConverter.parseHexBinary(hexKey), type);
            } else {
                fileRequest = new FileRequest(fileName, fileSize);
            }
            messageContainsFileRequest = true;
        }

        private void handleFileResponseTag(Attributes attributes) {
            /*
            Hanterar FileResponse-taggar gensom att spara informationen från
            attributen och ska en instans av
            FileResponse-klassen. För mer information om FileResponse-klassen
            läs under den klassen.
             */

            boolean acceptedFileRequest =
                    attributes.getValue("reply").equalsIgnoreCase("yes");

            int port = Integer.valueOf(attributes.getValue("port"));

            fileResponse = new FileResponse(acceptedFileRequest, port);
            messageContainsFileResponse = true;
        }

        private void handleRequestTag(Attributes attributes) {
            /*
            Hanterar request taggar.
             */

            messageContainsConnectRequest = true;
        }

        private Color createColorFromHex(String hexColor) {
            /*
            Konverterar en färg från hex-kod till en färg representerad av
            Javas Color-klass.
             */

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

        private String convertXMLsymbols(String text) {
            text = text.replaceAll("&gt;", ">");
            return text.replaceAll("&lt;", "<");
        }

    }

}
