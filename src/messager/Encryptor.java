package messager;

public abstract class Encryptor {

    protected abstract byte[] encrypt(byte[] key, byte[] value);

    protected abstract byte[] decrypt(byte[] key, byte[] value);

    protected abstract byte[] generateKey();

}
