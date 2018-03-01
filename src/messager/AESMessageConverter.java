package messager;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
/*
TODO KOMMENTERA DETTA SVEN !!

verkar bygga på message konverter abstrakta klassen så vi använder den för att
ta messages till strängar som kan skickas över konnection ???
*/
public class AESMessageConverter extends AbstractMessageConverter implements MessageConverter {

    /*
    Denna klass är en MessageConverter, och används då för att konvertera medelanden från Medelande-klassen till XML-kod
    representerat med en sträng. För mer information om de allmänna metoden hos MessageConverter se MessageConverter-klassen.
    Det som är speciellt för denna klass är att den krypterar XML-koden med AES-kryptering.
     */

    private Encryptor encryption;
    private static final String ALGORITHM = "AES";

    public AESMessageConverter() throws Exception {
        EncryptionFactory factory = new EncryptionFactory();
        this.encryption = factory.getEncryptor(ALGORITHM);
    }

    public String convertMessage(Message message) {
        /*
        Det är denna metod som krypterar medelandet. Den konverterar först medelandet till XML-kod i vanlig mening. Sedan
        så krypteras denna XML-kod med AES-kryptering. Efter kryptering så sätts encryption-taggar på med attribut "key"
        och attribut "type". Key genereras av Encryptor-klassen (se denna klass för mer information) och type är
        krypteringsalgoritmen, alltså AES. För att undvika tecken som är svåra att hantera så omvandlas de krypterade
        medelandet och nyckeln till Hex-kod.
         */

        String XMLMessage = getXMLFromMessage(message);

        try {
            byte[] key = encryption.generateKey();
            byte[] encryptedBytes = encryption.encrypt(key, XMLMessage.getBytes("UTF-8"));

            String[] attributeName = {"type", "key"};
            String[] attributeValue = {ALGORITHM, DatatypeConverter.printHexBinary(key)};

            String holdString = addTagWithAttribute("encrypted", attributeName, attributeValue,
                    DatatypeConverter.printHexBinary(encryptedBytes));

            return addTagWithAttribute("message", "sender", message.getSenderName(), holdString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

