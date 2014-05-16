package Other;

import GUI.MainWindow;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Connection implements Runnable {

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

    /**
     *
     */
    public Connection() {
        setConnection();
        setInputAndOutput();
    }
    
    public void setConnection() {    
        try {
            address = InetAddress.getByName("127.0.1.1");
            socket = new Socket(address, PORT);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setInputAndOutput() {
        try {
            input = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), "UTF-8"));

            output = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream(), "UTF-8"), true);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!done) {
            try {
                incMsg = input.readLine();
                if (incMsg != null) {
                    protocolNum = Protocol.extractProtocolNum(incMsg);

                    String serverResponse = null;

                    switch (protocolNum) {
                        case Protocol.LOGIN_SUCCESS:
                            gameReady = false;
                            mainWindow.gameReadyWait();
                            break;
                        case Protocol.LOGIN_FAIL:
                            mainWindow.createLoginErrorWindow();
                            break;
                        case Protocol.GAME_READY:
                            gameReady = true;
                            mainWindow.gameReadyWait();
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
                            mainWindow.getTurnText().setForeground(Color.RED);
                            mainWindow.getTurnText().setText("NEHRAJEŠ");
                            mainWindow.setEnabled(false);
                            break;
                        case Protocol.YOUR_TURN:
                            mainWindow.getTurnText().setForeground(Color.BLUE);
                            mainWindow.getTurnText().setText("HRAJEŠ");
                            mainWindow.setEnabled(true);
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
                        case Protocol.GENERAL_FAIL:
                            //TODO dodělat general fail case
                            break;
                            
                        default:
                            serverResponse = "0";
                            //TODO dodělat default case
                            break;
                    }
                    System.out.println(serverResponse);
                    //smazat až bude hotovo
                } else {
                    socket.close();
                    done = true;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public synchronized void addToOutput(String message) {
        output.println(message);
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
    
    public boolean getGameReady() {
        return gameReady;
    }
    
    public void socketClose() {
        try {
            done = true;
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
