package com.comp2042;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameOverPanel extends VBox {

    private GuiController controller;

    public GameOverPanel() {
        setSpacing(20);
        setAlignment(Pos.CENTER);

        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setStyle("gameOverStyle");

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> {
            if (controller != null) {
                controller.newGame(null);
            }
        });

        getChildren().addAll(gameOverLabel, restartButton);
        setVisible(false);

    }
        public void setController(GuiController controller) {
        this.controller = controller;

    }
}
