package EncryptionAlgorithms;

import Key.Key;
import com.company.Cipher;

public class XORCipher implements CipherInterface<Integer> {
    Key<Integer> key;

    public XORCipher(Key<Integer> key) {
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
            result[i] = (byte) (data[i] ^ key.getKey());
        }
        return result;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return encrypt(data);
    }
}
