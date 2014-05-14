/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Other;

import GUI.MainWindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author adam
 */
public class LoginTextInsert implements ActionListener {
    
    private final MainWindow mainWindow;
    
    private final Connection connection;
    
    public LoginTextInsert(MainWindow mainWindow, Connection connection) {
        this.mainWindow = mainWindow;
        this.connection = connection;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!(e.getActionCommand().equals(""))) {
            String userName = e.getActionCommand();
            connection.addToOutput(101 + " " + userName);
            userName = null;
        } else {
            JOptionPane.showMessageDialog(mainWindow, "Zadej uživatelské jméno!", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }
}
