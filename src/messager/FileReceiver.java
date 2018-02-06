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
import javax.swing.JFrame;
import javax.swing.JProgressBar;


/**
 *
 * @author maxoliveberg
 */
public class FileReceiver {
    
    int socketPort;
    String rFilePath;
    String serverIP;
    File rFile;
    int fileSize;
    
    
    FileReceiver(int aSocket,String aServerIP, String aRFilePath, int aFS) 
            throws InterruptedException, IOException{
        
        socketPort = aSocket;
        serverIP = aServerIP;
        rFilePath = aRFilePath;
        fileSize = aFS;
        System.out.println("Pre sleep");
        Thread.sleep(52);
        System.out.println("Post sleep");
        ReceiveFile();
        
        
    }
    
    private void ReceiveFile() throws IOException{
        
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(serverIP, socketPort);
            System.out.println("Connecting...");
            InputStream is = sock.getInputStream();
            /*
            BufferedReader in = 
                    new BufferedReader(
                            new InputStreamReader(is));
            fileSize = Integer.parseInt(in.readLine());
            System.out.println("file size: " + fileSize);*/
            byte [] mybytearray  = new byte [fileSize];
            fos = new FileOutputStream(rFilePath);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;
            System.out.println(current);
            JFrame pF = new JFrame();
            JProgressBar bar = new JProgressBar();
            bar.setMaximum(fileSize);
            pF.add(bar);
            pF.pack();
            pF.setVisible(true);
            
            
            do {
                bytesRead =
                is.read(mybytearray, current, (mybytearray.length-current));
                System.out.println(bytesRead);
                if(bytesRead >= 0) current += bytesRead;
                System.out.println(current);
                bar.setValue(current);
                } while(bytesRead >0);
            System.out.println("File " + rFilePath
                + " downloaded (" + current + " bytes read)");
            bos.write(mybytearray, 0 , current);
            bos.flush();
            System.out.println("ree");
    }
        finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (sock != null) sock.close();
    }
        
    }
}
