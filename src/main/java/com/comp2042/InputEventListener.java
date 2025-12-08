package com.comp2042;

/** Handles all input events from the GUI, acting as the bridge to the game logic.*/

public interface InputEventListener {

    /**Handles soft drop/auto-move down. @param event The move details. @return A {@link com.comp2042.DownData} object with new state and row clear info.*/

    DownData onDownEvent(MoveEvent event);

    /** Handles move left event. @param event The move details. @return The updated {@link com.comp2042.ViewData}.*/

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    DownData onHardDropEvent(MoveEvent event);

    ViewData onHoldEvent(MoveEvent event);

    void createNewGame();
}
