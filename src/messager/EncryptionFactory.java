package messager;

public class EncryptionFactory {

    /*
    Denna klass kan skapa krypterare. Detta är inte in strikt nödvändig klass men det visar sig att denna klass kan
    förenkla en del saker, eftersom att vi inte måste välja kryptering "en gång för alla" om vi istället kan skapa en
    instans av denna klass för att skapa krypterare. För mer information om krypterare, se AESEncryptor och
    CaesarEncryptor.
     */

    public Encryptor getEncryptor(String type) {

        if (type.equalsIgnoreCase("AES")) {
            return new AESEncryptor();
        } else if (type.equalsIgnoreCase("caesar")) {
            return new CaesarEncryptor();
        }

        throw new IllegalArgumentException("Program does not support encyptiontype: "+type+".");
    }
}
