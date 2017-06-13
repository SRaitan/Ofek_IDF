package EncryptionAlgorithms;

import Key.Key;
import com.company.Cipher;

/**
 * Created by hackeru on 3/19/2017.
 */
public class CaesarCipher implements CipherInterface<Integer> {
    Key<Integer> key;

    public CaesarCipher(Key<Integer> key) {
        this.key = key;
    }

    public Key<Integer> getKey() {
        return key;
    }

    public void setKey(Key<Integer> key) {
        this.key = key;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] + key.getKey());
        }
        return result;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] - key.getKey());
        }
        return result;
    }


}
