package GUI;

import Other.Connection;
import Other.GameGridClick;
import Other.GameGridMap;
import Other.LoginTextInsert;
import Other.Protocol;
import java.awt.BorderLayout;
import java.awt.Color;
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

    private final GameGridMap gameGridMap;

    private final Connection connection;
    
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
        setResizable(false);
        setVisible(true);
        
        mainPanel = new JPanel();

        infoPanel = new JPanel(new BorderLayout());
        gridPanel = new JPanel();

        login = new JLabel("Zadej přihlašovací jméno: ");
        loginText = new JTextField(20);
        loginText.addActionListener(new LoginTextInsert(this, connection));

        infoPanel.add(login, BorderLayout.WEST);
        infoPanel.add(loginText, BorderLayout.EAST);

        progressBar = new JProgressBar();
        text = new JLabel("Vyčkejte na připojení dalšího hrače");
        
        myIcon = new ImageIcon("/home/adam/Google Drive/vše/4. semestr/klient server aplikace v javě/1. semestrální práce/Piskvorky_GUI/src/img/cross.png");
        enemyIcon = new ImageIcon("/home/adam/Google Drive/vše/4. semestr/klient server aplikace v javě/1. semestrální práce/Piskvorky_GUI/src/img/circle.png");
        
        turnText = new JLabel("NEHRAJEŠ");
        turnText.setForeground(Color.RED);

        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.SOUTH);
        
        add(mainPanel);

        pack();
    }

    private void createGameGrid() {
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                Point coordinates = new Point(col, row);
                // JLabel label = new JLabel(col + "x" + row);
                JLabel label = new JLabel(
                        new ImageIcon("/home/adam/Google Drive/vše/4. semestr/klient server aplikace v javě/1. semestrální práce/Piskvorky_GUI/src/img/blank.png"));
                label.addMouseListener(new GameGridClick(gameGridMap, connection));
                label.setBorder(BorderFactory.createLineBorder(null));
                gameGridMap.getGameGridMap().put(coordinates, label);
                gridPanel.add(label);
            }
        }
    }

    public void createMainWindow() {
        setSize(525, 580);
        
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
        infoPanel.remove(turnText);
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
        String protocolNum = Protocol.extractProtocolNum(incMsg);
        Icon icon = null;

        switch (protocolNum) {
            case "612":
                icon = myIcon;
                break;
            case "611":
                icon = enemyIcon;
                break;
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
    
    public void gameEnd(String protocolNum) {
        String errorText = null;

        switch (protocolNum) {
            case "620":
                errorText = "Vyhrál jsi. Chceš si zahrát ještě jednou?";
                break;
            case "621":
                errorText = "Prohrál jsi. Chceš si zahrát ještě jednou?";
                break;
            case "622":
                errorText = "Remíza! Neuvěřitelné. Chceš si zahrát ještě jednou?";
                break;
        }
        int result = JOptionPane.showOptionDialog(this, errorText, "Konec hry", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if(result == 0) {
            connection.addToOutput("103");
        }
        else if( result == 1) {
            System.exit(0);
        }
    }
    
    public void gameReset() {
        gridPanel.removeAll();
        gameReadyWait();
    }
}