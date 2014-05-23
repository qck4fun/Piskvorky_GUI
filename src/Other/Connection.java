package Other;

import GUI.MainWindow;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * Třída, která se stará o veškerou komunikaci se serverem a následné
 * volání metod na překreslování hlavního okna aplikace.
 * 
 * @author Adam Žák
 */
public final class Connection implements Runnable {

    /**
     * Port, na který se klient připojuje
     */
    public static final int PORT = 1099;

    private MainWindow mainWindow;

    private InetAddress address;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private boolean done;
    private static String incMsg;
    private String protocolNum;
    private boolean gameReady;
    private boolean yourTurn;

    /**
     * Konstruktor třídy Connection.
     */
    public Connection() {
        setConnection();
        setInputAndOutput();
    }

    /**
     * Privátní metoda nastavující připojení k serveru.
     */
    private void setConnection() { 
        try {
            address = InetAddress.getByName("127.0.1.1");
            socket = new Socket(address, PORT);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Došlo k chybě při navazování spojení se serverem!", "Chyba", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Privátní metoda nastavující vstup a výstup.
     */
    private void setInputAndOutput() {
        try {
            input = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), "UTF-8"));

            output = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream(), "UTF-8"), true);
        } catch (UnsupportedEncodingException e) {
            JOptionPane.showMessageDialog(null, "Klientem použitá znaková sada není serverem podporována!", "Chyba", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Došlo k chybě při nastavování vstupu a výstupu!", "Chyba", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Metoda slouží ke čtení zpráv ze serveru, interpretaci příchozích zpráv a 
     * volání příslušných metod.
     */
    @Override
    public void run() {
        while (!done) {
            try {
                incMsg = input.readLine();
                if (incMsg != null) {
                    protocolNum = Protocol.extractProtocolNum(incMsg);

                    switch (protocolNum) {
                        case Protocol.LOGIN_SUCCESS:
                            gameReady = false;
                            mainWindow.initiateGame(gameReady);
                            break;
                        case Protocol.LOGIN_FAIL:
                            mainWindow.createLoginErrorWindow();
                            break;
                        case Protocol.GAME_READY:
                            gameReady = true;
                            mainWindow.initiateGame(gameReady);
                            break;
                        case Protocol.POSITION_OCCUPIED:
                            mainWindow.positionOccupied();
                            break;
                        case Protocol.ENEMY_MARK_PLACED:
                            mainWindow.paintIcon(incMsg);
                            break;
                        case Protocol.YOUR_MARK_PLACED:
                            mainWindow.paintIcon(incMsg);
                            break;
                        case Protocol.NOT_YOUR_TURN:
                            yourTurn = false;
                            mainWindow.isYourTurn(yourTurn);
                            break;
                        case Protocol.YOUR_TURN:
                            yourTurn = true;
                            mainWindow.isYourTurn(yourTurn);
                            break;
                        case Protocol.WIN:
                            mainWindow.gameEnd(protocolNum);
                            break;
                        case Protocol.LOSS:
                            mainWindow.gameEnd(protocolNum);
                            break;
                        case Protocol.DRAW:
                            mainWindow.gameEnd(protocolNum);
                            break;
                        case Protocol.NEW_GAME:
                            mainWindow.regame();
                            break;
                        case Protocol.OPPONENT_LEFT:
                            mainWindow.opponentDisconnected();
                            break;
                    }
                } else {
                    socket.close();
                    done = true;
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Došlo k chybě na vstupu. Hra bude ukončena.", "Chyba", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }
    
    /**
     * Metoda, která přidává zadaný parametr do výstupu aplikace.
     * 
     * @param message zpráva 
     */
    public synchronized void addToOutput(String message) {
        output.println(message);
    }
    
    /**
     * Metoda nastavující odkaz na instanci třídy MainWindow.
     * 
     * @param mainWindow odkaz na instanci třídy MainWindow
     */
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * Metoda sloužící k uzavření socketu.
     */
    public void socketClose() {
        try {
            done = true;
            socket.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Došlo k chybě při uzavírání socketu!", "Chyba", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    
    /**
     * Metoda vracející odkaz na boolovský datový atribut gameReady.
     * 
     * @return odkaz na boolovský datový atribut gameReady
     */
    public boolean getGameReady() {
        return gameReady;
    }
    
}