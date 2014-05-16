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
import java.net.URL;
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

    private boolean firstGame;

    public MainWindow(Connection connection) {
        this.connection = connection;
        connection.setMainWindow(this);
        gameGridMap = new GameGridMap();
        init();
        firstGame = true;
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
        progressBar.setIndeterminate(true);
        text = new JLabel("Vyčkejte na připojení dalšího hrače");

        URL myIconUrl = getClass().getClassLoader().getResource("img/cross.png");
        URL enemyIconUrl = getClass().getClassLoader().getResource("img/circle.png");

        myIcon = new ImageIcon(myIconUrl);
        enemyIcon = new ImageIcon(enemyIconUrl);

        turnText = new JLabel("NEHRAJEŠ");
        turnText.setForeground(Color.RED);

        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.SOUTH);

        add(mainPanel);

        pack();
    }

    private void createGameGrid() {
        URL background = getClass().getClassLoader().getResource("img/blank.png");
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                Point coordinates = new Point(col, row);
                JLabel label = new JLabel(new ImageIcon(background));
                label.addMouseListener(new GameGridClick(gameGridMap, connection));
                label.setBorder(BorderFactory.createLineBorder(null));
                gameGridMap.getGameGridMap().put(coordinates, label);
                gridPanel.add(label);
            }
        }
    }

    public void repaintMainWindow() {
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

    public void initiateGame(boolean gameReady) {
        if (firstGame == true) {
            infoPanel.remove(login);
            infoPanel.remove(loginText);
        }
        gameReadyWait(gameReady);
        validate();
        repaint();
    }

    private void gameReadyWait(boolean gameReady) {
        setSize(260, 80);
        infoPanel.add(progressBar, BorderLayout.NORTH);
        infoPanel.add(text);
        if (gameReady == true) {
            infoPanel.remove(progressBar);
            infoPanel.remove(text);
            repaintMainWindow();
        }
    }

    public void metoda(boolean gameReady) {
        if (gameReady == true) {
            infoPanel.remove(login);
            infoPanel.remove(loginText);
        }
    }

    public void paintIcon(String incMsg) {
        String protocolNum = Protocol.extractProtocolNum(incMsg);
        Icon icon = null;
        switch (protocolNum) {
            case Protocol.YOUR_MARK_PLACED:
                icon = myIcon;
                break;
            case Protocol.ENEMY_MARK_PLACED:
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
            case Protocol.WIN:
                errorText = "Vyhrál jsi. Chceš si zahrát ještě jednou?";
                break;
            case Protocol.LOSS:
                errorText = "Prohrál jsi. Chceš si zahrát ještě jednou?";
                break;
            case Protocol.DRAW:
                errorText = "Remíza! Neuvěřitelné. Chceš si zahrát ještě jednou?";
                break;
        }
        int result = JOptionPane.showOptionDialog(this, errorText, "Konec hry", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (result == 0) {
            int resultOption = JOptionPane.showOptionDialog(this, "Chceš si zahrát proti tomu samému spoluhráči nebo proti jinému?\n Ano: stejný\nNe: jiný", "Nová hra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (resultOption == 0) {
                firstGame = false;
                connection.addToOutput("103");
            } else {
                freshNewGame();
            }
        } else {
            System.exit(0);
        }
    }

    public void opponentDisconnected() {
        int result = JOptionPane.showOptionDialog(this, "Spoluhráč se odpojil. Chceš hru spustit znovu?\nPokud ne, program bude ukončen.", "Chyba", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
        if (result == 0) {
            freshNewGame();
        } else {
            System.exit(0);
        }
    }

    public void regame() {
        infoPanel.removeAll();
        gridPanel.removeAll();
        initiateGame(firstGame);
    }

    public void freshNewGame() {
        dispose();
        connection.socketClose();
        connection = new Connection();
        new Thread(connection).start();
        new MainWindow(connection);
    }

    public void positionOccupied() {
        JOptionPane.showMessageDialog(this, "Toto místo je již obsazeno!", "Chyba", JOptionPane.ERROR_MESSAGE);
    }
}
