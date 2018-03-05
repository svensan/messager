package messager;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

public class AESEncryptor extends Encryptor {
    /*
    Den här klassen tar hand om kryptering. Denna klass ska kunna göra de tre fundamentala sakerna som kryptering kräver,
    den kan kryptera, dekryptera och generera nycklar.
    
    TODO KOMMENTERA DETTA SVEN!!!
    */

    protected byte[] encrypt(byte[] key, byte[] value) {
        /*
        Denna metod krypterar enligt AES-algoritmen. Den använder metoder/klasser från javax.crypto, så om en vill ha mer
        förståelse en att denna metod krypterar så rekomenderas att läsa dokumentationen av dessa klasser.
         */

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            return cipher.doFinal(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected byte[] decrypt(byte[] key, byte[] value) {
        /*
        Denna metod dekrypterar ett medelande enligt AES-algoritmen. På samma sätt som för encrypt, så om en vill ha
        mer specifik information om hur detta fungerar läs javax.crypto dokumentationen.
         */

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            return cipher.doFinal(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    protected CipherInputStream getDecryptingInputStream(InputStream is, byte[] key) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            return new CipherInputStream(is, cipher);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected CipherOutputStream getEncryptingOutputStream(OutputStream os, byte[] key) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            return new CipherOutputStream(os, cipher);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    protected byte[] generateKey() {
        /*
        Denna metod genererar en ny säker nyckel. Vi använder javas egna SecureRandom för att generera slump till nyckeln.
         */

        try {
            SecureRandom random = new SecureRandom();
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(random);
            return keyGenerator.generateKey().getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

