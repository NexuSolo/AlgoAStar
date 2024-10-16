package astaralgo;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import astaralgo.enums.BlockType;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class GameMap {
    private Block[][] board;
    private BlockType currentPlacementType = BlockType.PLAYER;
    private int[] playerPosition = null;
    private int[] destinationPosition = null;
    private boolean isAlgorithmRunning = false; // Drapeau pour indiquer si l'algorithme est en cours
    private List<Node> path = new ArrayList<>(); // Chemin trouvé par l'algorithme A*
    private int currentStep = 0; // Étape actuelle du chemin

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

    public void findPath() {
        if (playerPosition == null || destinationPosition == null) {
            System.out.println("Le joueur ou la destination n'est pas placé.");
            return;
        }

        long startTime = System.nanoTime(); // Début de la mesure du temps

        PriorityQueue<Node> openList = new PriorityQueue<>();
        Set<Node> closedList = new HashSet<>();

        Node startNode = new Node(playerPosition[0], playerPosition[1], 0, calculateHeuristic(playerPosition[0], playerPosition[1]), null);
        openList.add(startNode);
        startNode.setInOpenList(true);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            closedList.add(currentNode);

            if (currentNode.getRow() == destinationPosition[0] && currentNode.getCol() == destinationPosition[1]) {
                reconstructPath(currentNode);
                long endTime = System.nanoTime(); // Fin de la mesure du temps
                System.out.println("Chemin trouvé en " + (endTime - startTime) + " ns");
                return;
            }

            for (Node neighbor : getNeighbors(currentNode)) {
                if (closedList.contains(neighbor)) {
                    continue;
                }

                if (!neighbor.isInOpenList()) {
                    openList.add(neighbor);
                    neighbor.setInOpenList(true);
                } else {
                    for (Node openNode : openList) {
                        if (openNode.equals(neighbor) && neighbor.getG() < openNode.getG()) {
                            openList.remove(openNode);
                            openList.add(neighbor);
                            break;
                        }
                    }
                }
            }
        }

        long endTime = System.nanoTime(); // Fin de la mesure du temps
        System.out.println("Aucun chemin trouvé. Temps écoulé: " + (endTime - startTime) + " ns");
    }

    private int calculateHeuristic(int row, int col) {
        return Math.abs(row - destinationPosition[0]) + Math.abs(col - destinationPosition[1]);
    }

    private Set<Node> getNeighbors(Node node) {
        Set<Node> neighbors = new HashSet<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] direction : directions) {
            int newRow = node.getRow() + direction[0];
            int newCol = node.getCol() + direction[1];

            if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length) {
                if (board[newRow][newCol].getType() == BlockType.FULL) {
                    continue;
                }
                int g = node.getG() + 1;
                int h = calculateHeuristic(newRow, newCol);
                neighbors.add(new Node(newRow, newCol, g, h, node));
            }
        }

        return neighbors;
    }

    private void reconstructPath(Node node) {
        path.clear();
        while (node != null) {
            node.setPath(true); // Marquer le nœud comme faisant partie du chemin
            path.add(0, node); // Ajouter le nœud au début de la liste
            node = node.getParent();
        }
        currentStep = 0;
    }

    public List<Node> getPath() {
        return path;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void advancePlayer() {
        if (currentStep < path.size()) {
            Node node = path.get(currentStep);
            if (currentStep > 0) {
                Node prevNode = path.get(currentStep - 1);
                board[prevNode.getRow()][prevNode.getCol()] = new Block(BlockType.EMPTY);
            }
            board[node.getRow()][node.getCol()] = new Block(BlockType.PLAYER);
            currentStep++;
        }
    }

    public boolean isNodeInPath(int row, int col) {
        return path.stream().anyMatch(node -> node.getRow() == row && node.getCol() == col);
    }

    public boolean hasPlayerReachedDestination() {
        return currentStep > 0 && currentStep == path.size();
    }

    public Node getNodeAt(int row, int col) {
        return path.stream().filter(node -> node.getRow() == row && node.getCol() == col).findFirst().orElse(null);
    }

    public static void main(String[] args) {
        GameMap gameMap = new GameMap(10, 10);

        SwingUtilities.invokeLater(() -> {
            GameMapFrame frame = new GameMapFrame(gameMap);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setVisible(true);
        });
    }
}