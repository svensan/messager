package messager;

import javax.xml.bind.DatatypeConverter;
import java.awt.*;

public abstract class AbstractMessageConverter implements Cloneable {
    /*
    Abstrakt klass för att konvertera message klassen till en sträng som kan
    skickas över streamen. Implementeras i ClientRep-klassen.
    
    TODO kommentera DETTA SVEN !!!
    */

    public abstract String convertMessage(Message message);

    public abstract AbstractMessageConverter clone();

    private String getHexColor(Color myColor) {
        /*
        Konverterar en färg från Color-klassen till Hex-kod, detta för att den ska kunna representeras som sträng.
         */

        int r = myColor.getRed();
        int g = myColor.getGreen();
        int b = myColor.getBlue();
        return String.format("#%02X%02X%02X", r, g, b);
    }

    protected String addTagWithAttribute(String tagName, String[] attributeName, String[] attributeValue, String text) {
        /*
        Skapar XML-kod om en vet tagnamn, attributnamn, attributvärden och vilken text som ska stå innuti taggen.
         */

        if (!(attributeName.length == attributeValue.length)) {
            /*
            Ett test så att vi har lika många attributnamn som attributvärden, om så inte är fallet så kan ju självklart
            inte XML-kod skapas.
             */

            throw new IllegalArgumentException();
        }

        StringBuilder attributeStringBuilder = new StringBuilder();
        for (int i = 0; i < attributeName.length; i++) {
            String holdString = String.format(" %1$2s=\"%2$2s\"", attributeName[i], attributeValue[i]);
            attributeStringBuilder.append(holdString);
        }
        String attributeString = attributeStringBuilder.toString();

        return String.format("<%1$2s%2$2s>%3$2s</%1$2s>", tagName, attributeString, text);
    }

    protected String addTagWithAttribute(String tagName, String attributeName, String attributeValue, String text) {

        /*
        En till klass som skapar XML-kod, denna funkar dock endast om vi bara har ETT attribut.
         */

        return String.format("<%1$2s %2$2s=\"%3$2s\">%4$2s</%1$2s>", tagName, attributeName, attributeValue, text);
    }

    protected String addTagWithAttribute(String tagName, String text) {

        /*
        En till metod som skapar XML-kod, denna kräver dock inga attribut alls.
         */

        return String.format("<%1$2s>%2$s</%1$2s>", tagName, text);
    }

    protected String getXMLFromMessage(Message message) {

        /*
        Denna metod är en sammansättning av de tidigare metoderna. Den känner igen de typer av medelanden som vi ska kunna
        konvertera till XML, och sedan skapar den fungerande XML av dem.
         */

        if (message.isFileRequest()) {
            FileRequest req = message.getFileRequest();

            String[] attributeName;
            String[] attributeValue;

            if (req.isUsingEncryption()) {
                attributeName = new String[]{"name", "size", "key", "type"};
                attributeValue = new String[]{req.getFileName(), String.valueOf(req.getFileSize()),
                        DatatypeConverter.printHexBinary(req.getKey()), req.getType()};
            } else {
                attributeName = new String[]{"name", "size"};
                attributeValue = new String[]{req.getFileName(), String.valueOf(req.getFileSize())};
            }

            return addTagWithAttribute("filerequest", attributeName,
                    attributeValue, message.getText());
        } else if (message.isFileResponse()) {
            FileResponse resp = message.getFileResponse();
            String reply;

            String[] attributeName = {"reply", "port"};

            if (resp.acceptedFileRequest()) {
                reply = "yes";
            } else reply = "no";

            String[] attributeValue = {reply, String.valueOf(resp.getPort())};

            return addTagWithAttribute("fileresponse", attributeName,
                    attributeValue, message.getText());
        } else if (message.isConnectRequest()) {
            return addTagWithAttribute("request", message.getText());
        } else if (message.isDisconnectMessage()) {
            return "<disconnect/>";
        } else {
            String[] attributeName = {"color"};
            String[] attibuteValue = {this.getHexColor(message.getColor())};

            String retText = convertXMLsymbols(message.getText());

            String ret = addTagWithAttribute("text", attributeName, attibuteValue, retText);
            return ret;
        }
    }

    private String convertXMLsymbols(String text) {
        text = text.replaceAll(">", "&gt;");
        return text.replaceAll("<", "&lt;");
    }
}
