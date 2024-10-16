package astaralgo;

import javax.swing.JPanel;

import astaralgo.enums.BlockType;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class GameMapPanel extends JPanel {
    private GameMap gameMap;

    public GameMapPanel(GameMap gameMap) {
        this.gameMap = gameMap;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameMap.isAlgorithmRunning()) {
                    return; // Ignorer les clics si l'algorithme est en cours
                }
                int cellSize = 50; // Taille de chaque carré
                int col = e.getX() / cellSize;
                int row = e.getY() / cellSize;
                gameMap.placeElement(row, col, gameMap.getCurrentPlacementType());
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (gameMap.getCurrentPlacementType() == BlockType.PLAYER) {
                        gameMap.setCurrentPlacementType(BlockType.DESTINATION);
                    } else if (gameMap.getCurrentPlacementType() == BlockType.DESTINATION) {
                        gameMap.setCurrentPlacementType(BlockType.FULL);
                    } else if (gameMap.getCurrentPlacementType() == BlockType.FULL) {
                        // Passer à l'étape suivante (par exemple, lancer l'algorithme A*)
                        gameMap.setAlgorithmRunning(true);
                        System.out.println("Lancer l'algorithme A*");
                    }
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Block[][] board = gameMap.getBoard();
        int cellSize = 50; // Taille de chaque carré

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Block block = board[row][col];
                g.setColor(block.getType().getColor());
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
            }
        }
    }
}