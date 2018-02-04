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
    
    JTextField textField;
    
    Client user;
    
    ChatWindow(Client creator){
        
        user = creator;
        
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
        
        buttonPanel = new JPanel();
        GridBagConstraints buttonC = new GridBagConstraints();
        buttonC.gridx = 0;
        buttonC.gridy = 0;
        
        buttonPanel.add(closeButton);
        buttonPanel.add(sendFileButton);
        
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
            JTextField recipientField;
            JTextField messageField;
            JTextField portField;
            
            public SendWindow(boolean isHost){
                
                sMainPanel = new JPanel();
                sMainPanel.setLayout(new GridLayout(5,1));
                if(isHost == true){
                    recipientField = new JTextField("Recipient...",15);
                    sMainPanel.add(recipientField);
                }
                portField = new JTextField("Port...",18);
                sMainPanel.add(portField);
                
                messageField = new JTextField("Message...");
                sMainPanel.add(messageField);
                
                sSendButton = new JButton("Send*");
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
        
        
        
        
        
    }

