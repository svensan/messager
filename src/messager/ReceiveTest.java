/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messager;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author maxoliveberg
 */
public class ReceiveTest {
    
    public static void main (String [] args ) throws IOException, InterruptedException {

     File sentFile = new File("/Users/maxoliveberg/Downloads/F6.pdf");
     FileReceiver r  = new FileReceiver(13267,"10.0.1.7",
             "/Users/maxoliveberg/f6.pdf",(int)sentFile.length());
     
     
     }
    
}
