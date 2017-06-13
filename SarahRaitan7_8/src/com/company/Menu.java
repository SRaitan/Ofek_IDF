package com.company;
import EncryptionAlgorithms.*;
import Exceptions.InvalidCipherKeyException;
import Exceptions.ThreadException;
import Files.DirectoryEncryptorConcurrent;
import Files.DirectoryEncryptorSequential;
import Files.FileEncryptor;
import Key.*;
import InputOutput.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

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
    FileEncryptor fileEncryptor;


    public Menu(Input myInput, Output myOutput) {
        this.myInput = myInput;
        this.myOutput = myOutput;
        fileEncryptor = new FileEncryptor();
        fileEncryptor.setListener(new EncryptionListener(myOutput));
        //setListener(new EncryptionListener(myOutput));
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
            Couple<Couple<Key<Integer>, Key<Integer>>, Key<Integer>> cipherKey;
            for (int i = 0; i < fileList.getLength(); i++) {
                Node node = fileList.item(i);
                Element directory = (Element) node;
                path = directory.getElementsByTagName("path").item(0).getTextContent();
                type = directory.getElementsByTagName("type").item(0).getTextContent();
                if (type.equals("encrypt"))
                    cipherKey = generateKeys(new File(path));
                else
                    cipherKey = getKeysFromFile(getFileFromUser(true));
                fileEncryptor.encryptOrDecryptFile(new File(path), generateCipher(cipherKey),  type.equals("encrypt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDirectory(NodeList directoryList) {
        try {
            String path;
            String type;
            Couple<Couple<Key<Integer>, Key<Integer>>, Key<Integer>> cipherKey;
            CipherInterface cipher;
            for (int i = 0; i < directoryList.getLength(); i++) {
                Node node = directoryList.item(i);
                Element directory = (Element) node;
                path = directory.getElementsByTagName("path").item(0).getTextContent();
                type = directory.getElementsByTagName("type").item(0).getTextContent();

                if(type.equals("encrypt")) {
                    cipherKey = generateKeys(new File(path));
                    cipher = generateCipher(cipherKey);
                    if ((directory.getAttribute("async").equals("true"))) {
                        DirectoryEncryptorSequential sequentialDirectory;
                        sequentialDirectory = new DirectoryEncryptorSequential();
                        sequentialDirectory.encryptDirectory(new File(path), cipher);
                    }
                    else{
                        DirectoryEncryptorConcurrent threadDirectory;
                        threadDirectory = new DirectoryEncryptorConcurrent(path);
                        threadDirectory.encryptDirectory(cipher);
                    }
                }
                else{
                    cipherKey = getKeysFromFile(getFileFromUser(true));
                    cipher = generateCipher(cipherKey);
                    if ((directory.getAttribute("async").equals("true"))) {
                        DirectoryEncryptorSequential sequentialDirectory;
                        sequentialDirectory = new DirectoryEncryptorSequential();
                        sequentialDirectory.decryptDirectory(new File(path), cipher);
                    }
                    else{
                        DirectoryEncryptorConcurrent threadDirectory;
                        threadDirectory = new DirectoryEncryptorConcurrent(path);
                        threadDirectory.decryptDirectory(cipher);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void mainMenu() {
        //todo: initialize listener of fileEncryptor
        String action;
        Couple<Couple<Key<Integer>, Key<Integer>>, Key<Integer>> cipherKey;
        CipherInterface cipher;
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
                    if (action.equals(ENCRYPT))
                        cipherKey = generateKeys(originalFile);
                    else
                        cipherKey = getKeysFromFile(getFileFromUser(true));
                    cipher = generateCipher(cipherKey);
                    switch (choice){
                            case FILE:
                                fileEncryptor.encryptOrDecryptFile(originalFile, cipher, action.equals(ENCRYPT));
                                break;
                            case THREAD:
                                directoryThread(action, cipher, originalFile);
                                break;
                            case SERIAL:
                                directorySerial(action, cipher, originalFile);
                                break;
                }} catch (Exception e) {
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

    private void directoryThread(String action, CipherInterface cipher, File originalFile) throws ExecutionException, InterruptedException, ThreadException {
        DirectoryEncryptorConcurrent threadDirectory;
        String path = originalFile.getPath();
        threadDirectory = new DirectoryEncryptorConcurrent(path);
        if(action.equals(ENCRYPT))
            threadDirectory.encryptDirectory(cipher);
        else
            threadDirectory.decryptDirectory(cipher);
    }

    private void directorySerial(String action, CipherInterface cipher, File originalFile) {
        DirectoryEncryptorSequential sequentialDirectory;
        sequentialDirectory = new DirectoryEncryptorSequential();
        if(action.equals(ENCRYPT))
            sequentialDirectory.encryptDirectory(originalFile,cipher);
        else
            sequentialDirectory.decryptDirectory(originalFile,cipher);
    }

    CipherInterface generateCipher (Couple<Couple<Key<Integer>, Key<Integer>>, Key<Integer>> key){
        Key<Integer> caesarKey=key.getFirst().getFirst();
        Key<Integer> multKey=key.getFirst().getSecond();
        Key<Integer> xorKey=key.getSecond();
        CipherInterface<Couple> forReverse = new DoubleCipher(new CaesarCipher(caesarKey), new MultiplicationCipher(multKey));
        return new DoubleCipher(new ReverseCipher(forReverse), new XORCipher(xorKey));
    }

    Couple<Couple<Key<Integer>, Key<Integer>>, Key<Integer>> generateKeys(File original) throws InvalidFileException {
        RandomKey randomKeyMaker = new RandomKey();
        MultKey multKey = new MultKey(randomKeyMaker.getKey());

        Couple<Key<Integer>,Key<Integer>> forRev;
        forRev = new Couple<Key<Integer>, Key<Integer>>(randomKeyMaker.newKey(), multKey);
        Couple<Couple<Key<Integer>, Key<Integer>>, Key<Integer>> finalKey;
        finalKey = new Couple<Couple<Key<Integer>, Key<Integer>>, Key<Integer>>(forRev,randomKeyMaker.newKey());

        if(original.isFile())
            fileEncryptor.writeObjectToFile(fileEncryptor.createKeyFile(original), finalKey);
        else if (original.isDirectory())
            fileEncryptor.writeObjectToFile(fileEncryptor.createKeyFileforDirectory(original), finalKey);
        return finalKey;
    }

    Couple<Couple<Key<Integer>, Key<Integer>>, Key<Integer>> getKeysFromFile(File keyFile) throws InvalidFileException {
         Serializable key = fileEncryptor.readObjectFromFile (keyFile);
         if(key instanceof Couple)
             return (Couple<Couple<Key<Integer>, Key<Integer>>, Key<Integer>>) key;
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
