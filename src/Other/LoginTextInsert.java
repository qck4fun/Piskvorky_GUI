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
 * Třída pracující se vstupem od uživatele při přihlašování do hry.
 * 
 * @author Adam Žák
 */
public class LoginTextInsert implements ActionListener {
    
    private final MainWindow mainWindow;
    
    private final Connection connection;
    
    /**
     * Konstruktor třídy předávající odkaz na instanci třídy MainWindow
     * a Connection.
     * 
     * @param mainWindow odkaz na instanci třídy MainWindow
     * @param connection odkaz na instanci třídy Connection
     */
    public LoginTextInsert(MainWindow mainWindow, Connection connection) {
        this.mainWindow = mainWindow;
        this.connection = connection;
    }

    /**
     * Metoda posílající vstup uživatele při přihlašování serveru
     * 
     * @param e informace o ActionEventu
     */
    @Override
    public void actionPerformed(ActionEvent e) {
            String userName = e.getActionCommand();
            connection.addToOutput(101 + " " + userName);
    }
}
