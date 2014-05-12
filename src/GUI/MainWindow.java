package GUI;

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

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GameGridMap gameGridMap;

	private JPanel mainPanel;

	private String loginText;

	public MainWindow() {
		gameGridMap = new GameGridMap();
		createLoginWindow();
		init();
		createGameGrid();
	}

	private void init() {
		setTitle("Piškvorky");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 500);
		setVisible(true);

		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 16));
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
		loginText = JOptionPane.showInputDialog("Přihlaš se:");
		while (loginText.equals("")) {
				JOptionPane.showMessageDialog(null, "Vyplň uživatelské jméno",
						"Přihlašovací chyba", JOptionPane.ERROR_MESSAGE);
			loginText = JOptionPane.showInputDialog("Přihlaš se:");
		}
		//System.out.println(loginText);
	}

	public String getLoginText() {
		return loginText;
	}
}