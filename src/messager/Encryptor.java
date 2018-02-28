package messager;

public abstract class Encryptor {

    /*
    Denna klass är straightforward: Det är en abstrakt krypterare. Den kan göra tre saker, kryptera, dekryptera och
    generera nycklar. För att läsa mer om hur den implementeras se AESEncryptor och CaesarEncryptor.
     */

    protected abstract byte[] encrypt(byte[] key, byte[] value);

    protected abstract byte[] decrypt(byte[] key, byte[] value);

    protected abstract byte[] generateKey();

}
