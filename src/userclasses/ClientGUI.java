package userclasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame {

    private JTextField userTypeBox;
    private JTextArea chatHistoryBox;

    public ClientGUI() {
        super("IM chat");

        userTypeBox = new JTextField();
        userTypeBox.setEditable(false);
        userTypeBox.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //translateAndSendMessage(e.getActionCommand()); TA MEDELANDE OCH GÖR NÅGOT! SKICKA TILL CONTROLLER?!
                        userTypeBox.setText("");
                    }
                }
        );
        this.add(userTypeBox, BorderLayout.SOUTH);

        chatHistoryBox = new JTextArea();
        JScrollPane myWindowWithScroll = new JScrollPane(chatHistoryBox);
        this.add(myWindowWithScroll);

        this.setSize(500,300);
        this.setVisible(true);
    }

    private void showMessage(final String message) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chatHistoryBox.append(message);
                    }
                }
        );
    }
}
