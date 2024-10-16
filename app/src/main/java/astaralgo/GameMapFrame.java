package astaralgo;

import javax.swing.JFrame;

class GameMapFrame extends JFrame {
    public GameMapFrame(GameMap gameMap) {
        setTitle("Game Map");
        GameMapPanel panel = new GameMapPanel(gameMap);
        add(panel);
    }
}