package messager;

import java.awt.*;

public abstract class AbstractMessageConverter implements MessageConverter{

    public abstract String convertMessage(Message message);

    private String getHexColor(Color myColor) {
        int r = myColor.getRed();
        int g = myColor.getGreen();
        int b = myColor.getBlue();
        return String.format("#%02X%02X%02X", r, g, b);
    }

    protected String addTagWithAttribute(String tagName, String[] attributeName, String[] attributeValue, String text) {

        if (!(attributeName.length == attributeValue.length)) {
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
        return String.format("<%1$2s %2$2s=\"%3$2s\">%4$2s</%1$2s>", tagName, attributeName, attributeValue, text);
    }

    protected String getXMLFromMessage(Message message) {

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
        } else {
            String[] attributeName = {"color"};
            String[] attibuteValue = {this.getHexColor(message.getColor())};

            return addTagWithAttribute("text", attributeName, attibuteValue, message.getText());
        }
    }
}
