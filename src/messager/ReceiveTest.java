/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messager;

import java.io.IOException;

/**
 *
 * @author maxoliveberg
 */
public class ReceiveTest {
    
    public static void main (String [] args ) throws IOException, InterruptedException {

     
     FileReceiver r  = new FileReceiver(13267,"10.0.1.7",
             "/Users/maxoliveberg/receiver.rtf");
     
     
     }
    
}
