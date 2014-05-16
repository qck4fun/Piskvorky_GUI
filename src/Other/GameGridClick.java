package Other;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map.Entry;

import javax.swing.JLabel;

public class GameGridClick implements MouseListener {

    private final GameGridMap gameGridMap;
    private final Connection connection;
    private Point key;

    public GameGridClick(GameGridMap gameGridMap, Connection connection) {
        this.gameGridMap = gameGridMap;
        this.connection = connection;
    }

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
}
