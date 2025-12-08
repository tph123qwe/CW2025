package com.comp2042.logic.bricks;

/** Defines the contract for generating new Tetris pieces*/

public interface BrickGenerator {

    /** Retrieves and consumes the next brick in the queue. @return The next {@link com.comp2042.logic.bricks.Brick}. */

    Brick getBrick();

    Brick getNextBrick();
}
