package astaralgo.enums;

import java.awt.Color;

public enum BlockType {
    EMPTY(0, Color.WHITE),
    FULL(1, Color.BLACK),
    PLAYER(3, Color.RED),
    DESTINATION(4, Color.GREEN);

    private final int value;
    private final Color color;

    BlockType(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }
}