package astaralgo;

public class Node implements Comparable<Node> {
    private int row;
    private int col;
    private int g; // Coût depuis le départ
    private int h; // Coût estimé jusqu'à la destination
    private Node parent;
    private boolean isPath; // Indique si le nœud fait partie du chemin

    public Node(int row, int col, int g, int h, Node parent) {
        this.row = row;
        this.col = col;
        this.g = g;
        this.h = h;
        this.parent = parent;
        this.isPath = false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }

    public int getF() {
        return g + h;
    }

    public Node getParent() {
        return parent;
    }

    public boolean isPath() {
        return isPath;
    }

    public void setPath(boolean isPath) {
        this.isPath = isPath;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.getF(), other.getF());
    }
}