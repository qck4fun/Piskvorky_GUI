package Other;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map.Entry;

import javax.swing.JLabel;

/**
 * Třída starající se o aktivitu hráčů na hracím poli - zjišťuje, kam 
 * hráči klikají.
 * 
 * @author Adam Žák
 */
public class GameGridClick implements MouseListener {

    private final GameGridMap gameGridMap;
    private final Connection connection;
    private Point key;

    /**
     * Konstruktor předávájí odkazy na třídy Connection a 
     * GameGridMap.
     * 
     * @param gameGridMap odkaz na instanci třídy GameGridMap
     * @param connection odkaz na instanci třídy Connection
     */
    public GameGridClick(GameGridMap gameGridMap, Connection connection) {
        this.gameGridMap = gameGridMap;
        this.connection = connection;
    }

    /**
     * Metoda, která zjišťuje souřadnice kliknutého políčka a ty pak přeposílá
     * tříde Connection ke zpracování.
     * 
     * @param e informace o kliknutí
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        key = null; 
        for (Entry<Point, JLabel> coordinate : gameGridMap.getGameGridMap().entrySet()) {
            if (coordinate.getValue().equals(e.getComponent())) {
                key = coordinate.getKey();
                connection.addToOutput(102 + " " + key.x + "," + key.y);
                break;
            }
        }
    }
    
    /**
     * Nevyužitá abstraktní metoda třídy MouseListener.
     * 
     * @param e informace o klinutí
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    
    /**
     * Nevyužitá abstraktní metoda třídy MouseListener.
     * 
     * @param e informace o klinutí
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    /**
     * Nevyužitá abstraktní metoda třídy MouseListener.
     * 
     * @param e informace o klinutí
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }
    
    /**
     * Nevyužitá abstraktní metoda třídy MouseListener.
     * 
     * @param e informace o klinutí
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
