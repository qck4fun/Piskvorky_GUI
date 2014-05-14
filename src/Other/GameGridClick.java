package Other;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map.Entry;

import javax.swing.JLabel;

public class GameGridClick implements MouseListener {

    private final GameGridMap gameGridMap;
    private final Connection connection;
    private static Point key;

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
                connection.addToOutput(102 + " " + key.x + "," + key.y);
                System.out.println(102 + " " + key.x + "," + key.y);
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
