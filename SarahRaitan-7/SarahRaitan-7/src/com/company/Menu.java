package com.company;
import EncryptionAlgorithms.*;
import Key.*;
import File.*;
import InputOutput.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;

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
    FileEncryptor<DoubleKey<DoubleKey<Integer, Integer>, Integer>> fileEncryptor;
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

    void XMLMenu(){
        try {
            File fxmlFile = new File("C:\\Users\\Jbt\\Desktop\\SarahRaitan-7\\src\\XMLFiles\\encryptor");
            //todo: change to be relative path
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fxmlFile);
            doc.getDocumentElement().normalize();
            NodeList encryptor = doc.getElementsByTagName("encryptor");
            Node node = encryptor.item(0); //only item there is bc there is one encryptor
            Element encyrptorElement = (Element)node;
            NodeList directoryList = encyrptorElement.getElementsByTagName("directory");
            handleDirectory(directoryList);
            NodeList fileList = encyrptorElement.getElementsByTagName("file");
            handleFile(fileList);
            System.out.println("hey");

        } catch (Exception e) {
            //todo: separate exceptions
            e.printStackTrace();
        }
    }

    private void handleFile(NodeList fileList) {
        try {
            String path;
            String type;
            for (int i = 0; i < fileList.getLength(); i++) {
                Node node = fileList.item(i);
                Element directory = (Element) node;
                path = directory.getElementsByTagName("path").item(0).getTextContent();
                type = directory.getElementsByTagName("type").item(0).getTextContent();
                if (type.equals("encrypt")) {
                    started();
                    fileEncryptor.encryptOrDecryptFile(new File(path), getAlgorithms(), setKey(new File(path)), true);
                    finished();
                } else {
                    started();
                    fileEncryptor.encryptOrDecryptFile(new File(path), getAlgorithms(), getKeyFromFile(path), false);
                    finished();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDirectory(NodeList directoryList) {
        try {
        String path;
        String type; //todo: enum
        //FileEncryptor fileEncryptor;
        for (int i = 0; i < directoryList.getLength(); i++) {
            Node node = directoryList.item(i);
            Element directory = (Element) node;
            path = directory.getElementsByTagName("path").item(0).getTextContent();
            type = directory.getElementsByTagName("type").item(0).getTextContent();

            if(type.equals("encrypt")) {
                if ((directory.getAttribute("async").equals("true"))) {
                    started();
                    DirectoryEncryptorSequential<DoubleKey<DoubleKey<Integer, Integer>, Integer>> sequentialDirectory;
                    sequentialDirectory = new DirectoryEncryptorSequential<DoubleKey<DoubleKey<Integer, Integer>, Integer>>();
                    sequentialDirectory.encryptDirectory(new File(path), getAlgorithms(), setKey(new File(path)));
                    finished();
                }
                else{
                    DirectoryEncryptorConcurrent<DoubleKey<DoubleKey<Integer, Integer>, Integer>> threadDirectory;
                    threadDirectory = new DirectoryEncryptorConcurrent<DoubleKey<DoubleKey<Integer, Integer>, Integer>>(path);
                    started();
                    threadDirectory.encryptDirectory(getAlgorithms(),setKey(new File(path)));
                    finished();
                }
            }
            else{
                if ((directory.getAttribute("async").equals("true"))) {
                    started();
                    DirectoryEncryptorSequential<DoubleKey<DoubleKey<Integer, Integer>, Integer>> sequentialDirectory;
                    sequentialDirectory = new DirectoryEncryptorSequential<DoubleKey<DoubleKey<Integer, Integer>, Integer>>();
                    sequentialDirectory.decryptDirectory(new File(path), getAlgorithms(), getKeyFromDirectory(path));
                    finished();
                }
                else{
                    DirectoryEncryptorConcurrent<DoubleKey<DoubleKey<Integer, Integer>, Integer>> threadDirectory;
                    threadDirectory = new DirectoryEncryptorConcurrent<DoubleKey<DoubleKey<Integer, Integer>, Integer>>(path);
                    started();
                    threadDirectory.decryptDirectory(getAlgorithms(),getKeyFromDirectory(path));
                    finished();
                }

            }

        }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        cipherKey = getKeyFromFile(originalFile.getPath());
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

    private DoubleKey<DoubleKey<Integer, Integer>, Integer> getKeyFromFile (String path) throws Exception{
        path = fileEncryptor.getKeyPath(path);
        Serializable key = fileEncryptor.readObjectFromFile(new File(path ));
        if(key instanceof DoubleKey)
            return (DoubleKey<DoubleKey<Integer, Integer>, Integer>) key;
        throw new InvalidCipherKeyException("Key could not be read from file");
    }

    private DoubleKey<DoubleKey<Integer, Integer>, Integer> getKeyFromDirectory (String path) throws Exception{
        path = fileEncryptor.getKeyFileforDirectory(path);
        Serializable key = fileEncryptor.readObjectFromFile(new File(path ));
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
