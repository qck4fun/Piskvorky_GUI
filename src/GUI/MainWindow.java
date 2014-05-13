package GUI;

import Other.Connection;
import Other.GameGridClick;
import Other.GameGridMap;
import Other.LoginTextInsert;
import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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

    private Connection connection;

    public MainWindow(Connection connection) {
        this.connection = connection;
        connection.setMainWindow(this);
        gameGridMap = new GameGridMap();
        init();
    }

    private void init() {
        setTitle("Piškvorky");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        mainPanel = new JPanel();

        login = new JLabel("Zadej přihlašovací jméno: ");
        loginText = new JTextField(20);
        loginText.addActionListener(new LoginTextInsert(this, connection));

        mainPanel.add(login);
        mainPanel.add(loginText);

        add(mainPanel);

        pack();

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
                                "/home/adam/Google Drive/vše/4. semestr/klient server aplikace v javě/1. semestrální práce/Piskvorky_GUI/src/img/blank.png"));
                label.addMouseListener(new GameGridClick(gameGridMap));
                // label.setText(col + "x" + row);
                label.setBorder(BorderFactory.createLineBorder(null));
                gameGridMap.getGameGridMap().put(coordinates, label);
                mainPanel.add(label);
            }
        }
    }

    public void createMainWindow() {
        mainPanel.remove(login);
        mainPanel.remove(loginText);
        setSize(550, 550);
        mainPanel.setLayout(new GridLayout(0, 16));

        createGameGrid();
        validate();
        repaint();
    }
    
    public void createLoginErrorWindow() {
        JOptionPane.showMessageDialog(this, "Došlo k chybě při přihlašování.\nZkuste to prosím znovu.", "Přihlašovací chyba", JOptionPane.ERROR_MESSAGE);
    }
    
    public void gameReadyWait() {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        mainPanel.add(progressBar);
        
        validate();
        repaint();
    }
}
