package com.comp2042;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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

    public void setController(GuiController controller) {
        this.controller = controller;
    }
}
