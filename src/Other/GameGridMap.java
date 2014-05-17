package Other;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

/**
 * Třída vytvářející mapu s herním polem.
 * 
 * @author Adam Žák
 */
public class GameGridMap {

	private Map<Point, JLabel> gameGridMap;
	
	/**
         * Konstruktor třídy GameGridMap vytvářející HashMapu s klíčem 
         * typu Point a hodnotou JLabel.
         */
        public GameGridMap() {
		this.gameGridMap = new HashMap<Point, JLabel>();
	}
	
        /**
         * Metoda vracející odkaz na istanci této třídy.
         * 
         * @return odkaz na instanci mapy
         */
	public Map<Point, JLabel> getGameGridMap() {
		return this.gameGridMap;
	}
}
