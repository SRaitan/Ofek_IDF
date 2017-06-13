package File;
import EncryptionAlgorithms.*;
import com.company.Cipher;
import com.company.EncryptionListener;
import com.company.InvalidFileException;

import java.io.*;
import java.nio.file.Files;

public class FileEncryptor <T> {
    EncryptionListener listener;

    void setListener(EncryptionListener listener) {this.listener = listener;}

    void started (String fileName){
        if(listener != null)
            listener.onStarted(fileName);
    }
    void finished (String fileName){
        if(listener != null)
            listener.onFinished(fileName);
    }

    public boolean isValidFile(String filepath) {
        File file = new File(filepath);
        return (file.exists() && file.isFile() && file.canWrite() && file.canRead());
    }

    File returnFile(File original, boolean encrypt) throws InvalidFileException {
        if (original == null)
            throw new InvalidFileException("No return file found");
        String fname = original.getAbsolutePath();
        int pos = fname.lastIndexOf(".");
        if (pos > 0)
            fname = fname.substring(0, pos);
        if (encrypt)
            return new File(fname + "_encrypted.txt");
        return new File(fname + "_decrypted.txt");
    }

    public void writeObjectToFile(File file, Serializable toWrite) throws InvalidFileException {
        ObjectOutputStream objectOutputStream = null;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(toWrite);
        } catch (FileNotFoundException e) {
            throw new InvalidFileException("No write file found");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStreams(objectOutputStream, outputStream);
        }
    }

    void writeToFile(File file, byte[] bytes) throws InvalidFileException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
            finished(file.getName());
        } catch (FileNotFoundException e) {
            throw new InvalidFileException("No write file found");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStreams(outputStream);
        }
    }

    public Serializable readObjectFromFile(File file) throws InvalidFileException {
        InputStream inputStream = null;
        ObjectInputStream objectInputStream = null;
        Serializable toRead = null;
        try {
            inputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(inputStream);
            toRead = (Serializable) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            throw new InvalidFileException("No read file found");
        } catch (IOException e) {
            e.getMessage();
        } catch (ClassNotFoundException e) {
            e.getMessage();
        } finally {
            closeStreams(inputStream, objectInputStream);
        }
        return toRead;
    }

    void closeStreams(Closeable... closeables){
        for(Closeable closeable : closeables){
            if(closeable != null)
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    byte[] readFromFile(File readfile) throws IOException {
        started(readfile.getName());
        return Files.readAllBytes(readfile.toPath());
    }

    public void encryptOrDecryptFile(File original, Cipher cipher, T key, boolean encryptFile) {
        //started();
        //OutputStream outputStream = null;
        try {
            File newFile;
            if (cipher instanceof ReverseCipher)
                newFile = returnFile(original, false);
            else
                newFile = returnFile(original, encryptFile);
            //todo: reac func
            byte[] fileBytes = readFromFile(original);
            //todo: write func
            //outputStream = new FileOutputStream(newFile);
            //inputStream = new FileInputStream(original);
            if (encryptFile)
                writeToFile(newFile,cipher.encryptBytes(fileBytes, key));
                //outputStream.write(cipher.encryptBytes(fileBytes, key));
                //cipher.encryptOrDecrypt(outputStream, inputStream, key, true);
            else
                writeToFile(newFile,cipher.decryptBytes(fileBytes, key));
             //outputStream.write(cipher.decryptBytes(fileBytes, key));
            //cipher.encryptOrDecrypt(outputStream, inputStream, key, false);
            //finished();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //closeStreams(outputStream);
        }
    }

    public File createKeyFile(File file){
        int lastDot = file.getPath().lastIndexOf('.');
        String newPath;
        newPath = file.getPath().substring(0, lastDot)+ ".key.bin";
        return new File(newPath);
    }

    public File createKeyFileforDirectory(File file){
        String newPath = file.getPath() + ".key.bin";
        return new File(newPath);
    }

    public String getKeyPath(String path) {
        int indexOfName = path.lastIndexOf("_encrypted");
        String newPath = path.substring(0,indexOfName) + ".key.bin";
        return newPath;
    }

    public String getKeyFileforDirectory(String path){
        int indexDirectoryName = path.lastIndexOf("\\");
        path = path.substring(0,indexDirectoryName);
        String newPath = path + ".key.bin";
        return newPath;
    }

    File returnDirectory(File file, boolean isEncrypt) throws InvalidFileException {
        if (file == null)
            throw new InvalidFileException("No return file found");
        String fname = file.getAbsolutePath();
        String fileNameOnly = file.getName();
        int lastDirectory = fname.lastIndexOf("\\");
        fname = fname.substring(0, lastDirectory);
        if(isEncrypt)
            fname = fname + "\\encrypt\\";
        else {
            int secondToLastDirectory = fname.lastIndexOf("\\");
            fname = fname.substring(0, secondToLastDirectory);
            fname = fname + "\\decrypt\\";
        }
        new File(fname).mkdir();
        String path = fname + fileNameOnly;
        return returnFile(new File(path),isEncrypt);
    }
}
