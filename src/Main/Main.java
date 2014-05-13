package Main;

import javax.swing.SwingUtilities;

import GUI.MainWindow;
import Other.Connection;

public class Main {
    
    private static Connection connection;

    public static void main(String[] args) {

        connection = new Connection();
        new Thread(connection).start();
        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainWindow(connection);
            }
        });
        
        //new Thread(new Connection()).start();
    }
}
