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

/**
 * Třída reprezentující veškerou grafiku hry Piškvorky
 * 
 * @author Adam Žák
 */
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
    
    /**
     * Konstruktor třídy, který předává odkaz na třídu Connection, dále
     * vytváří mapu hracího pole a inicializuje grafické kompomenty
     * aplikace.
     * 
     * @param connection odkaz na instanci třídy Connection
     */
    public MainWindow(Connection connection) {
        this.connection = connection;
        connection.setMainWindow(this);
        gameGridMap = new GameGridMap();
        init();
        firstGame = true;
    }

    /**
     * Privátní metoda vytvářející úvodní přihlašovací okno aplikace.
     */
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

    /**
     * Privátní metoda, která vytváří hrací pole hry.
     */
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
    
    /**
     * Metoda slouží k vykreslení hracího pole do hlavního okna aplikace.
     */
    public void repaintMainWindow() {
        setSize(525, 580);
        infoPanel.add(turnText, BorderLayout.CENTER);
        gridPanel.setLayout(new GridLayout(0, 16));
        createGameGrid();
        validate();
        repaint();
    }

    /**
     * Metoda vyhazuje dialogové okno v případě špatného přihlašovacího vstupu
     * uživatele.
     */
    public void createLoginErrorWindow() {
        JOptionPane.showMessageDialog(this, "Došlo k chybě při přihlašování.\nZkuste to prosím znovu.", "Přihlašovací chyba", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Metoda, která v případě, že jde o první hru dvou hráčů vymaže obsah 
     * přihlašovacího okna a poté zavolá privátní metodu gameReadyWait.
     * 
     * @param gameReady true pokud je hra připravena; slouží pro předání 
     *                   privátní metodě gameReadyWait
     */
    public void initiateGame(boolean gameReady) {
        if (firstGame == true) {
            infoPanel.remove(login);
            infoPanel.remove(loginText);
        }
        gameReadyWait(gameReady);
        validate();
        repaint();
    }

    /**
     * Privátní metoda, která vykreslí do okna načítací bar pokud hra není 
     * připravena.Pokud je hra připravena tento bar odstraní a volá metodu
     * {@link #repaintMainWindow() repaintMainWindow}.
     * 
     * @param gameReady true pokud je hra připravena
     */
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

    /**
     * Metoda slouží k přiřazení obrázku křížku či kolečka se kterým 
     * hráč hraje.Implicitně má lokální hráč křížek a síťový spoluhráč
     * kolečko.Následně tento obrázek vykreslí na souřadnice, které jsou
     * součástí zprávy od serveru.
     *  
     * @param incMsg příchozí zpráva od serveru s kterou metoda pracuje
     */
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

    /**
     * Metoda vyhodnocuje zda je hráč na tahu a podle toho píše nad 
     * hrací poli informaci.
     * 
     * @param yourTurn true pokud je hráč na řadě
     */
    public void isYourTurn(boolean yourTurn) {
        if (yourTurn == true) {
            turnText.setForeground(Color.BLUE);
            turnText.setText("HRAJEŠ");
            setEnabled(true);
        } else {
            turnText.setForeground(Color.RED);
            turnText.setText("NEHRAJEŠ");
            setEnabled(false);
        }
    }

    /**
     * Metoda vyhodnocuje na základě číselných zpráv od serveru zda je 
     * konec hry.Hra končí výhrou, prohrou nebo remízou - při každém z
     * těchto scénářů vyskočí dialogové okno, které se ptá jestli si
     * hráč přeje hrát dále nebo ne.Pokud ne tak aplikace končí.Pokud
     * ano tak se objeví další okno s dotazem jestli chce hrát s tím 
     * samým hráčem nebo s jiným(nový start hry).
     * 
     * @param protocolNum číslo protokolu přijaté serverem
     */
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

    /**
     * Metoda vyhazující dialogové okno pokud se jeden z hráčů 
     * odpojil.Nabízí možnost hru ukončit nebo ji spustit znovu. 
     */
    public void opponentDisconnected() {
        int result = JOptionPane.showOptionDialog(this, "Spoluhráč se odpojil. Chceš hru spustit znovu?\nPokud ne, program bude ukončen.", "Chyba", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
        if (result == 0) {
            freshNewGame();
        } else {
            System.exit(0);
        }
    }

    /**
     * Metoda smaže veškerý obsah hlavního okna a následně zavolá metodu
     * {@link #initiateGame(boolean) initiateGame}.
     */
    public void regame() {
        infoPanel.removeAll();
        gridPanel.removeAll();
        initiateGame(connection.getGameReady());
    }

    /**
     * Metoda schová hlavní okno původní aplikace, uzavře socket a 
     * vytvoří novou instanci připojení a grafiky hry.
     */
    public void freshNewGame() {
        dispose();
        connection.socketClose();
        connection = new Connection();
        new Thread(connection).start();
        new MainWindow(connection);
    }

    /**
     * Metoda vyhodí chybovou hlášku když hráč chce umístit svůj symbol
     * na místo kde už nějaký je.
     */
    public void positionOccupied() {
        JOptionPane.showMessageDialog(this, "Toto místo je již obsazeno!", "Chyba", JOptionPane.ERROR_MESSAGE);
    }
}
