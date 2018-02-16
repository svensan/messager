package messager;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Encryptor {

    public String encrypt(String type, String key, String initVector, String value) {

        if (type.equalsIgnoreCase("AES")) {
            return encryptAES(key, initVector, value);
        }

        if (type.equalsIgnoreCase("Caesar")) {
            return encryptCaesar(key, initVector, value);
        }

        throw new IllegalArgumentException();
    }

    public String decrypt(String type, String key, String initVector, String encrypted) {

        if (type.equalsIgnoreCase("AES")) {
            return decryptAES(key, initVector, encrypted);
        }

        if (type.equalsIgnoreCase("Caesar")) {
            return decryptCaesar(key, initVector, encrypted);
        }

        throw new IllegalArgumentException();
    }

    private byte[] encryptAES(String key, String initVector, byte[] value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            return cipher.doFinal(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String encryptAES(String key, String initVector, String value) {
        byte[] encryptedBytes = encryptAES(key, initVector, value.getBytes());
        return DatatypeConverter.printBase64Binary(encryptedBytes);
    }

    private byte[] decryptAES(String key, String initVector, byte[] encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            return cipher.doFinal(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private String decryptAES(String key, String initVector, String encrypted) {
        byte[] decryptedBytes = decryptAES(key, initVector, DatatypeConverter.parseBase64Binary(encrypted));
        return new String(decryptedBytes);
    }

    private String encryptCaesar(String key, String initVector, String value) {
        return null;
    }

    private String decryptCaesar(String key, String initVector, String encrypted) {
        return null;
    }
}
