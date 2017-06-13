package com.company;

import InputOutput.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Raitan on 3/29/2017.
 */
public class EncryptionListener implements CipherListener {
    private long begin;
    Output output;

    public EncryptionListener(Output output) {
        this.output = output;
    }

    @Override
        public void onStarted() {
        begin = System.nanoTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        output.output("Started: " + dateFormat.format(date));
    }

    @Override
    public void onFinished() {
        output.output("Total operation runtime "+ (System.nanoTime() - begin) + "ns");
    }
}
