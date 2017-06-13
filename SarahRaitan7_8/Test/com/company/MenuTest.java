/*
package com.company;

import InputOutput.*;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;

*/
/**
 * Created by Raitan on 5/6/2017.
 *//*

class MenuTest {
    Menu menu;
    static Input input;
    static Output output;

    @Test
    void testInput(){

    }


    @BeforeEach
    void setUp() {
        System.out.println("In setup");
        input = mock (Input.class);
        output = mock (Output.class);
        menu=new Menu(input,output);
    }

    @AfterEach
    void tearDown() {
        System.out.println("In teardown");
    }

    @Test
    void forStringInput() {
        when(input.input()).thenReturn("a").thenReturn("0");
        menu.mainMenu();
        verify (output).output("Invalid input, please retry");
    }
    @Test
    void forBlankInput() {
        when(input.input()).thenReturn("").thenReturn("0");
        menu.mainMenu();
        verify (output).output("Invalid input, please retry");
    }

   */
/* @Test
    void checkCorrectInput() {
        when(input.input()).thenReturn("1").thenReturn("0");
        menu.mainMenu();
        verify(output).output("Enter the path of the file you want to work with: ");
    }
    @Test
    void forIncorrectFilepath() {
        when(input.input()).thenReturn("2").thenReturn("ghfdfdhfdf").thenReturn("0");
        menu.mainMenu();
        verify(output).output("Something went wrong... Please try again");
    }*//*

    @Test
    void testExit() {
        when(input.input()).thenReturn("0");
        menu.mainMenu();
        verify(output).output("Exiting application");
    }

}*/
