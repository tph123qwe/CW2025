package com.comp2042.logic.bricks;

import java.util.List;

/** DEfines the contract for all seven Tetris bricks.*/

public interface Brick {

    /** Gets a list of matrices representing all rotation states. @return A list of 2D integer arrays.*/

    List<int[][]> getShapeMatrix();
}
