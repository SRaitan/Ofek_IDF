/*
package com.company;

import EncryptionAlgorithms.CaesarCipher;
import EncryptionAlgorithms.MultiplicationCipher;
import EncryptionAlgorithms.XORCipher;
import Key.Key;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

*/
/**
 * Created by Raitan on 5/6/2017.
 *//*

class CipherTest {
    Cipher cipher;
    private final String original = "this is a test";

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void caesarEncryption(){
        cipher = new CaesarCipher();
        encryptionTester();
    }
    @Test
    void xorEncryption(){
        cipher = new XORCipher();
        encryptionTester();
    }
*/
/*    @Test
    void multEncryption(){
        cipher = new MultiplicationCipher();
        encryptionTester();
    }*//*


    private byte[] encryptionDecryption (int i){
        Integer key = i;
        byte[] coded;
        try {
            coded = cipher.encryptBytes(original.getBytes(),key);
            return cipher.decryptBytes(coded,key);
        } catch (InvalidFileException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void encryptionTester(){
        for (int i = 1; i < Byte.MAX_VALUE; i ++) {
            assertArrayEquals(encryptionDecryption(i), original.getBytes(), "error with key " + i);
        }
    }

}*/
