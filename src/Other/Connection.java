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

public final class Connection implements Runnable {

    public static final int PORT = 1099;

    private MainWindow mainWindow;

    private InetAddress address;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private boolean done;
    private static String incMsg;
    private String ProtocolNum;
    private boolean gameReady;

    /**
     *
     */
    public Connection() {
        setInputAndOutput();
    }

    private void setInputAndOutput() {
        try {
            address = InetAddress.getByName("127.0.1.1");
            socket = new Socket(address, PORT);

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
                    ProtocolNum = Protocol.extractProtocolNum(incMsg);

                    String serverResponse = null;

                    switch (ProtocolNum) {
                        case "601":
                            //mainWindow.createMainWindow();
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
                            serverResponse = "jeho značka umístěna";
                            break;
                        case "612":
                            serverResponse = "tvoje značka umístěna";
                            break;
                        case "614":
                            serverResponse = "nehraješ";
                            break;
                        case "615":
                            serverResponse = "hraješ";
                            break;
                        case "620":
                            serverResponse = "výhra";
                            break;
                        case "621":
                            serverResponse = "prohra";
                            break;
                        case "622":
                            serverResponse = "remíza";
                            break;
                        case "700":
                            serverResponse = "nová hra";
                            break;
                        case "103":
                            serverResponse = "regame";
                            break;
                            
                        default:
                            serverResponse = "0";
                            break;
                    }
                    System.out.println(serverResponse);
                    //System.out.println(incMsg);
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

    public String getProtocolNum() {
        return ProtocolNum;
    }
    
    public boolean getGameReady() {
        return gameReady;
    }
}
