package Main;

import javax.swing.SwingUtilities;

import GUI.MainWindow;
import Other.Connection;

public class Main {
	
	private static MainWindow mainWindow;
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				mainWindow = new MainWindow();
			}
		});
		new Thread(new Connection(mainWindow)).start();
	}
}
