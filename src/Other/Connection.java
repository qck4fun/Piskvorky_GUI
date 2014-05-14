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
    private String messageBody;
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
                        case "601":
                            gameReady = false;
                            mainWindow.gameReadyWait();
                            serverResponse = "přihlášen";
                            break;
                        case "602":
                            mainWindow.createLoginErrorWindow();
                            serverResponse = "nepřihlášen";
                            break;
                        case "603":
                            gameReady = true;
                            mainWindow.gameReadyWait();
                            serverResponse = "game ready";
                            break;
                        case "610":
                            serverResponse = "místo obsazeno";
                            break;
                        case "611":
                            mainWindow.paintIcon(incMsg);
                            serverResponse = "jeho značka umístěna";
                            break;
                        case "612":
                            mainWindow.paintIcon(incMsg);
                            serverResponse = "tvoje značka umístěna";
                            break;
                        case "614":
                            mainWindow.getTurnText().setForeground(Color.RED);
                            mainWindow.getTurnText().setText("NEHRAJEŠ");
                            mainWindow.setEnabled(false);
                            serverResponse = "nehraješ";
                            break;
                        case "615":
                            mainWindow.getTurnText().setForeground(Color.BLUE);
                            mainWindow.getTurnText().setText("HRAJEŠ");
                            mainWindow.setEnabled(true);
                            serverResponse = "hraješ";
                            break;
                        case "620":
                            mainWindow.gameEnd(protocolNum);
                            serverResponse = "výhra";
                            break;
                        case "621":
                            mainWindow.gameEnd(protocolNum);
                            serverResponse = "prohra";
                            break;
                        case "622":
                            mainWindow.gameEnd(protocolNum);
                            serverResponse = "remíza";
                            break;
                        case "700":
                            mainWindow.regame();
                            serverResponse = "nová hra";
                            break;
                        case "701":
                            mainWindow.opponentDisconnected();
                            serverResponse = "spoluhráč se odpojil";
                            break;
                            
                        default:
                            serverResponse = "0";
                            break;
                    }
                    System.out.println(serverResponse);
                } else {
                    socket.close();
                    done = true;
                    System.out.println("konec komunikace");
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
}
