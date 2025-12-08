package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

import java.util.ArrayList;
import java.util.List;

/** Implementation of {@link com.comp2042.Board}. Manages the game grid, movement, collision and piece generation.*/

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    //Stores held brick and prevents hold spamming

    private Brick heldBrick = null;
    private boolean holdUsed = false; //prevents spamming hold per piece

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        // uses bag system for fairer piece distribution
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        //initializes score tracker
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }


    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 2); // changed block spawning higher
        holdUsed = false; //Allows hold function to be used for the new piece
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() { //ghost block calculation, returns ViewData including ghost Y position for display
        int ghostY = (int) currentOffset.getY();
        int[][] currentShape = brickRotator.getCurrentShape();
        int[][] boardCopy = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        while (true) { //simulate dropping piece down until it hits something
            p = new Point(p);
            p.translate(0, 1);
            boolean conflict = MatrixOperations.intersect(boardCopy, currentShape, (int) p.getX(), (int) p.getY());
            if (conflict) {
                break;
            } else {
                ghostY = (int) p.getY();
            }
        }
        // gets the matrix of held piece to display and null if there is nothing
        int[][] heldMatrix = null;
        if (heldBrick != null) {
            heldMatrix = heldBrick.getShapeMatrix().get(0);
        }

        //get matrices for the next 2 pieces
        List<int[][]> nextMatrices = new ArrayList<>();
        if (brickGenerator instanceof RandomBrickGenerator) {
            nextMatrices = ((RandomBrickGenerator) brickGenerator).getNextBricksMatrices();
        } else {
            int[][] next = brickGenerator.getNextBrick() == null ? null : brickGenerator.getNextBrick().getShapeMatrix().get(0);
            nextMatrices.add(next);
            nextMatrices.add(null);
            nextMatrices.add(null);
        }
        return new ViewData(currentShape, (int) currentOffset.getX(),
                (int) currentOffset.getY(), nextMatrices, heldMatrix, ghostY);
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    public Score getScore() {
        return score;
    }

    //resets all game state when new game starts
    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        heldBrick = null;
        holdUsed = false;
        createNewBrick();
    }
    //swaps current piece with held piece when C is pressed
    //can only be used once per piece
    @Override
    public boolean holdCurrentBrick() {
        if (holdUsed) {
            return false;
        }

        Brick current = brickRotator.getBrick();
        if (current == null) {
            return false;
        }

        if (heldBrick == null) {
            heldBrick = current;
            boolean gameOver = createNewBrick();
            holdUsed = true;
            return gameOver;
        } else {
            Brick temp = heldBrick;
            heldBrick = current;
            brickRotator.setBrick(temp);
            currentOffset = new Point(4, 0);
            holdUsed = true;
            return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        }
    }
}
