package messager;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class AESEncryptor extends Encryptor {

    protected byte[] encrypt(byte[] key, byte[] value) {
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

    protected byte[] generateKey() {
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

