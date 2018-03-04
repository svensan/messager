/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.xml.bind.DatatypeConverter;

/**
 * @author maxoliveberg
 */
public class FileSender {
    /*
    Öppnar upp en server o skickar över en fil till den som ansluter.
    motparten till filereceiver. Skickar fil o sånt baserat på indata
    */

    int socketPort;
    String sentFilePath;
    File sentFile;
    int fileSize;
    private byte[] key;
    private Encryptor encryptor;
    private boolean usingEncryption = false;

    FileSender(int argPort, String aSentFilePath) throws IOException {

        socketPort = argPort;
        sentFilePath = aSentFilePath;
        sentFile = new File(sentFilePath);
        fileSize = (int) sentFile.length();

        RunServer();

    }

    FileSender(int argPort, String aSentFilePath, byte[] key, String type) throws IOException {

        socketPort = argPort;
        sentFilePath = aSentFilePath;
        sentFile = new File(sentFilePath);
        fileSize = (int) sentFile.length();

        this.key = key;
        EncryptionFactory factory = new EncryptionFactory();
        this.encryptor = factory.getEncryptor(type);
        usingEncryption = true;
        RunServer();
    }

    private void RunServer() throws FileNotFoundException, IOException {
        /*
        sätter upp saker o chillar sen på anslutning
        */

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;

        System.out.println("Waiting for client");

        try {
            servsock = new ServerSocket(socketPort);
            while (true) {
                System.out.println("Waiting...");
                try {
                    sock = servsock.accept();
                    System.out.println("Accepted connection : " + sock);

                    int current = 0;
                    int bytesSent = 0;
                    byte[] mybytearray = new byte[256];
                    os = sock.getOutputStream();

                    if (usingEncryption) {
                        os = encryptor.getEncryptingOutputStream(os, key);
                    }

                    fis = new FileInputStream(sentFile);
                    bis = new BufferedInputStream(fis);
                    /*
                    trycket upp en bar enl krav
                    */
                    JFrame pF = new JFrame();
                    JProgressBar bar = new JProgressBar();
                    bar.setMaximum(fileSize);
                    pF.add(bar);
                    pF.pack();
                    pF.setVisible(true);

                    System.out.println("Sending "
                            + sentFilePath + "("
                            + mybytearray.length + " bytes)");

                    while ((bytesSent = bis.read(mybytearray, 0,
                            mybytearray.length)) != -1) {
                        /*
                        kollar stegvis hur mycekt den lyckas skriva över
                        o uppdaterar sendbar därefter.
                        */
                        current += bytesSent;
                        System.out.println(current);
                        bar.setValue(current);
                        os.write(mybytearray, 0, mybytearray.length);
                    }

                    os.flush();
                    System.out.println("Done.");
                    if (servsock != null) servsock.close();
                    return;
                } finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sock != null) sock.close();
                }
            }
        } finally {
            if (servsock != null) servsock.close();


        }


    }
}
