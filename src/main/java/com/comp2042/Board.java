package com.comp2042;

/** Defines the core logic for the Tetris game board, managing movement, state, and scoring. */

public interface Board {

    /**Attempts to move the current brick down 1 block. @return {@code true} if collision occurs*/

    boolean moveBrickDown();

    /**Attempts to move the current brick left 1 block. @return {@code true} if collision occurs*/

    boolean moveBrickLeft();

    /**Attempts to move the current brick right 1 block. @return {@code true} if collision occurs*/

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    /**Spawns a new brick. @return {@code true} if the new brick immediately collies, game over will initiate.*/

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    /**Checks for and removes any full rows. @return A {@link ClearRow} object with results*/

    ClearRow clearRows();

    Score getScore();

    void newGame();

    /**Swaps the current brick with the held brick. @return {@code true} if the swap results in an immediate collision. */

    boolean holdCurrentBrick();
}
