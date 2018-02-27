package messager;
/*
TODO sven kommentera detta
*/
public class DefaultMessageConverter extends AbstractMessageConverter implements MessageConverter {
    public String convertMessage(Message message) {
        String holdString = getXMLFromMessage(message);
        return addTagWithAttribute("message", "sender", message.getSenderName(), holdString);
    }
}
