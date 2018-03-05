package messager;

import javax.xml.bind.DatatypeConverter;

public class EncryptedMessageConverter extends AbstractMessageConverter {

    private Encryptor encryption;
    private String algorithm;

    public EncryptedMessageConverter(String algorithm) throws Exception {
        EncryptionFactory factory = new EncryptionFactory();
        this.encryption = factory.getEncryptor(algorithm);
        this.algorithm = algorithm;
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
            String[] attributeValue = {algorithm, DatatypeConverter.printHexBinary(key)};

            String holdString = addTagWithAttribute("encrypted", attributeName, attributeValue,
                    DatatypeConverter.printHexBinary(encryptedBytes));

            return addTagWithAttribute("message", "sender", message.getSenderName(), holdString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setEncryption(String algorithm) {
        EncryptionFactory factory = new EncryptionFactory();
        this.encryption = factory.getEncryptor(algorithm);
        this.algorithm = algorithm;
    }

    public AbstractMessageConverter clone() {
        try {
            String newAlgorithm = this.algorithm;
            return new EncryptedMessageConverter(newAlgorithm);
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println("Could not clone messageConverter, you get default.");
            return new DefaultMessageConverter();
        }
    }

}
