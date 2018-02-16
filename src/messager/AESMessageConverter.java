package messager;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

public class AESMessageConverter extends AbstractMessageConverter implements MessageConverter {

    private Encryptor encryption;
    private String key;
    private static final String ALGORITHM = "AES";
    private SecureRandom random;

    public AESMessageConverter() throws Exception {
        this.encryption = new Encryptor();
        this.random = new SecureRandom();
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

        byte[] byteKey = new byte[128];
        System.out.println(byteKey.length);
        random.nextBytes(byteKey);

        try {
            String key = new String(byteKey, "UTF-8");
            System.out.println(key.getBytes("UTF-8").length);

            String encryptedString = encryption.encrypt(ALGORITHM, key,
                    "RandomInitVector", XMLMessage);

            String[] attributeName = {"type", "key"};
            String[] attributeValue = {ALGORITHM, toHexadecimal(key)};

            String holdString = addTagWithAttribute("encrypted", attributeName, attributeValue,
                    toHexadecimal(encryptedString));

            return addTagWithAttribute("message", "sender", message.getSenderName(), holdString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

