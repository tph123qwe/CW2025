package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
    private static final int PREVIEW_SIZE = 14;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private PausePanel pausePanel;

    //HUD for holding bricks and showing next bricks.

    @FXML
    private GridPane heldPanel;

    @FXML
    private GridPane next1Panel;

    @FXML
    private GridPane next2Panel;

    //Displays score

    @FXML
    private Label scoreLabel;

    @FXML
    private BorderPane gameBoard;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    //Timeline for soft drop

    private Timeline softDropTimeline;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private int[][] lastBoardMatrix;

    //Speed System, tracks the number of pieces placed and increases speed based on thqat value.

    private int piecesPlaced = 0;
    private static final int BASE_SPEED = 400; //Milliseconds
    private static final int SPEED_INCREASE_INTERVAL = 10; //Speed increases every 10 pieces
    private static final int MIN_SPEED = 100; //Max speed cap

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        if (gamePanel != null) {
            gamePanel.setFocusTraversable(true);
            gamePanel.requestFocus();
        }
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE) { //Pause Menu when pressing ESC key
                    togglePause();
                    keyEvent.consume();
                    return;
                }
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.C) { //Hold function when pressing C
                        moveHold(new MoveEvent(EventType.HOLD, EventSource.USER));
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) { //Soft drop function when pressing the Down or S Key
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        if (softDropTimeline == null) { //Timeline that triggers every 60 milliseconds when key is held
                            softDropTimeline = new Timeline(new KeyFrame(Duration.millis(60),
                                    ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.USER))));
                            softDropTimeline.setCycleCount(Timeline.INDEFINITE);
                        }
                        softDropTimeline.play();
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.SPACE) { //Hard drop function when pressing SPACE
                        moveHardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
            }
        });

        gamePanel.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) { // when Down or S key is released, stop Soft drop
                if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                    if (softDropTimeline != null) {
                        softDropTimeline.stop();
                    }
                    keyEvent.consume();
                }

            }
        });
        if (gameOverPanel != null) {
            gameOverPanel.setVisible(false);
            gameOverPanel.setController(this);
        }
        //Initialize pause menu that appears when esc key is pressed
        if (pausePanel != null) {
            pausePanel.setVisible(false);
            pausePanel.setController(this);
        }

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.getStyleClass().add("board-cell");
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangle.getStyleClass().add("brick");
                rectangle.setArcWidth(9);
                rectangle.setArcHeight(9);
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(gameBoard.getLayoutX() + 15 + brick.getxPosition() * (brickPanel.getVgap() + BRICK_SIZE));
        brickPanel.setLayoutY(gameBoard.getLayoutY() + 15 + (brick.getyPosition() - 2) * (brickPanel.getHgap() + BRICK_SIZE));

        this.lastBoardMatrix = MatrixOperations.copy(boardMatrix);
        refreshGameBackground(this.lastBoardMatrix);

        //Speed system implementation
        //Starts game at base speed
        updateGameSpeed();
        //Displays held piece and next 3 pieces on the side panel
        renderPreviews(brick);
    }
    //Speed System implementation
    //Speed increases every 10 pieces placed
    private void updateGameSpeed() {
        if (timeLine != null) {
            timeLine.stop();
        }
        //starts at 400ms and decreases by 20ms every 10 pieces
        int currentSpeed = Math.max(MIN_SPEED, BASE_SPEED - (piecesPlaced / SPEED_INCREASE_INTERVAL) * 20);

        timeLine = new Timeline(new KeyFrame(Duration.millis(currentSpeed),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }

    //Shows a grid for the side panel for the next pieces and hold function ( just for styling purposes)
    private void renderMiniGrid(GridPane pane, int[][] shape) {
        pane.getChildren().clear();
        if (shape == null) return;
        int rows = shape.length;
        int cols = shape[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Rectangle r = new Rectangle(PREVIEW_SIZE, PREVIEW_SIZE);
                r.getStyleClass().add("preview-cell");
                r.setArcHeight(4);
                r.setArcWidth(4);
                r.setFill(getFillColor(shape[i][j]));
                pane.add(r, j, i);
            }
        }
    }

    //Updates the Held Piece and the next 2 blocks
    private void renderPreviews(ViewData viewData) {
        if (viewData == null) return;
        renderMiniGrid(heldPanel, viewData.getHeldBrickData());
        List<int[][]> nextList = viewData.getNextBricksData();
        if (nextList.size() > 0) renderMiniGrid(next1Panel, nextList.get(0));
        if (nextList.size() > 1) renderMiniGrid(next2Panel, nextList.get(1));
    }

    //Ghost Block implementation
    private void refreshGhost(ViewData brick) {
        if (lastBoardMatrix == null) return;

        int[][] shape = brick.getBrickData();
        int baseX = brick.getxPosition();
        int ghostY = brick.getGhostYPosition();

        final double ghostAlpha = 0.35;

        //Draws ghost blocks where the brick will land
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                int cell = shape[i][j];
                if (cell == 0) continue;
                int boardX = baseX + j;
                int boardY = ghostY + i;
                if (boardY >= 0 && boardY < lastBoardMatrix.length && boardX >= 0 && boardX < lastBoardMatrix[boardY].length) {
                    if (lastBoardMatrix[boardY][boardX] == 0) {
                        Rectangle r = displayMatrix[boardY][boardX];
                        Paint base = getFillColor(cell);
                        if (base instanceof Color) {
                            Color c = (Color) base;
                            //Transparent version of the current piece
                            r.setFill(Color.color(c.getRed(), c.getGreen(), c.getBlue(), ghostAlpha));
                        } else {
                            r.setFill(base);
                        }
                    }
                }
            }
        }
    }

    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            if (lastBoardMatrix != null) {
                for (int i = 2; i < lastBoardMatrix.length; i++) {
                    for (int j = 0; j < lastBoardMatrix[i].length; j++) {
                        setRectangleData(lastBoardMatrix[i][j], displayMatrix[i][j]);
                    }
                }
            }
            //refreshes ghost brick
            refreshGhost(brick);

            brickPanel.setLayoutX(gameBoard.getLayoutX() + 15 + brick.getxPosition() * (brickPanel.getVgap() + BRICK_SIZE));
            brickPanel.setLayoutY(gameBoard.getLayoutY() + 15 + (brick.getyPosition() - 2) * (brickPanel.getHgap() + BRICK_SIZE));
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
            //refreshes blocks for the side panels
            renderPreviews(brick);
        }
        if (gamePanel != null) gamePanel.requestFocus();
    }

    public void refreshGameBackground(int[][] board) {
        this.lastBoardMatrix = MatrixOperations.copy(board);
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }
    //Hold function
    private void moveHold(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            ViewData viewData = eventListener.onHoldEvent(event);
            refreshBrick(viewData);
        }
        if (gamePanel != null) gamePanel.requestFocus();
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            //Scoring system display and notification
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            //increments counter when blocks are placed and updates the speed accordingly
            if (event.getEventSource() == EventSource.THREAD && downData.getClearRow() != null) {
                boolean pieceLanded = !eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD)).getViewData().equals(downData.getViewData());
                if (pieceLanded) {
                    piecesPlaced++;
                    if (piecesPlaced % SPEED_INCREASE_INTERVAL == 0) {
                        updateGameSpeed();
                    }
                }
            }

            refreshBrick(downData.getViewData());
        }
        if (gamePanel != null) gamePanel.requestFocus();
    }
    //Hard drop
    private void moveHardDrop(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onHardDropEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        if (pausePanel != null) gamePanel.requestFocus();
    }
    //Pause menu toggle
    private void togglePause() {
        if (isGameOver.getValue() == Boolean.TRUE) {
            return;
        }
        if (isPause.getValue() == Boolean.TRUE) {
            if (pausePanel != null) pausePanel.setVisible(false);
            if (timeLine != null) timeLine.play();
            isPause.setValue(Boolean.FALSE);
            if (softDropTimeline != null) {
                softDropTimeline.stop();
            }
            if (gamePanel != null) gamePanel.requestFocus();
        } else {
            if (pausePanel != null) pausePanel.setVisible(true);
            if (timeLine != null) timeLine.pause();
            if (softDropTimeline != null) {
                softDropTimeline.stop();
            }
            isPause.setValue(Boolean.TRUE);
        }
    }
    //resume from pause
    public void resumeFromPause() {
        if (pausePanel != null) pausePanel.setVisible(false);
        if (timeLine != null) timeLine.play();
        if (softDropTimeline != null) softDropTimeline.stop();
        isPause.setValue(Boolean.FALSE);
        if (gamePanel != null) gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }
    //bind score label to score property for automatic updates
    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null && integerProperty != null) {
            scoreLabel.textProperty().bind(integerProperty.asString());
        }
    }

    public void gameOver() {
        if (pausePanel != null) pausePanel.setVisible(false);
        if (softDropTimeline != null) softDropTimeline.stop();
        if (timeLine != null) timeLine.stop();
        if (gameOverPanel != null) gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }
    // restart function
    public void newGame(ActionEvent actionEvent) {
        if (softDropTimeline != null) {
            softDropTimeline.stop();
        }
        if (timeLine != null) timeLine.stop();
        if (gameOverPanel != null) gameOverPanel.setVisible(false);
        if (pausePanel != null) pausePanel.setVisible(false);
        if (eventListener != null) eventListener.createNewGame();
        if (gamePanel != null) gamePanel.requestFocus();

        //reset speed when new game
        piecesPlaced = 0;
        updateGameSpeed();

        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        togglePause();
        if (gamePanel != null)
            gamePanel.requestFocus();

    }
}