package com.comp2042;

import java.util.ArrayList;
import java.util.List;

/** Data transfer object containing all visual information sent from the model to the GUI */

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final List<int[][]> nextBricksData;
    private final int[][] heldBrickData;
    private final int ghostYPosition;

    public ViewData(int[][] brickData, int xPosition, int yPosition,
                    List<int[][]> nextBricksData, int[][] heldBrickData, int ghostYPosition) {
        this.brickData = MatrixOperations.copy(brickData);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        if (nextBricksData == null) {
            this.nextBricksData = new ArrayList<>();
        } else {
            List<int[][]> copy = new ArrayList<>();
            for (int[][] m : nextBricksData) {
                copy.add(m == null ? null : MatrixOperations.copy(m));
            }
            this.nextBricksData = copy;
        }
        this.heldBrickData = heldBrickData == null ? null : MatrixOperations.copy(heldBrickData);
        this.ghostYPosition = ghostYPosition;
    }

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int[][] heldBrickData, int ghostYPosition) {
        this(brickData, xPosition, yPosition, toList(nextBrickData), heldBrickData, ghostYPosition);
    }

    private static List<int[][]> toList(int[][] next) {
        List<int[][]> l = new ArrayList<>();
        l.add(next);
        l.add(null);
        l.add(null);
        return l;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public List<int[][]> getNextBricksData() {
        List<int[][]> copy = new ArrayList<>();
        for (int[][] m : nextBricksData) {
            copy.add(m == null ? null : MatrixOperations.copy(m));
        }
        return copy;
    }

    public int[][] getHeldBrickData() {
        return heldBrickData == null ? null : MatrixOperations.copy(heldBrickData);
    }

    public int getGhostYPosition() {
        return ghostYPosition;
    }
}
