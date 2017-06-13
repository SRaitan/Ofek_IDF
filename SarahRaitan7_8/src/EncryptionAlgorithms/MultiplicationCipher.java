package EncryptionAlgorithms;

import Key.Key;
import com.company.Cipher;

public class MultiplicationCipher implements CipherInterface<Integer> {
    int decKey;
    Key<Integer> key;

    public MultiplicationCipher(Key<Integer> key) {
        this.key = key;
    }

    public Key<Integer> getKey() {
        return key;
    }

    public void setKey(Key<Integer> key) {
        this.key = key;
    }

    private int getDecKey(int key) {
        int decKey = 0;
        for (int i = 0; i < 256; i++) {
            if(((i * key) & 0x000000FF) == 1) {
                decKey = i;
                break;
            }
        }
        return decKey;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        int valueKey = key.getKey();
        byte [] encryptBytes = new byte[data.length];
        for (int i = 0; i < encryptBytes.length; i++) {
            encryptBytes[i] = (byte) (data[i]*valueKey);
        }
        return encryptBytes;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        byte [] decryptBytes = new byte[data.length];
        if(decKey == 0)
            decKey = getDecKey(key.getKey());
        for (int i = 0; i < decryptBytes.length; i++) {
            decryptBytes[i] = (byte) (data[i] * decKey);
        }
        return decryptBytes;
    }

}
