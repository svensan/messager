/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


/**
 *
 * @author maxoliveberg
 */
public class ChatWindow {
    JFrame mainFrame; // mainpanel sitter på mainframe
    JPanel mainPanel; // ALLT sitter på mainpanel
    
    JPanel buttonPanel; 
    
    JEditorPane chatWindow;
    JScrollPane chatScrollWindow;
    
    JButton closeButton;
    JButton exampleButton;
    
    JTextField textField;
    
    ChatWindow(){
        
        mainFrame = new JFrame();
        mainPanel = new JPanel(new GridBagLayout());
        
        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {mainFrame.dispose();}});
        
        exampleButton = new JButton("exempelknapp*");
        
        buttonPanel = new JPanel();
        GridBagConstraints buttonC = new GridBagConstraints();
        buttonC.gridx = 0;
        buttonC.gridy = 0;
        
        buttonPanel.add(closeButton);
        buttonPanel.add(exampleButton);
        
        chatWindow = new JEditorPane();
        chatWindow.setEditable(false);
        
        chatScrollWindow = new JScrollPane(chatWindow);
        chatScrollWindow.setPreferredSize(new Dimension(480,320));
        chatScrollWindow.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        GridBagConstraints scrollC = new GridBagConstraints();
        scrollC.gridx = 0;
        scrollC.gridy = 1;
        
        textField = new JTextField("type message...*",35);
        
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
}
