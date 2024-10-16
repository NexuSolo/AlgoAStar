package astaralgo;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

import astaralgo.enums.BlockType;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class GameMapPanel extends JPanel {
    private GameMap gameMap;
    private boolean isMousePressed = false;

    public GameMapPanel(GameMap gameMap) {
        this.gameMap = gameMap;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameMap.isAlgorithmRunning()) {
                    return; // Ignorer les clics si l'algorithme est en cours
                }
                isMousePressed = true;
                placeElement(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isMousePressed = false;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameMap.isAlgorithmRunning()) {
                    return; // Ignorer les clics si l'algorithme est en cours
                }
                placeElement(e);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isMousePressed) {
                    placeElement(e);
                }
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
                        if (!gameMap.isAlgorithmRunning()) {
                            gameMap.setAlgorithmRunning(true);
                            gameMap.findPath();
                        } else {
                            if (gameMap.hasPlayerReachedDestination()) {
                                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(GameMapPanel.this);
                                topFrame.dispose();
                            } else {
                                gameMap.advancePlayer();
                            }
                        }
                        repaint();
                    }
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    private void placeElement(MouseEvent e) {
        int cellSize = 10; // Taille de chaque carré
        int col = e.getX() / cellSize;
        int row = e.getY() / cellSize;
        gameMap.placeElement(row, col, gameMap.getCurrentPlacementType());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Block[][] board = gameMap.getBoard();
        int cellSize = 10; // Taille de chaque carré
        Font font = new Font("Arial", Font.PLAIN, 10);

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Block block = board[row][col];
                if (block.getType() == BlockType.PLAYER) {
                    g.setColor(Color.RED);
                } else if (block.getType() == BlockType.DESTINATION) {
                    g.setColor(Color.GREEN);
                } else if (gameMap.isAlgorithmRunning() && gameMap.isNodeInPath(row, col)) {
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(block.getType().getColor());
                }
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);

                if (gameMap.isAlgorithmRunning() && gameMap.isNodeInPath(row, col)) {
                    Node node = gameMap.getNodeAt(row, col);
                    if (node != null) {
                        g.setFont(font);
                        g.drawString(String.valueOf(node.getH()), col * cellSize + 2, row * cellSize + 12); // Distance par rapport à la destination
                        g.drawString(String.valueOf(node.getG()), col * cellSize + cellSize - 12, row * cellSize + 12); // Distance par rapport au joueur
                        g.drawString(String.valueOf(node.getF()), col * cellSize + 2, row * cellSize + cellSize - 2); // Somme des deux distances
                    }
                }
            }
        }
    }
}