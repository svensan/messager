package messager;

import java.util.Random;

public class CaesarEncryptor extends Encryptor {

    protected byte[] encrypt(byte[] key, byte[] value) {

        int counter = 0;
        byte[] returnValue = new byte[value.length];

        for (byte currentByte : value) {
            if (97 <= currentByte && currentByte <= 122) {

                byte holdByte = (byte) (97 + (currentByte - 97 + key[0]) % 26);
                returnValue[counter] = holdByte;

            } else if (65 <= currentByte && currentByte <= 90) {

                byte holdByte = (byte) (65 + (currentByte - 65 + key[0]) % 26);
                returnValue[counter] = holdByte;

            } else {
                returnValue[counter] = currentByte;
            }

            counter++;
        }

        return returnValue;
    }

    protected byte[] decrypt(byte[] key, byte[] value) {

        int counter = 0;
        byte[] returnValue = new byte[value.length];

        for (byte currentByte : value) {
            if (97 <= currentByte && currentByte <= 122) {

                byte holdByte = (byte) (97 + (currentByte - 97 - key[0] + 26) % 26);
                returnValue[counter] = holdByte;

            } else if (65 <= currentByte && currentByte <= 90) {

                byte holdByte = (byte) (65 + (currentByte - 65 - key[0] + 26) % 26);
                returnValue[counter] = holdByte;

            } else {
                returnValue[counter] = currentByte;
            }

            counter++;
        }

        return returnValue;
    }

    protected byte[] generateKey() {
        Random random = new Random();
        return new byte[]{(byte) (1+random.nextInt(25))};
    }

}
