package astaralgo;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameMapPanel gameMapPanel = new GameMapPanel();
            GameMap gameMap = new GameMap(75, 140, gameMapPanel);
            gameMapPanel.setGameMap(gameMap);
            GameMapFrame frame = new GameMapFrame(gameMapPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1500, 1000);
            frame.setVisible(true);
        });
    }

}
