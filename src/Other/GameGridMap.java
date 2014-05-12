package Other;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

public class GameGridMap {

	private Map<Point, JLabel> gameGridMap;
	
	public GameGridMap() {
		this.gameGridMap = new HashMap<Point, JLabel>();
	}
	
	public Map<Point, JLabel> getGameGridMap() {
		return this.gameGridMap;
	}
}
