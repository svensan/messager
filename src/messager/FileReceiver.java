/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messager;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.crypto.CipherInputStream;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.xml.bind.DatatypeConverter;


/**
 * @author maxoliveberg
 */
public class FileReceiver implements Runnable {
    /*
    Den här klassen försöker koppla upp mot en server som sätta upp av
    filesender o laddar sen hem filen med path o allt enligt indata
    
    */

    int socketPort;
    String rFilePath;
    String serverIP;
    File rFile;
    int fileSize;
    byte[] key;
    Encryptor encryptor;
    private boolean usingEncryption = false;

    FileReceiver(int aSocket, String aServerIP, String aRFilePath, int aFS)
            throws InterruptedException, IOException {

        socketPort = aSocket;
        serverIP = aServerIP;
        rFilePath = aRFilePath;
        fileSize = aFS;
        
        /*
        Sover en liten stund så motparten får set up
        
        TODO fixa timing
        */
        System.out.println("Pre sleep");
        Thread.sleep(500);
        System.out.println("Post sleep");

        Thread t = new Thread(this);
        t.start();
    }

    FileReceiver(int aSocket, String aServerIP, String aRFilePath, FileRequest req)
            throws InterruptedException, IOException {
        socketPort = aSocket;
        serverIP = aServerIP;
        rFilePath = aRFilePath;
        fileSize = req.getFileSize();

        if (req.isUsingEncryption()) {
            this.key = req.getKey();
            EncryptionFactory factory = new EncryptionFactory();
            this.encryptor = factory.getEncryptor(req.getType());
            usingEncryption = true;
        }

        System.out.println("Pre sleep");
        Thread.sleep(500);
        System.out.println("Post sleep");

        Thread t = new Thread(this);
        t.start();

    }

    FileReceiver(int aSocket, String aServerIP, String aRFilePath, int aFS, byte[] key, String type)
            throws InterruptedException, IOException {

        socketPort = aSocket;
        serverIP = aServerIP;
        rFilePath = aRFilePath;
        fileSize = aFS;

        this.key = key;
        EncryptionFactory factory = new EncryptionFactory();
        this.encryptor = factory.getEncryptor(type);
        usingEncryption = true;

        /*
        Sover en liten stund så motparten får set up

        TODO fixa timing
        */
        System.out.println("Pre sleep");
        Thread.sleep(500);
        System.out.println("Post sleep");

        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        /*
        Stora delar av de här är baserat på något jag googlade upp, även om
        det ändrats en hel del från det stadiet. Tex variabel namn är fortf
        samma
        */

        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            /*
            Ansluter sig o sätter upp alla strömmar osv 
            */
            System.out.println(serverIP);
            System.out.println(socketPort);
            sock = new Socket(serverIP, socketPort);
            System.out.println("Connecting...");
            InputStream is = sock.getInputStream();

            if (usingEncryption) {
                is = encryptor.getDecryptingInputStream(is, key);
            }

            byte[] mybytearray = new byte[fileSize];
            fos = new FileOutputStream(rFilePath);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray, 0, mybytearray.length);

            current = bytesRead;
            System.out.println(current);
            /* 
            trycker upp en progressbar enligt kravens
            */
            JFrame pF = new JFrame();
            JProgressBar bar = new JProgressBar();
            bar.setMaximum(fileSize);
            pF.add(bar);
            pF.pack();
            pF.setVisible(true);


            do {
                /*
                Checkar vad som lästs o uppdaterar baren. skriver osv
                */

                bytesRead =
                        is.read(mybytearray, current, (mybytearray.length - current));


                System.out.println(bytesRead);
                if (bytesRead >= 0) current += bytesRead;
                System.out.println(current);
                bar.setValue(current);
            } while (bytesRead > 0);
            System.out.println("File " + rFilePath
                    + " downloaded (" + current + " bytes read)");
            bos.write(mybytearray, 0, current);
            bos.flush();
            System.out.println("ree");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
                if (bos != null) bos.close();
                if (sock != null) sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
