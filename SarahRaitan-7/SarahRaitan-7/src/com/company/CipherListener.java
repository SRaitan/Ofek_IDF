package com.company;

/**
 * Created by Raitan on 3/30/2017.
 */
public interface CipherListener{
    void onStarted(String fileName);
    void onFinished(String fileName);
}