package astaralgo;

import astaralgo.enums.BlockType;

public class Block {
    private BlockType type;

    public Block(BlockType type) {
        this.type = type;
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public int getValue() {
        return type.getValue();
    }
}