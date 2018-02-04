/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import static teststuff.SimpleFileClient.FILE_SIZE;
import static teststuff.SimpleFileClient.FILE_TO_RECEIVED;
import static teststuff.SimpleFileClient.SERVER;
import static teststuff.SimpleFileClient.SOCKET_PORT;

/**
 *
 * @author maxoliveberg
 */
public class FileReceiver {
    
    int socketPort;
    String rFilePath;
    String serverIP;
    File rFile;
    private final int maxSize = 2000000;
    
    
    FileReceiver(int aSocket,String aServerIP, String aRFilePath) 
            throws InterruptedException, IOException{
        
        socketPort = aSocket;
        serverIP = aServerIP;
        rFilePath = aRFilePath;
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

      // receive file
            byte [] mybytearray  = new byte [maxSize];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(rFilePath);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            do {
                bytesRead =
                is.read(mybytearray, current, (mybytearray.length-current));
                if(bytesRead >= 0) current += bytesRead;
                } while(bytesRead > -1);

            bos.write(mybytearray, 0 , current);
            bos.flush();
            System.out.println("File " + rFilePath
                + " downloaded (" + current + " bytes read)");
    }
        finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (sock != null) sock.close();
    }
        
    }
}
