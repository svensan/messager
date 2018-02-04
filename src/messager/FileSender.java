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
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author maxoliveberg
 */
public class FileSender {
    
    int socketPort;
    String sentFilePath;
    File sentFile;
    
    FileSender(int argPort, String aSentFilePath) throws IOException{
        
        socketPort = argPort;
        sentFilePath = aSentFilePath;
        sentFile = new File(sentFilePath);
        
        RunServer();
        
    }
    
    private void RunServer() throws FileNotFoundException, IOException{
        
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;
        
        System.out.println("Waiting for client");
        
        try{
            servsock = new ServerSocket(socketPort);
            while (true) {
               System.out.println("Waiting...");
                try {
                    sock = servsock.accept();
                    System.out.println("Accepted connection : " + sock);

                    
                    byte [] mybytearray  = new byte [(int)sentFile.length()];
                    fis = new FileInputStream(sentFile);
                    bis = new BufferedInputStream(fis);
                    bis.read(mybytearray,0,mybytearray.length);
                    os = sock.getOutputStream();
                    System.out.println("Sending " 
                            + sentFilePath + "(" 
                            + mybytearray.length + " bytes)");
                    os.write(mybytearray,0,mybytearray.length);
                    os.flush();
                    System.out.println("Done.");
                    if (servsock != null) servsock.close();
                    return;
        }
                finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sock!=null) sock.close();
        }
      }
        }
        finally {
            if (servsock != null) servsock.close();
        
    }
    
    
    
    }
}
