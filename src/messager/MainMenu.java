/*
Den här filen har huvudmenyn och relaterade submenyer i sig. 
Basically bara massa swing komponenter, men den interagerar lite med client och
server klasserna när man skall connecta osv.
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
    
    /*
    Swing sakerna här för easy access
    */
    JFrame mainFrame; // mainpanel sitter på mainframe
    JPanel mainPanel; // ALLT sitter på mainpanel
    JPanel buttonPanel; 
    
    JButton connectButton;
    JButton hostButton;
    JButton closeButton;
    
    public MainMenu(){

        /*
        Allt skapas och trycks in i rätt behållare här.
        Actionlisteners definieras direkt i knapparna.
        */
        mainFrame = new JFrame();
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3,1));
        
        connectButton = new JButton("Connect to a server");
        connectButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                // skapar en ny connectmeny
                ConnectMenu newCMenu = new ConnectMenu();
            }
            
        
    });
        
        
        hostButton = new JButton("Host a server");
        hostButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //skapar en ny host meny
                HostMenu newMenu = new HostMenu();
            }
            
        
    });
        
        
        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    // stänger programmet
                    mainFrame.dispose();
                }
            }
            );
        
        /*
        allt trycks in
        */
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
        /*
        menyn som kommer upp när man vill connecta till en server
        */
        JFrame cMainFrame; // mainpanel sitter på mainframe
        JPanel cMainPanel; // ALLT sitter på mainpanel
        JTextField adressBar;
        JTextField cNameBar;
        JTextField portBar;
        JButton cConnectButton;
        JButton cCloseButton;
        
        public ConnectMenu(){
            
            /*
            All swing skapas. Actionlisteners definieras inne i knapparna så 
            att säga.
            */
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
                /*
                Gör om input datan till information för connecten,
                skapar sedan en ny klient och ansluter.
                
                TODO FELHANTERING
                
                */
                int port = Integer.parseInt(portBar.getText());
                String name = cNameBar.getText();
                String adress = adressBar.getText();

                Client client = new Client(false);
                client.setName(name);
                client.getConnection(adress, port);
                cMainFrame.dispose();
                
            }});
            
            cCloseButton = new JButton("Close");
            cCloseButton.addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    // dödar menyn
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
        
        /*
        Menyn som kommer upp när man vill hosta. Actionlisteners def i knapparna
        
        */
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
                /*
                Tar input datan och sätter upp en server med den. Skapar 
                sedan en klient i admin läge och ansluter denne till servern.
                Klienten sätts sen som admin i servern, tag en titt på dem
                klasserna för o se hur de håller koll på varandra och använder
                varandra.
                
                TODO FELHANTERING
                
                */
                int port = Integer.parseInt(adressBar.getText());
                String name = nameBar.getText();
                ServerMultipart server = new ServerMultipart();
                server.setPort(port);
                server.startServer();
                
                Client client = new Client(true);
                client.setName(name);
                client.setServer(server);
                client.getConnection("localhost", port);
                server.setOwner(client);
                
                hMainFrame.dispose();
                
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
