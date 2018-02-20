package messager;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

public class AESMessageConverter extends AbstractMessageConverter implements MessageConverter {

    private Encryptor encryption;
    private static final String ALGORITHM = "AES";

    public AESMessageConverter() throws Exception {
        EncryptionFactory factory = new EncryptionFactory();
        this.encryption = factory.getEncryptor(ALGORITHM);
    }

    public static String toHexadecimal(String text) {
        try {
            byte[] myBytes = text.getBytes("UTF-8");
            return DatatypeConverter.printHexBinary(myBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convertMessage(Message message) {
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

