package messager;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class CaesarEncryptor extends Encryptor {
/*
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int length = Integer.parseInt(in.nextLine());
        String str = in.nextLine();
        int k = Integer.parseInt(in.nextLine());

        k = k % 26;

        System.out.println(encrypt(str, length, k));

        in.close();
    }

    private static String encrypt(String str, int length, int shift) {
        StringBuilder strBuilder = new StringBuilder();
        char c;
        for (int i = 0; i < length; i++) {
            c = str.charAt(i);
            // if c is letter ONLY then shift them, else directly add it
            if (Character.isLetter(c)) {
                c = (char) (str.charAt(i) + shift);
                // System.out.println(c);

                // checking case or range check is important, just if (c > 'z'
                // || c > 'Z')
                // will not work
                if ((Character.isLowerCase(str.charAt(i)) && c > 'z')
                        || (Character.isUpperCase(str.charAt(i)) && c > 'Z'))

                    c = (char) (str.charAt(i) - (26 - shift));
            }
            strBuilder.append(c);
        }
        return strBuilder.toString();
    }
*/

    protected byte[] encrypt(byte[] key, byte[] value) {
        return null;
    }

    protected byte[] decrypt(byte[] key, byte[] value) {
        return null;
    }

    protected byte[] generateKey() {
        return null;
    }

}
