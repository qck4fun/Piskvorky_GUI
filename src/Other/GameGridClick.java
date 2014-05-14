package Other;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GameGridClick implements MouseListener {

    private GameGridMap gameGridMap;
    private Connection connection;
    private static Point key;
    private ImageIcon icon;

    public GameGridClick(GameGridMap gameGridMap, Connection connection) {
        this.gameGridMap = gameGridMap;
        this.connection = connection;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        key = null;

        for (Entry<Point, JLabel> blabla : gameGridMap.getGameGridMap().entrySet()) {
            if (blabla.getValue().equals(e.getComponent())) {
                key = blabla.getKey();
                gameGridMap.getGameGridMap().get(key).setIcon(new ImageIcon("/home/adam/Google Drive/vše/4. semestr/klient server aplikace v javě/1. semestrální práce/Piskvorky_GUI/src/img/cross.png"));
                gameGridMap.getGameGridMap().get(key).repaint();
                connection.addToOutput(102 + " " + GameGridClick.getClickCoordinates().x + "," + GameGridClick.getClickCoordinates().y);
                System.out.println(102 + " " + GameGridClick.getClickCoordinates().x + "," + GameGridClick.getClickCoordinates().y);
                break;
            }
        }
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

    private static Point getClickCoordinates() {
        return key;
    }
}
