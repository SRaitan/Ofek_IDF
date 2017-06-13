package File;

import com.company.Cipher;
import com.company.EncryptionListener;

import java.io.File;

public class DirectoryEncryptorSequential<T> {

    FileEncryptor fileEncryptor;
    EncryptionListener listener;
    void setListener(EncryptionListener listener) {this.listener = listener;}
    void started (){
        if(listener != null)
            listener.onStarted();
    }
    void finished (){
        if(listener != null)
            listener.onFinished();
    }

    public DirectoryEncryptorSequential() {
        this.fileEncryptor = new FileEncryptor();
    }

    public void encryptDirectory(File directory, Cipher cipher, T key) {
        encryptOrDecryptDirectory(directory,cipher,key,true);
    }

    public void decryptDirectory(File directory, Cipher cipher, T key) {
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
