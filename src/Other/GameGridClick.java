package Other;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GameGridClick implements MouseListener {
	
	private GameGridMap gameGridMap;
	private static Point key;

	public GameGridClick(GameGridMap gameGridMap) {
		this.gameGridMap = gameGridMap;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		key = null;

		for (Entry<Point, JLabel> blabla : gameGridMap.getGameGridMap().entrySet()) {
			if (blabla.getValue().equals(e.getComponent())) {
				key = blabla.getKey();
				gameGridMap.getGameGridMap().get(key).setIcon(new ImageIcon("/home/adam/workspace/Piskvorky_GUI/src/img/cross.png"));
				gameGridMap.getGameGridMap().get(key).repaint();
				break;
			}
		}
		//System.out.println("Sloupec " + key.getX() + " Řádek " + key.getY());
		//System.out.println("Sloupec " + key.x + " Řádek " + key.y);
		System.out.println(GameGridClick.getClickCoordinates().x + " " + GameGridClick.getClickCoordinates().y);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static Point getClickCoordinates() {
		return key;
	}
}
