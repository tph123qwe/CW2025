package com.comp2042;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int[][] heldBrickData;
    private final int ghostYPosition;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        this(brickData, xPosition, yPosition, nextBrickData, null, yPosition);
    }

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int[][] heldBrickData) {
        this(brickData, xPosition, yPosition, nextBrickData, heldBrickData, yPosition);
    }

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData,
                    int[][] heldBrickData, int ghostYPosition) {
        this.brickData = MatrixOperations.copy(brickData);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData == null ? null : MatrixOperations.copy(nextBrickData);
        this.heldBrickData = heldBrickData == null ? null : MatrixOperations.copy(heldBrickData);
        this.ghostYPosition = ghostYPosition;
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

    public int[][] getNextBrickData() {
        return nextBrickData == null ? null : MatrixOperations.copy(nextBrickData);
    }

    public int[][] getHeldBrickData() {
        return heldBrickData == null ? null : MatrixOperations.copy(heldBrickData);
    }

    public int getGhostYPosition() {
        return ghostYPosition;
    }
}
