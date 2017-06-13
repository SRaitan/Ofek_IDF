package com.company;

import EncryptionAlgorithms.*;
import Key.*;
import InputOutput.*;
import java.io.File;
import java.io.Serializable;
import java.security.KeyException;

public class Menu {

    private static final String EXIT = "0";
    private static final String ENCRYPT = "1";
    private static final String DECRYPT = "2";

    private static final String FILE = "1";
    private static final String DIRECTORY = "2";
    private static final String THREAD = "3";
    private static final String SERIAL = "4";

    private Input myInput;
    private Output myOutput;
    FileEncryptor <DoubleKey<DoubleKey<Integer, Integer>, Integer>> fileEncryptor;
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

    public Menu(Input myInput, Output myOutput) {
        this.myInput = myInput;
        this.myOutput = myOutput;
        fileEncryptor = new FileEncryptor<DoubleKey<DoubleKey<Integer, Integer>, Integer>>();
        setListener(new EncryptionListener(myOutput));
    }

    void DoubleMenu() {
        String action;
        while (true) {
            myOutput.output("Welcome! Press 1 to encrypt, 2 to decrypt, 0 to exit: ");
            if ((action = myInput.input()).equals(EXIT)) {
                myOutput.output("Exiting application");
                return;
            }
            if (!action.equals(ENCRYPT) && !action.equals(DECRYPT))
                myOutput.output("Invalid input, please retry");
            else {
                try {
                    String choice = subMenu();
                    File originalFile = getFileFromUser();
                    DoubleKey<DoubleKey<Integer, Integer>, Integer> cipherKey;
                    if (action.equals(ENCRYPT))
                        cipherKey = setKey(originalFile);
                    else
                        cipherKey = getKeyFromFile();
                    switch (choice){
                            case FILE:
                                started();
                                fileEncryptor.encryptOrDecryptFile(originalFile, getAlgorithms(), cipherKey, action.equals(ENCRYPT));
                                finished();
                                break;
                            case THREAD:
                                DirectoryEncryptorConcurrent<DoubleKey<DoubleKey<Integer, Integer>, Integer>> threadDirectory;
                                String path = originalFile.getPath();
                                threadDirectory = new DirectoryEncryptorConcurrent<DoubleKey<DoubleKey<Integer, Integer>, Integer>>(path);
                                started();
                                if(action.equals(ENCRYPT))
                                    threadDirectory.encryptDirectory(getAlgorithms(),cipherKey);
                                else
                                    threadDirectory.decryptDirectory(getAlgorithms(),cipherKey);
                                finished();
                                break;
                            case SERIAL:
                                started();
                                DirectoryEncryptorSequential<DoubleKey<DoubleKey<Integer, Integer>, Integer>> sequentialDirectory;
                                sequentialDirectory = new DirectoryEncryptorSequential<DoubleKey<DoubleKey<Integer, Integer>, Integer>>();
                                if(action.equals(ENCRYPT))
                                    sequentialDirectory.encryptDirectory(originalFile,getAlgorithms(),cipherKey);
                                else
                                    sequentialDirectory.decryptDirectory(originalFile,getAlgorithms(),cipherKey);
                                finished();
                                break;
                }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    String subMenu(){
        myOutput.output("Press 1 for file, 2 for directory, other to exit");
        String input = myInput.input();
        if(input.equals(FILE)){
            return FILE;
        }
        else if(input.equals(DIRECTORY)){
            myOutput.output("Use threads? 1 (yes) / 2 (no)");
            input = myInput.input();
            return input.equals("1")? THREAD : SERIAL;
        }
        return "exit";
    }

    Cipher<Couple> getAlgorithms () throws InvalidFileException {
        Cipher<Couple> forReverse = new DoubleCipher(new CaesarCipher(), new MultiplicationCipher());
        Cipher<Couple> cipher = new DoubleCipher(new ReverseCipher(forReverse), new XORCipher());
        cipher.setListener(new EncryptionListener(myOutput));
        return cipher;
    }

    private DoubleKey<DoubleKey<Integer, Integer>, Integer> setKey(File original) throws InvalidFileException {
        DoubleKey<DoubleKey<Integer, Integer>, Integer> finalKey = makeMasterKey();
        if(original.isFile())
        fileEncryptor.writeObjectToFile(fileEncryptor.createKeyFile(original), finalKey);
        else if (original.isDirectory())
            fileEncryptor.writeObjectToFile(fileEncryptor.createKeyFileforDirectory(original), finalKey);
        return finalKey;
    }

    private DoubleKey<DoubleKey<Integer, Integer>, Integer> makeMasterKey() {
        RandomKey randomKeyMaker = new RandomKey();
        MultKey multKey = new MultKey(randomKeyMaker.getKey());
        DoubleKey <Integer, Integer> forRev = new DoubleKey<Integer, Integer>(randomKeyMaker.newKey(), multKey.getKey());
        return new  DoubleKey<DoubleKey<Integer, Integer>, Integer>(forRev, randomKeyMaker.newKey());
    }

    private DoubleKey<DoubleKey<Integer, Integer>, Integer> getKeyFromFile () throws Exception{
        Serializable key = fileEncryptor.readObjectFromFile (getFileFromUser(true));
        if(key instanceof DoubleKey)
            return (DoubleKey<DoubleKey<Integer, Integer>, Integer>) key;
        throw new InvalidCipherKeyException("Key could not be read from file");
    }

    private File getFileFromUser() {
        return getFileFromUser(false);
    }

    private File getFileFromUser(boolean isKeyFile) {
        if(!isKeyFile)
            myOutput.output("Enter the path of your file / directory: ");
        else
            myOutput.output("Enter the path of the file of your key: ");
        String filePath = myInput.input();
        while (filePath.isEmpty()) {
            myOutput.output("The path you entered seems to be invalid or nonexistent. Please retry: ");
            filePath = myInput.input();// TODO: make separate func in fileEnc and throw exceptions
        }
        return new File(filePath);
    }
}
