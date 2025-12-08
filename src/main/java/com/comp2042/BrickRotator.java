package com.comp2042;

import com.comp2042.logic.bricks.Brick;

/** Manages the rotation state of the current brick. Keeps track of the current rotation index and calculates the shape of the next rotation.*/

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /** Calculates the shape matrix and rotation index of the bricka after one clockwise rotation. @return NextShapeInfo containing the rotated matrix and the new index.*/

    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }

    public Brick getBrick() {
        return this.brick;
    }


}
