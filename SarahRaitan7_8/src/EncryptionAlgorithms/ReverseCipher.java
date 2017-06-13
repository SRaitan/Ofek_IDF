package EncryptionAlgorithms;

import Key.Key;
import com.company.Cipher;

public class ReverseCipher<T> implements CipherInterface<T> {
    private CipherInterface algToReverse;

/*
    public void setKey(Key<T> key) {
       algToReverse.setKey(key);
    }
*/

    public ReverseCipher(CipherInterface cipher) {
        algToReverse = cipher;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return algToReverse.decrypt(data);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return algToReverse.encrypt(data);
    }
}

