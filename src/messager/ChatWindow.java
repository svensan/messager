/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messager;

import java.awt.*;
import static java.awt.Color.blue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
    
    Client user;
    
    ChatWindow(Client creator){
        
        user = creator;
       // ReceiveWindow meme = new ReceiveWindow("jewlord",6969);
        mainFrame = new JFrame();
        mainPanel = new JPanel(new GridBagLayout());
        
        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {mainFrame.dispose();}});
        
        sendFileButton = new JButton("Send File");
        sendFileButton.addActionListener(new ActionListener() { 
                    public void actionPerformed(ActionEvent e) 
                         {
                             SendWindow sWindow = new SendWindow(true);}});
        
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

        try { doc.insertString(doc.getLength(), user+": "+message +"\n",style); }
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
            
            String filePath;
            String fileName;
            long fileSize;
            
            public SendWindow(boolean isHost){
                
                sMainPanel = new JPanel();
                sMainPanel.setLayout(new GridLayout(5,1));
                if(isHost == true){
                    recipientField = new JTextField("Recipient...",15);
                    sMainPanel.add(recipientField);
                }
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
                            System.out.println("Selected file: " + selectedFile.getAbsolutePath()
                            + " file size: " + fileSize + " name " + fileName);
                    }}});
                
                sMainPanel.add(browseButton);
                
                //portField = new JTextField("Port...",18);
               // sMainPanel.add(portField);
                
                messageField = new JTextField("Message...");
                sMainPanel.add(messageField);
                
                sSendButton = new JButton("Send");
                sSendButton.addActionListener(new ActionListener() { 
                    public void actionPerformed(ActionEvent e) 
                         
                    {
                        boolean meme =  user.checkIP(recipientField.getText());
                        System.out.println(meme + " at ip " 
                                + recipientField.getText());
                        
                         
                         }});
                
                sMainPanel.add(sSendButton);
                
                sCloseButton = new JButton("Close");
                sCloseButton.addActionListener(new ActionListener() { 
                    public void actionPerformed(ActionEvent e) 
                         {sMainFrame.dispose();}});
                sMainPanel.add(sCloseButton);
                
                sMainFrame = new JFrame();
                sMainFrame.add(sMainPanel);
                sMainFrame.setSize(300,150);
                sMainFrame.pack();
                sMainFrame.setVisible(true);
                
                
                
            }
        }
        
        class ReceiveWindow{
            
            JFrame rMainFrame;
            JPanel rMainPanel;
            JTextField infoField;
            JTextField rPathField;
            JTextField responseField;
            JButton yesButton;
            JButton noButton;
            
            
            String filePath;
            public ReceiveWindow(String name, long size){
                
                infoField = new JTextField("File name: " + name + " Size: " 
                                            +size);
                infoField.setEditable(false);
                
                rPathField = new JTextField("C:/"+name);
                responseField = new JTextField("Type a response here...");
                
                yesButton = new JButton("Accept file");
                noButton = new JButton("Deny file");
                
                rMainPanel = new JPanel();
                rMainPanel.add(infoField);
                rMainPanel.add(rPathField);
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
        
        
        
        
        
    }

