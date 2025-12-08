package com.comp2042;

/** Final data class used by the {@link com.comp2042.GameController} to communicate the results of a down move back to the {@link GuiController}. It keeps all information necessary for the view to update, including row clearing status and updated board data.*/

public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /** The result of any rows cleared during the move, encapsulated in a {@link com.comp2042.ClearRow} object.*/

    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }
}
