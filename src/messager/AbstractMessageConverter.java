package messager;

import java.awt.*;

public abstract class AbstractMessageConverter implements MessageConverter{
    /*
    Abstrakt klass för att konvertera message klassen till en sträng som kan
    skickas över streamen. Implementeras i ClientRep-klassen.
    
    TODO kommentera DETTA SVEN !!!
    */

    public abstract String convertMessage(Message message);

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

            String[] attributeName = {"name", "size"};
            String[] attributeValue = {req.getFileName(), String.valueOf(req.getFileSize())};

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
        } else {
            String[] attributeName = {"color"};
            String[] attibuteValue = {this.getHexColor(message.getColor())};
            String ret = addTagWithAttribute("text", attributeName, attibuteValue, message.getText());
            if(message.isDisconnectMessage()){
                ret = ret+"<disconnect/>";
            }
            return ret;
        }
    }
}
