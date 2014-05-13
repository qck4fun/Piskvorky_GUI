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
    public static String userName;
    
    private MainWindow mainWindow;

    public LoginTextInsert(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!(e.getActionCommand().equals(""))) {
            userName = e.getActionCommand();
            new Thread(new Connection(mainWindow)).start();
            mainWindow.createMainWindow();
        } else {
            JOptionPane.showMessageDialog(null, "Zadej uživatelské jméno!", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }
}
