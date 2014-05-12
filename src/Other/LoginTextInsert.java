/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Other;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author adam
 */
public class LoginTextInsert implements ActionListener {
    
    public static boolean loggedIn = false;
    public static String userName;

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!(e.getActionCommand().equals(""))) {
            userName = e.getActionCommand();
            loggedIn = true;
        }
        else {
            JOptionPane.showMessageDialog(null, "Zadej uživatelské jméno!", "Chyba", JOptionPane.ERROR_MESSAGE);
            loggedIn = false;
        }
    }
}
