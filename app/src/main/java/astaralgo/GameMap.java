package astaralgo;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import astaralgo.enums.BlockType;

public class GameMap {
    private Block[][] board;
    private BlockType currentPlacementType = BlockType.PLAYER;
    private int[] playerPosition = null;
    private int[] destinationPosition = null;
    private boolean isAlgorithmRunning = false; // Drapeau pour indiquer si l'algorithme est en cours

    public GameMap(int rows, int cols) {
        board = new Block[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = new Block(BlockType.EMPTY);
            }
        }
    }

    public void placeElement(int row, int col, BlockType type) {
        if (row >= 0 && row < board.length && col >= 0 && col < board[0].length) {
            if (type == BlockType.PLAYER) {
                if (playerPosition != null) {
                    // Réinitialiser l'ancienne position du joueur à EMPTY
                    board[playerPosition[0]][playerPosition[1]] = new Block(BlockType.EMPTY);
                }
                playerPosition = new int[]{row, col};
            } else if (type == BlockType.DESTINATION) {
                if (destinationPosition != null) {
                    // Réinitialiser l'ancienne position de la destination à EMPTY
                    board[destinationPosition[0]][destinationPosition[1]] = new Block(BlockType.EMPTY);
                }
                destinationPosition = new int[]{row, col};
            } else if (type == BlockType.FULL) {
                // Ne pas placer de mur sur le joueur ou la destination
                if ((playerPosition != null && playerPosition[0] == row && playerPosition[1] == col) ||
                    (destinationPosition != null && destinationPosition[0] == row && destinationPosition[1] == col)) {
                    System.out.println("Impossible de placer un mur sur le joueur ou la destination.");
                    return;
                }
            }
            board[row][col] = new Block(type);
        } else {
            System.out.println("Position invalide");
        }
    }

    public Block getElement(int row, int col) {
        if (row >= 0 && row < board.length && col >= 0 && col < board[0].length) {
            return board[row][col];
        } else {
            System.out.println("Position invalide");
            return null;
        }
    }

    public Block[][] getBoard() {
        return board;
    }

    public BlockType getCurrentPlacementType() {
        return currentPlacementType;
    }

    public void setCurrentPlacementType(BlockType currentPlacementType) {
        this.currentPlacementType = currentPlacementType;
    }

    public boolean isAlgorithmRunning() {
        return isAlgorithmRunning;
    }

    public void setAlgorithmRunning(boolean algorithmRunning) {
        isAlgorithmRunning = algorithmRunning;
    }

    public void displayBoard() {
        for (Block[] row : board) {
            for (Block block : row) {
                System.out.print(block.getValue() + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        GameMap gameMap = new GameMap(5, 5);

        SwingUtilities.invokeLater(() -> {
            GameMapFrame frame = new GameMapFrame(gameMap);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setVisible(true);
        });
    }
}