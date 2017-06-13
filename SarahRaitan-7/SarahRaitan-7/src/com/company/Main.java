package com.company;

import EncryptionAlgorithms.CaesarCipher;
import InputOutput.UserInput;
import InputOutput.UserOutput;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
        new Menu(new UserInput(), new UserOutput()).XMLMenu();
        /*CaesarCipher caesar = new CaesarCipher();
        DirectoryEncryptorSequential<Integer> direc = new DirectoryEncryptorSequential<>();
        direc.encryptDirectory(new File("C:\\Users\\Jbt\\Desktop\\cipher"),caesar,56);
        direc.decryptDirectory(new File("C:\\Users\\Jbt\\Desktop\\cipher\\encrypt"),caesar,56);*/

/*        CaesarCipher caesar2 = new CaesarCipher();
        DirectoryEncryptorConcurrent<Integer> direc = new DirectoryEncryptorConcurrent<Integer>("C:\\Users\\Jbt\\Desktop\\cipher");
        try {
            direc.encryptDirectory(caesar2,43);
            direc.decryptDirectory(caesar2,43);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("finished");*/
    }
}
