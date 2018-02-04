/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt_gui;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author maxoliveberg
 */
public class MainMenu{
    
    JFrame mainFrame; // mainpanel sitter på mainframe
    JPanel mainPanel; // ALLT sitter på mainpanel
    JPanel buttonPanel; 
    
    JButton connectButton;
    JButton hostButton;
    JButton closeButton;
    
    public MainMenu(){

        
        mainFrame = new JFrame();
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3,1));
        
        connectButton = new JButton("Connect to a server");
        connectButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectMenu newCMenu = new ConnectMenu();
            }
            
        
    });
        
        
        hostButton = new JButton("Host a server");
        hostButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                HostMenu newhMenu = new HostMenu();
            }
            
        
    });
        
        
        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainFrame.dispose();
                }
            }
            );
        
        mainPanel.add(connectButton);
        mainPanel.add(hostButton);
        mainPanel.add(closeButton);
        
        mainFrame.add(mainPanel);
        mainFrame.setSize(300,150);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
    }
    
    class ConnectMenu{
        JFrame cMainFrame; // mainpanel sitter på mainframe
        JPanel cMainPanel; // ALLT sitter på mainpanel
        JTextField adressBar;
        JButton cConnectButton;
        JButton cCloseButton;
        
        public ConnectMenu(){
            
            cMainFrame = new JFrame();
            
            cMainPanel = new JPanel();
            cMainPanel.setLayout(new GridLayout(3,1));
            
            
            adressBar = new JTextField("Please enter IP:PORT*");
            
            cConnectButton = new JButton("Connect*");
            
            cCloseButton = new JButton("Close");
            cCloseButton.addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    cMainFrame.dispose();
                }
            }
            );
            
            cMainPanel.add(adressBar);
            cMainPanel.add(cConnectButton);
            cMainPanel.add(cCloseButton);
            
            cMainFrame.add(cMainPanel);
            cMainFrame.setSize(300,150);
            cMainFrame.pack();
            cMainFrame.setVisible(true);
            
            
            
        }
    } 
    
    class HostMenu{
        JFrame hMainFrame; // mainpanel sitter på mainframe
        JPanel hMainPanel; // ALLT sitter på mainpanel
        JTextField adressBar;
        JButton hConnectButton;
        JButton hCloseButton;
        
        public HostMenu(){
            
            hMainFrame = new JFrame();
            
            hMainPanel = new JPanel();
            hMainPanel.setLayout(new GridLayout(3,1));
            
            
            adressBar = new JTextField("Please enter PORT*");
            
            hConnectButton = new JButton("Connect*");
            
            hCloseButton = new JButton("Close");
            hCloseButton.addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    hMainFrame.dispose();
                }
            }
            );
            
            hMainPanel.add(adressBar);
            hMainPanel.add(hConnectButton);
            hMainPanel.add(hCloseButton);
            
            hMainFrame.add(hMainPanel);
            hMainFrame.setSize(300,150);
            hMainFrame.pack();
            hMainFrame.setVisible(true);
            
            
            
        }
    
    
}
    
    
}
