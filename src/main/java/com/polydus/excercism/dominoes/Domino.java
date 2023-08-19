package com.polydus.excercism.dominoes;

import java.util.Objects;

public class Domino {
    private int left;
    private int right;
    public Domino(int left, int right) {
        this.left = left;
        this.right = right;
    }
    
    public int getLeft() {
        return this.left;
    }
    
    public int getRight() {
        return this.right;
    }

    @Override
    public boolean equals(Object o) {
        Domino otherDomino = (Domino) o;
        return (this.getLeft() == otherDomino.getLeft() && this.getRight() == otherDomino.getRight()) ||
            (this.getLeft() == otherDomino.getRight() && this.getRight() == otherDomino.getLeft());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
