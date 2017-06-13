package Files;

import EncryptionAlgorithms.CipherInterface;

import java.io.File;

public class DirectoryEncryptorSequential {

    FileEncryptor fileEncryptor;

    public DirectoryEncryptorSequential() {
        this.fileEncryptor = new FileEncryptor();
    }

    public void encryptDirectory(File directory, CipherInterface cipher) {
        encryptOrDecryptDirectory(directory,cipher,true);
    }

    public void decryptDirectory(File directory, CipherInterface cipher) {
        encryptOrDecryptDirectory(directory,cipher,false);
    }

    private void encryptOrDecryptDirectory(File directory, CipherInterface cipher, boolean isEncrypt) {
        try {
            File[] allFiles = directory.listFiles();
            for (File file : allFiles) {
                if (file.isFile()) {
                    File newFile = fileEncryptor.returnDirectory(file,isEncrypt);
                    if (isEncrypt)
                        fileEncryptor.writeToFile(newFile,cipher.encrypt(fileEncryptor.readFromFile(file)));
                    else
                        fileEncryptor.writeToFile(newFile,cipher.decrypt(fileEncryptor.readFromFile(file)));
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
