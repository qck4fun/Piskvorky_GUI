package Other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;

import GUI.MainWindow;

public final class Connection implements Runnable {

	public static final int PORT = 1099;

	private MainWindow mainWindow;

	private InetAddress address;
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	
	private boolean done;
	private static String incMsg;

	public Connection(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		setInputAndOutput();
		startTheGame();
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

	private void startTheGame() {
		output.println(101 + " Adam");
		//output.println(101 + " " + mainWindow.getLoginText());
	}

	@Override
	public void run() {
		while (!done) {
			try {			
				incMsg = input.readLine();
				if(incMsg!=null) {								
					String ProtocolNum = Protocol.extractProtocolNum(incMsg);
					
					String blabla = null;
					
					switch (ProtocolNum) {
		            case "601":
		                blabla = "přihlášen";
		                break;
		            case "602":
		                blabla = "nepřihlášen";
		                break;
		            case "603":
		                blabla = "game ready";
		                break;
		            default: 
		                blabla = "0";
		                break;
					}
			       System.out.println(blabla);
		        }
				else {
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
}
