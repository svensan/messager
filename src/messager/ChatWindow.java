/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messager;

import java.awt.*;
import static java.awt.Color.BLACK;
import static java.awt.Color.blue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


/**
 *
 * @author maxoliveberg
 */
public class ChatWindow {
    JFrame mainFrame; // mainpanel sitter på mainframe
    JPanel mainPanel; // ALLT sitter på mainpanel
    
    JPanel buttonPanel; 
    
    JTextPane chatWindow;
    JScrollPane chatScrollWindow;
    
    JButton closeButton;
    JButton sendFileButton;
    JButton colorButton;
    
    JTextField textField;
    //SendWindow activeSendWindow;
    
    Client user;
    
    ChatWindow(Client creator){
        
        user = creator;
       // ReceiveWindow meme = new ReceiveWindow("jewlord",6969);
        mainFrame = new JFrame();
        mainPanel = new JPanel(new GridBagLayout());
        
        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                user.close();
                mainFrame.dispose();
                
            }});
        
        sendFileButton = new JButton("Send File");
        sendFileButton.addActionListener(new ActionListener() { 
                    public void actionPerformed(ActionEvent e) 
                         {
                             SendWindow sWindow = new SendWindow(
                                     user.isAdmin());}});
        
        colorButton = new JButton("Select color");
        colorButton.addActionListener(new ActionListener() { 
                    public void actionPerformed(ActionEvent e) 
                         {
                             ColorWindow sWindow = new ColorWindow(user);
                         }});
        
        buttonPanel = new JPanel();
        GridBagConstraints buttonC = new GridBagConstraints();
        buttonC.gridx = 0;
        buttonC.gridy = 0;
        
        buttonPanel.add(closeButton);
        buttonPanel.add(sendFileButton);
        buttonPanel.add(colorButton);
        
        chatWindow = new JTextPane();
        chatWindow.setEditable(false);
        
        chatScrollWindow = new JScrollPane(chatWindow);
        chatScrollWindow.setPreferredSize(new Dimension(480,320));
        chatScrollWindow.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        GridBagConstraints scrollC = new GridBagConstraints();
        scrollC.gridx = 0;
        scrollC.gridy = 1;
        
        textField = new JTextField("type message...*",35);
        textField.addActionListener(new ActionListener() { 
                    public void actionPerformed(ActionEvent e) 
                         {
                         String messageText = textField.getText();
                         Color color = user.getColor();
                         String name = user.getName();
                         
                         Message message = new Message(
                         color, name, messageText);
                         
                         user.sendMessage(message);
                         
                         textField.setText("");
                         }});
        
        GridBagConstraints textC = new GridBagConstraints();
        textC.gridx = 0;
        textC.gridy = 2;
        
        mainPanel.add(buttonPanel,buttonC);
        mainPanel.add(chatScrollWindow,scrollC);
        mainPanel.add(textField,textC);
        
        mainFrame.add(mainPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);

    } 
    
    public void handleMessage(Message argM){
        
        String user = argM.getSenderName();
        String message = argM.getText();
        Color color = argM.getColor();
        StyledDocument doc = chatWindow.getStyledDocument();

        Style style = chatWindow.addStyle("I'm a Style", null);
        StyleConstants.setForeground(style, color);

        try { doc.insertString(doc.getLength(), user+": "+message +"\n",style);}
        catch (BadLocationException e){}

        
       
        
    }
    

    
        class SendWindow{
            
            JFrame sMainFrame;
            JPanel sMainPanel;
            JButton sCloseButton;
            JButton sSendButton;
            JButton browseButton;
            JTextField recipientField;
            JTextField messageField;
            JTextField portField;
            JTextField pathField;
            
            String filePath;
            String fileName;
            String recipient;
            long fileSize;
            
            public SendWindow(boolean isHost){
                
               
                
                sMainPanel = new JPanel();
                sMainPanel.setLayout(new GridLayout(5,1));
                if(isHost == true){
                    recipientField = new JTextField("Recipient...",15);
                    sMainPanel.add(recipientField);
                }else{
                    recipient = user.getConnectedIP();
                }
                
                pathField = new JTextField("");
                pathField.setEditable(false);
                
                browseButton = new JButton("Browse for file...");
                browseButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                        int result = fileChooser.showOpenDialog(mainFrame);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = fileChooser.getSelectedFile();
                            
                            fileName = selectedFile.getName();
                            fileSize = selectedFile.length();
                            filePath = selectedFile.getAbsolutePath();
                            pathField.setText(filePath);
                            System.out.println("Selected file: " + selectedFile.getAbsolutePath()
                            + " file size: " + fileSize + " name " + fileName);
                    }}});
                
                sMainPanel.add(pathField);
                sMainPanel.add(browseButton);
                
                portField = new JTextField("Port...",18);
                sMainPanel.add(portField);
                
                messageField = new JTextField("Message...");
                sMainPanel.add(messageField);
                
                sSendButton = new JButton("Send");
                sSendButton.addActionListener(new ActionListener() { 
                    public void actionPerformed(ActionEvent e) 
                         
                    {
                        //boolean meme =  user.checkIP(recipientField.getText());
                        //System.out.println(meme + " at ip " 
                         //       + recipientField.getText());
                        
                        user.setSendInfo(Integer.parseInt(portField.getText()),
                                (int)fileSize,
                                fileName, filePath);
                        
                        FileRequest requesto = new FileRequest(fileName,
                                (int)fileSize);
                        
                        Message fileReqMsg = new Message(user.getName(),
                        messageField.getText(),requesto);
                        
                        if(recipient== null){
                        recipient = recipientField.getText();
                        }
                        
                        user.sendFileRequest(fileReqMsg, recipient);
                        
                         
                         }});
                
                sMainPanel.add(sSendButton);
                
                sCloseButton = new JButton("Close");
                sCloseButton.addActionListener(new ActionListener() { 
                    public void actionPerformed(ActionEvent e) 
                        {
                        
                        
                        sMainFrame.dispose();
                             
                        }});
                sMainPanel.add(sCloseButton);
                
                sMainFrame = new JFrame();
                sMainFrame.add(sMainPanel);
                sMainFrame.setSize(300,150);
                sMainFrame.pack();
                sMainFrame.setVisible(true);
                
                
                
            }
       
        }
        
        public void createReceiveWindow(String name,String sendName, long size,
                String argIP){
            ReceiveWindow windo = new ReceiveWindow(name,sendName,size,argIP);
        }
        
        class ReceiveWindow{
            
            JFrame rMainFrame;
            JPanel rMainPanel;
            JTextField infoField;
            JTextField rPathField;
            JTextField responseField;
            JTextField rPortField;
            JButton yesButton;
            JButton noButton;
            
            
            String filePath;
            public ReceiveWindow(String name,String sendName, long size,
                    String argIP){
                
                infoField = new JTextField("File name: " + name + " Size: " 
                                            +size+". Sent by:" + sendName);
                infoField.setEditable(false);
                
                rPathField = new JTextField("C://"+name);
                rPortField = new JTextField("port");
                responseField = new JTextField("Type a response here...");
                
                yesButton = new JButton("Accept file");
                yesButton.addActionListener(new ActionListener(){
                    
                public void actionPerformed(ActionEvent e) {
                   FileResponse respondo = new FileResponse(true,
                           Integer.parseInt(rPortField.getText()));
                   Message texado = new Message(user.getColor(),
                           user.getName(),responseField.getText());
                   Message messado = new Message(user.getName(),user.getName(),respondo);
                   
                   user.sendMessage(texado);
                   user.sendMessage(messado);
                   
                   String ip;

                    try {
                        FileReceiver receivaton = new FileReceiver(
                                Integer.parseInt(rPortField.getText()),
                                argIP,rPathField.getText(),(int)size);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                
            }});
                
                noButton = new JButton("Deny file");
                noButton.addActionListener(new ActionListener(){
                    
                public void actionPerformed(ActionEvent e) {
                           FileResponse respondo = new FileResponse(false,
                           Integer.parseInt(rPortField.getText()));
                           
                           Message texado = new Message(user.getColor(),
                           user.getName(),responseField.getText());
                           
                           Message messado = new Message(user.getName(),"",respondo);
                   
                    user.sendMessage(texado);
                    user.sendMessage(messado);
                
                }});
                
                rMainPanel = new JPanel();
                rMainPanel.add(infoField);
                rMainPanel.add(rPathField);
                rMainPanel.add(rPortField);
                rMainPanel.add(responseField);
                rMainPanel.add(yesButton);
                rMainPanel.add(noButton);
                
                rMainFrame = new JFrame();
                rMainFrame.add(rMainPanel);
                rMainFrame.pack();
                rMainFrame.setVisible(true);
                
                
                
                
                
            }
        }
        
        class ColorWindow{
            
            Client cUser;
            JFrame cMainFrame;
            JPanel cMainPanel;
            JTextField hexField;
            JButton confirmButton;
            
            public ColorWindow(Client aUser){
                
                cUser = aUser;
                
                hexField = new JTextField("Enter hexcode");
                
                confirmButton = new JButton("Confirm");
                confirmButton.addActionListener(new ActionListener() { 
                    public void actionPerformed(ActionEvent e) 
                         {
                             Color newColor = 
                                     createColorFromHex(hexField.getText());
                             
                             cUser.setColor(newColor);
                             cMainFrame.dispose();
                             
                         }});
                
                
               cMainPanel = new JPanel();
               cMainPanel.add(hexField);
               cMainPanel.add(confirmButton);
               
               cMainFrame = new JFrame();
               cMainFrame.add(cMainPanel);
               cMainFrame.pack();
               cMainFrame.setVisible(true);
               
                
                     
                    
            }
            
            private Color createColorFromHex(String hexColor) {
                int r, g, b;
                r = Integer.valueOf(hexColor.substring(1, 3), 16);
                g = Integer.valueOf(hexColor.substring(3, 5), 16);
                b = Integer.valueOf(hexColor.substring(5, 7), 16);

               return new Color(r,g,b);
        }
        }
        
        public class ConnectionWindow{
            
            JFrame connectMainFrame;
            JButton yesB;
            JButton noB;
            
            
            public ConnectionWindow(Message message,ClientRep sender){
                
                connectMainFrame = new JFrame();
                JTextField text = new JTextField("User "+ 
                        message.getSenderName()+" wants to connect. "+
                        "Connection information: "+message.getText());
                
                yesB = new JButton("Accept request");
                yesB.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    sender.acceptConnection();
                    Message accepto = new Message(BLACK,"Server","User "
                            + message.getSenderName()+" accepted.");
                    user.getServer().sendMessage(accepto, sender);
                    }});
                
                noB = new JButton("Deny Request");
                
                noB.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        Message accepto = new Message(BLACK,"Server","User "
                                + message.getSenderName()+" denied.");
                        
                        user.getServer().sendMessage(accepto, sender);
                        user.getServer().removeRep(sender);
                        sender.closeConnection();
                    }});
                
                connectMainFrame.add(text);
                connectMainFrame.add(yesB);
                connectMainFrame.add(noB);
                connectMainFrame.pack();
                connectMainFrame.setVisible(true);
            }
            
        }
        
        public void createConnectionWindow(Message message, ClientRep sender){
            ConnectionWindow connecto = new ConnectionWindow(message, sender);
        }
        
        
    }

