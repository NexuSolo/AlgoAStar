package astaralgo;

import javax.swing.JFrame;

class GameMapFrame extends JFrame {
    public GameMapFrame(GameMapPanel panel) {
        setTitle("Game Map");
        add(panel);
    }
}