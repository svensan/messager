/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messager;

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
                HostMenu newMenu = new HostMenu();
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
        JTextField cNameBar;
        JTextField portBar;
        JButton cConnectButton;
        JButton cCloseButton;
        
        public ConnectMenu(){
            
            cMainFrame = new JFrame();
            
            cMainPanel = new JPanel();
            cMainPanel.setLayout(new GridLayout(3,1));
            
            
            adressBar = new JTextField("Please enter IP");
            portBar = new JTextField("Please enter port");
            cNameBar = new JTextField("Please enter a name");
            
            cConnectButton = new JButton("Connect*");
            cConnectButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(portBar.getText());
                String name = cNameBar.getText();
                String adress = adressBar.getText();

                Client client = new Client(false);
                client.setName(name);
                client.getConnection(adress, port);
                
            }});
            
            cCloseButton = new JButton("Close");
            cCloseButton.addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    cMainFrame.dispose();
                }
            }
            );
            
            cMainPanel.add(cNameBar);
            cMainPanel.add(adressBar);
            cMainPanel.add(portBar);
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
        JTextField nameBar;
        JButton hConnectButton;
        JButton hCloseButton;
        
        public HostMenu(){
            
            hMainFrame = new JFrame();
            
            hMainPanel = new JPanel();
            hMainPanel.setLayout(new GridLayout(3,1));
            
            
            adressBar = new JTextField("Please enter PORT");
            nameBar = new JTextField("Please enter your name");
            
            hConnectButton = new JButton("Host");
            hConnectButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(adressBar.getText());
                String name = nameBar.getText();
                ServerMultipart server = new ServerMultipart();
                server.setPort(port);
                server.startServer();
                
                Client client = new Client(true);
                client.setName(name);
                client.setServer(server);
                client.getConnection("localhost", port);
                
            }});
            
            hCloseButton = new JButton("Close");
            hCloseButton.addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    hMainFrame.dispose();
                }
            }
            );
            
            hMainPanel.add(nameBar);
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
