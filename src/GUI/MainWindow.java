package GUI;

import Other.Connection;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Other.GameGridClick;
import Other.GameGridMap;
import Other.LoginTextInsert;
import java.awt.BorderLayout;
import javax.swing.JTextField;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GameGridMap gameGridMap;

	private JPanel mainPanel;
        
        private JLabel login;

	private JTextField loginText;

	public MainWindow() {
		gameGridMap = new GameGridMap();
		init();
                createLoginWindow();
		//createGameGrid();
	}

	private void init() {
		setTitle("Piškvorky");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setSize(500, 500);
		setVisible(true);

		mainPanel = new JPanel();
		//mainPanel.setLayout(new GridLayout(0, 16));
                
		add(mainPanel);
		
		/*
		 * for (Point blabla : gameGridMap.getGameGridMap().keySet()) {
		 * 
		 * System.out.println(blabla.getX() + " " + blabla.getY()); }
		 */
	}

	private void createGameGrid() {
		for (int row = 0; row < 16; row++) {
			for (int col = 0; col < 16; col++) {
				Point coordinates = new Point(col, row);
				// JLabel label = new JLabel(col + "x" + row);
				JLabel label = new JLabel(
						new ImageIcon(
								"/home/adam/workspace/Piskvorky_GUI/src/img/blank.png"));
				label.addMouseListener(new GameGridClick(gameGridMap));
				// label.setText(col + "x" + row);
				label.setBorder(BorderFactory.createLineBorder(null));
				gameGridMap.getGameGridMap().put(coordinates, label);
				mainPanel.add(label);
			}
		}
	}

	private void createLoginWindow() {
            login = new JLabel("Zadej přihlašovací jméno: ");
            loginText = new JTextField(20);
            loginText.addActionListener(new LoginTextInsert(this));
            mainPanel.add(login);
            mainPanel.add(loginText);
            pack();
            
            
//            loginText = JOptionPane.showInputDialog("Přihlaš se:");
//		while (loginText.equals("")) {
//				JOptionPane.showMessageDialog(null, "Vyplň uživatelské jméno",
//						"Přihlašovací chyba", JOptionPane.ERROR_MESSAGE);
//			loginText = JOptionPane.showInputDialog("Přihlaš se:");
//		}
		//System.out.println(loginText);
	}
        
        public void createMainWindow() {
            mainPanel.remove(login);
            mainPanel.remove(loginText);
            setSize(500, 500);
            mainPanel.setLayout(new GridLayout(0, 16));
            
            
        }

	public JTextField getLoginText() {
		return loginText;
	}
}