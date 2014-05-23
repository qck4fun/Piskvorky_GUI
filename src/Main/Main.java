package Main;

import javax.swing.SwingUtilities;

import GUI.MainWindow;
import Other.Connection;

/**
 * Třída sloužící ke spustění grafického klienta Piškvorek.
 * 
 * @author Adam Žák
 */
public class Main {
    
    /**
     * Spustí vlákno s instancí třídy Connection a vytvoří hlavní okno 
     * aplikace.
     * 
     * @param args ignored
     */
    public static void main(String[] args) {

        final Connection connection = new Connection();
        new Thread(connection).start();
        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainWindow(connection);
            }
        });
    }
}
