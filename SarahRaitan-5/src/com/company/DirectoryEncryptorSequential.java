package com.company;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class DirectoryEncryptorSequential<T> {

    FileEncryptor fileEncryptor;

    public DirectoryEncryptorSequential() {
        this.fileEncryptor = new FileEncryptor();
    }

    void encryptDirectory(File directory, Cipher cipher, T key) {
        encryptOrDecryptDirectory(directory,cipher,key,true);
    }

    void decryptDirectory(File directory, Cipher cipher, T key) {
        encryptOrDecryptDirectory(directory,cipher,key,false);
    }

    private void encryptOrDecryptDirectory(File directory, Cipher cipher, T key, boolean isEncrypt) {
        try {
            File[] allFiles = directory.listFiles();
            for (File file : allFiles) {
                if (file.isFile()) {
                    File newFile = fileEncryptor.returnDirectory(file,isEncrypt);
                    if (isEncrypt)
                        fileEncryptor.writeToFile(newFile,cipher.encryptBytes(fileEncryptor.readFromFile(file),key));
                    else
                        fileEncryptor.writeToFile(newFile,cipher.decryptBytes(fileEncryptor.readFromFile(file),key));
                }
            }
        } catch (Exception ex) {}
    }

/*    File returnFile(String fname, boolean encrypt) throws InvalidFileException {
        int pos = fname.lastIndexOf(".");
        if (pos > 0)
            fname = fname.substring(0, pos);
        if (encrypt)
            return new File(fname + "_encrypted.txt");
        return new File(fname + "_decrypted.txt");
    }*/
}
