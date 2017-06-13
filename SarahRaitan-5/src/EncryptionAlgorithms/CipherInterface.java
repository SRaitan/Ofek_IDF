package EncryptionAlgorithms;
/**
 * Created by hackeru on 3/21/2017.
 */
public interface CipherInterface<T> {

    byte[] encrypt(byte[] bytes);
    byte[] decrypt(byte[] bytes);
}
