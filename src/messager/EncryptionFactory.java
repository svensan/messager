package messager;

public class EncryptionFactory {
    public Encryptor getEncryptor(String type) {

        if (type.equalsIgnoreCase("AES")) {
            return new AESEncryptor();
        } else if (type.equalsIgnoreCase("caesar")) {
            return new CaesarEncryptor();
        }

        throw new IllegalArgumentException("Program does not support encyptiontype: "+type+".");
    }
}
