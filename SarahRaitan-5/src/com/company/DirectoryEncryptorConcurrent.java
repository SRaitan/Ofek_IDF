package com.company;
import Exceptions.ThreadException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Raitan on 5/7/2017.
 */
public class DirectoryEncryptorConcurrent<T> {
    File directory;
    FileEncryptor fileEncryptor;


    public DirectoryEncryptorConcurrent(String directoryPath) {
        fileEncryptor = new FileEncryptor();
        this. directory = new File(directoryPath);
    }

    public void operationFiles(File directory, Cipher fileOperation, T key) {
        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        File[] allFiles = directory.listFiles();
        ExecutorService readWriteThread = Executors.newSingleThreadExecutor();
        byte[] fileBytes;
        Future future;
        for (File file : allFiles) {
            if (file.isFile()) {
                future = readWriteThread.submit(readBytesCallable(file));
                try {
                    fileBytes = (byte[]) future.get();
                    //System.out.println(new String(fileBytes));
                    byte[] copyFileBytes = Arrays.copyOf(fileBytes, fileBytes.length);
                    future = threadPool.submit(encryptCallable(fileOperation, copyFileBytes, key));
                    //todo: void callable
                    //readWriteThread.submit(runnableWriteToFile(future,file,fileOperation ));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private Callable<byte[]> encryptCallable(Cipher cipher, byte[] bytes, T key) {
        return () -> cipher.encryptBytes(bytes, key);
    }

    private Callable<byte[]> decryptCallable(Cipher cipher, byte[] bytes, T key) {
        return () -> cipher.decryptBytes(bytes, key);
    }

    private Callable<byte[]> readBytesCallable(File file) {
        return () -> fileEncryptor.readFromFile(file);
    }

    private Callable<Void> writeBytesCallable(Future future, File file) {
        return ()-> {
            fileEncryptor.writeToFile(file, (byte[]) future.get());
            return null;};
    }
    File[] getFiles(File directory){
        File[] listFile = directory.listFiles();
        File[] filesOnly = new File[listFile.length];
        int filesOnlyCounter = 0;
        for (File file : listFile) {
            if(file.isFile())
                filesOnly[filesOnlyCounter++] = file;
        }
        return filesOnly;
    }

    public void encryptDirectory(Cipher cipher, T key) throws ExecutionException, InterruptedException, ThreadException {
        try {
            ExecutorService readWriteThread = Executors.newSingleThreadExecutor();
            ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            File[] listFile = getFiles(directory);
            int fileCounter = 0;
            // todo: for on both of them
            List<Future<byte[]>> readFilesForEncryption = readAndSaveFiles(listFile);
            for (Future<byte[]> future : readFilesForEncryption) {
                byte[] readBytes = future.get();
                Future<byte[]> futureEncryption = threadPool.submit(encryptCallable(cipher, Arrays.copyOf(readBytes, readBytes.length), key));
                File returnFile = fileEncryptor.returnDirectory(listFile[fileCounter++], true);
                readWriteThread.submit(writeBytesCallable(futureEncryption, returnFile));
            }
            readWriteThread.shutdown();
            threadPool.shutdown();
        }
        catch (Exception ex) { //todo: separate for read / write
            throw new ThreadException("Problem reading file");
        }
    }

    public void decryptDirectory(Cipher cipher, T key) throws ExecutionException, InterruptedException, ThreadException {
        try {
            //directory = new File(directory.getAbsolutePath() + "\\encrypt\\");
            ExecutorService readWriteThread = Executors.newSingleThreadExecutor();
            ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            File[] listFile = getFiles(directory);
            int fileCounter = 0;
            //todo: for on both of them
            List<Future <byte[]>> readFilesForDecryption = readAndSaveFiles(listFile);
            for (Future <byte[]> future : readFilesForDecryption) {
                byte[] readBytes = future.get();
                Future<byte[]> futureDecryption = threadPool.submit(decryptCallable(cipher, Arrays.copyOf(readBytes, readBytes.length), key));
                File returnFile = fileEncryptor.returnDirectory(listFile[fileCounter++], false);
                readWriteThread.submit(writeBytesCallable(futureDecryption, returnFile));
            }
            readWriteThread.shutdown();
            threadPool.shutdown();
        }
        catch (Exception ex){
            throw new ThreadException("Problem reading file");
            //ex.printStackTrace();
        }
    }
    //todo: better name
    private List <Future<byte[]>> readAndSaveFiles(File [] listFile){
        ExecutorService readThread = Executors.newSingleThreadExecutor();
        List <Future <byte []>> futuresForEncryption = new ArrayList<>();
        for (File file : listFile) {
            if(file!= null && file.isFile()) {
                futuresForEncryption.add(readThread.submit(readBytesCallable(file)));
            }
        }
        return futuresForEncryption;
    }
    //todo: TEST
}


















