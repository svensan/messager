package messager;
/*
TODO sven kommentera detta
*/
public class DefaultMessageConverter extends AbstractMessageConverter implements MessageConverter {

    /*
    Detta är en messageconverter som är default, alltså använder ingen kryptering. Allt den gör är att sätta taggar
    med attribut. Se AbstractMessageConverter för hur metoderna denna klass använder fungerar.
     */

    public String convertMessage(Message message) {
        String holdString = getXMLFromMessage(message);
        return addTagWithAttribute("message", "sender", message.getSenderName(), holdString);
    }
}
