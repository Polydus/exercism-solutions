package com.polydus.exercism.queenattack;

import static java.lang.Math.abs;

public class QueenAttackCalculator {

    Queen q1;
    Queen q2;

    public QueenAttackCalculator(Queen q1, Queen q2) throws IllegalArgumentException {
        if(q1 == null || q2 == null) throw new IllegalArgumentException("You must supply valid positions for both Queens.");
        int dx = q1.getX() - q2.getX();
        int dy = q1.getY() - q2.getY();
        if(dx == 0 && dy == 0) throw new IllegalArgumentException("Queens cannot occupy the same position.");
        this.q1 = q1;
        this.q2 = q2;
    }

    public boolean canQueensAttackOneAnother() {

        //In the game of chess, a queen can attack pieces which are on the same row, column, or diagonal.
        int dx = abs(q1.getX() - q2.getX());
        int dy = abs(q1.getY() - q2.getY());


        if(dx == 0) return true; //horizontal
        if(dy == 0) return true; //vertical
        if(dx == dy) return true; //diagonal

        return false;
    }


    public static class Queen {

        private int x = 0;
        private int y = 0;


        public Queen(int x, int y) throws RuntimeException {
            if (x < 0) throw new IllegalArgumentException("QueenAttackCalculator.Queen position must have positive row.");
            if (x > 7) throw new IllegalArgumentException("QueenAttackCalculator.Queen position must have row <= 7.");
            if (y < 0) throw new IllegalArgumentException("QueenAttackCalculator.Queen position must have positive column.");
            if (y > 7) throw new IllegalArgumentException("QueenAttackCalculator.Queen position must have column <= 7.");

            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

    }


}

