package messager;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Encryptor {

    /*
    Denna klass är straightforward: Det är en abstrakt krypterare. Den kan göra tre saker, kryptera, dekryptera och
    generera nycklar. För att läsa mer om hur den implementeras se AESEncryptor och CaesarEncryptor.
     */

    protected abstract byte[] encrypt(byte[] key, byte[] value);

    protected abstract byte[] decrypt(byte[] key, byte[] value);

    protected abstract InputStream getDecryptingInputStream(InputStream is, byte[] key);

    protected abstract OutputStream getEncryptingOutputStream(OutputStream os, byte[] key);

    protected abstract byte[] generateKey();

}
