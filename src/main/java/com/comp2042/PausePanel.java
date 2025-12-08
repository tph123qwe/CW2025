package com.comp2042;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** custom JavaFx VBox component representing the Pause Menu. Strictly a view component and only responsible for displaying the Paused label and the 2 Resume and Restart buttons. It forwards all button actions back to the GuiController.*/

public class PausePanel extends VBox {

    private GuiController controller;

    public PausePanel() {
        setSpacing(12);
        setAlignment(Pos.CENTER);

        Label pauseLabel = new Label("Paused");
        pauseLabel.setStyle("gameOverStyle");

        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(e -> {
            if (controller != null) {
                controller.resumeFromPause();
            }
        });

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> {
            if (controller != null) {
                controller.newGame(null);
            }
        });

        getChildren().addAll(pauseLabel, resumeButton, restartButton);
        setVisible(false);
    }

    /** Sets the reference to the main {@link com.comp2042.GuiController}. This allows the Resume and Restart Buttons to call the appropriate methods. @param controller The main GUI controller instance.*/

    public void setController(GuiController controller) {
        this.controller = controller;
    }
}
