package astaralgo;

public class Node implements Comparable<Node> {
    private int row;
    private int col;
    private int g; // Coût depuis le départ
    private int h; // Coût estimé jusqu'à la destination
    private Node parent;
    private boolean isPath; // Indique si le nœud fait partie du chemin
    private boolean inOpenList; // Indique si le nœud est dans la liste ouverte

    public Node(int row, int col, int g, int h, Node parent) {
        this.row = row;
        this.col = col;
        this.g = g;
        this.h = h;
        this.parent = parent;
        this.isPath = false;       
        this.inOpenList = false;
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

    public boolean isInOpenList() {
        return inOpenList;
    }

    public void setInOpenList(boolean inOpenList) {
        this.inOpenList = inOpenList;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.getF(), other.getF());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return row == node.row && col == node.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}