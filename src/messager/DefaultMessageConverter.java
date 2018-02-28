package messager;
/*
TODO sven kommentera detta
*/
public class DefaultMessageConverter extends AbstractMessageConverter implements MessageConverter {

    /*
    Denna klass är en MessageConverter. Detta är den enklaste typen av message converter, allt den gör är att omvandla
    medelanden från Medelande-klassen till XML-kod representerat som en sträng. För mer information om hur detta går
    till, se kommentarerna hos AbstractMessageConverter.
     */

    public String convertMessage(Message message) {
        String holdString = getXMLFromMessage(message);
        return addTagWithAttribute("message", "sender", message.getSenderName(), holdString);
    }
}
