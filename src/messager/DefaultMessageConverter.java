package messager;

public class DefaultMessageConverter extends AbstractMessageConverter implements MessageConverter {
    public String convertMessage(Message message) {
        String holdString = getXMLFromMessage(message);
        return addTagWithAttribute("message", "sender", message.getSenderName(), holdString);
    }
}
