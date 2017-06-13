package EncryptionAlgorithms;
import Key.*;
import com.company.Cipher;

public class DoubleCipher <T,K> implements CipherInterface< Couple<T,K> > {
    CipherInterface firstAlgorithm;
    CipherInterface secondAlgorithm;
    Key<Couple<T,K>> key;

    @Override
    public byte[] encrypt(byte[] data) {
        return secondAlgorithm.encrypt(firstAlgorithm.encrypt(data));
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return firstAlgorithm.decrypt(secondAlgorithm.decrypt(data));
    }

    public DoubleCipher(CipherInterface firstAlgorithm, CipherInterface secondAlgorithm) {
        this.firstAlgorithm = firstAlgorithm;
        this.secondAlgorithm = secondAlgorithm;
    }

    public Key<Couple<T, K>> getKey() {
        return key;
    }

    public void setKey(Key<Couple<T, K>> key) {
        //todo: add setKey to cipherInterface
        this.key = key;
    }
}
