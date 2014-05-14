package GUI;

import Other.Connection;
import Other.GameGridClick;
import Other.GameGridMap;
import Other.LoginTextInsert;
import Other.Protocol;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.Icon;
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

    private Connection connection;
    
    private JPanel mainPanel;
    
    private JPanel infoPanel;

    private JPanel gridPanel;

    private JLabel login;

    private JTextField loginText;

    private JProgressBar progressBar;
    
    private JLabel turnText;

    private JLabel text;

    private Icon myIcon;

    private Icon enemyIcon;

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
        
        mainPanel = new JPanel(new BorderLayout());

        infoPanel = new JPanel(new BorderLayout(20, 0));
        gridPanel = new JPanel();

        login = new JLabel("Zadej přihlašovací jméno: ");
        loginText = new JTextField(20);
        loginText.addActionListener(new LoginTextInsert(this, connection));

        infoPanel.add(login, BorderLayout.WEST);
        infoPanel.add(loginText, BorderLayout.EAST);

        progressBar = new JProgressBar();
        text = new JLabel(" Vyčkejte na připojení dalšího hrače");
        
        myIcon = new ImageIcon("/home/adam/Google Drive/vše/4. semestr/klient server aplikace v javě/1. semestrální práce/Piskvorky_GUI/src/img/cross.png");
        enemyIcon = new ImageIcon("/home/adam/Google Drive/vše/4. semestr/klient server aplikace v javě/1. semestrální práce/Piskvorky_GUI/src/img/circle.png");
        
        turnText = new JLabel("blabla");

        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.SOUTH);
        
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
                label.addMouseListener(new GameGridClick(gameGridMap, connection));
                // label.setText(col + "x" + row);
                label.setBorder(BorderFactory.createLineBorder(null));
                gameGridMap.getGameGridMap().put(coordinates, label);
                gridPanel.add(label);
            }
        }
    }

    public void createMainWindow() {
        setSize(550, 570);
        
        infoPanel.add(turnText, BorderLayout.CENTER);
        
        gridPanel.setLayout(new GridLayout(0, 16));
        createGameGrid();
        

        validate();
        repaint();
    }

    public void createLoginErrorWindow() {
        JOptionPane.showMessageDialog(this, "Došlo k chybě při přihlašování.\nZkuste to prosím znovu.", "Přihlašovací chyba", JOptionPane.ERROR_MESSAGE);
    }

    public void gameReadyWait() {
        infoPanel.remove(login);
        infoPanel.remove(loginText);
        setSize(260, 80);
        progressBar.setIndeterminate(true);
        if (connection.getGameReady() == true) {
            infoPanel.remove(progressBar);
            infoPanel.remove(text);
            createMainWindow();
        } else {
            infoPanel.add(progressBar, BorderLayout.NORTH);
            infoPanel.add(text);
        }

        validate();
        repaint();
    }

    public void paintIcon(String incMsg) {
        int protocolNum = Integer.valueOf(Protocol.extractProtocolNum(incMsg));

        Icon icon = null;

        if (protocolNum == 612) {
            icon = myIcon;
        } else if (protocolNum == 611) {
            icon = enemyIcon;
        }

        String messageBody = Protocol.extractMessageBody(incMsg);
        String[] msgBodyArray = messageBody.split("\\,");
        Point key = new Point(Integer.valueOf(msgBodyArray[0]), Integer.valueOf(msgBodyArray[1]));
        gameGridMap.getGameGridMap().get(key).setIcon(icon);
        gameGridMap.getGameGridMap().get(key).repaint();
    }

    public JLabel getTurnText() {
        return turnText;
    }
}
    /*
    public void paintMyIcon(String messageBody) {
        String[] msgBodyArray = messageBody.split("\\,");
        Point key = new Point(Integer.valueOf(msgBodyArray[0]), Integer.valueOf(msgBodyArray[1]));
        gameGridMap.getGameGridMap().get(key).setIcon(myIcon);
        gameGridMap.getGameGridMap().get(key).repaint();
    }

    public void paintEnemyIcon(String messageBody) {
        String[] msgBodyArray = messageBody.split("\\,");
        Point key = new Point(Integer.valueOf(msgBodyArray[0]), Integer.valueOf(msgBodyArray[1]));
        gameGridMap.getGameGridMap().get(key).setIcon(enemyIcon);
        gameGridMap.getGameGridMap().get(key).repaint();
    }
    */
