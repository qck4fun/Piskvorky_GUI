package Main;

import javax.swing.SwingUtilities;

import GUI.MainWindow;
import Other.Connection;

public class Main {

    public static void main(String[] args) {

        Connection connection = new Connection();
        new Thread(connection).start();
        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainWindow(connection);
            }
        });
    }
}
