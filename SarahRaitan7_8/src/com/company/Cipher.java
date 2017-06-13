package com.company;

import EncryptionAlgorithms.CipherListener;

public abstract class Cipher<T>  {
    CipherListener listener;

    void setListener(CipherListener listener) {this.listener = listener;}

    void started (){
        if(listener != null)
            listener.onStarted();
    }

    void finished (){
        if(listener != null)
            listener.onFinished();
    }

    public byte[] encryptBytes(byte[] data, T key) throws InvalidFileException {
        return encryptOrDecryptBytes(data,key,true);
    }
    public byte[] decryptBytes(byte[] data, T key) throws InvalidFileException {
        return encryptOrDecryptBytes(data,key,false);
    }

    public byte[] encryptOrDecryptBytes(byte[] data, T key, boolean encryptFile) throws InvalidFileException {
        //started();
        byte[] result = new byte [data.length];
        try {
            if(encryptFile) {
                for (int i = 0; i < data.length; i++) {
                    result[i] = (byte) encrypt(data[i],key);
                }
            }
            else{
                for (int i = 0; i < data.length; i++) {
                    result[i] = (byte) decrypt(data[i],key);
                }
            }
            //finished();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    abstract public int encrypt(int oneByte, T key);
    abstract public int decrypt(int oneByte, T key);
}
