package Cartoon;

import java.util.Arrays;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.DepthTest;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public class GameScene extends Scene {
    
    private Cartoon _cartoon;
    private Breakout _breakout;
    
    private BorderPane _root;
    private Label _cameraLocation;
    private Label _score;
    
    /**
     * Creates a GameScene
     * */
    public GameScene(BorderPane root, Cartoon cartoon) {
        super(root, Constants.SCREEN_WIDTH + 300, Constants.SCREEN_HEIGHT + 200);
        _root = root;
        _cartoon = cartoon;
        Label c = new Label("The Game");
        c.setFont(new Font("sans-serif", Constants.FONT_SIZE));
        c.getTransforms().addAll(new Translate(100, 100));
        _cameraLocation = new Label("Camera Location: " + new Point3D(0, 0, 0));
        _score = new Label("Score: " + 0);
        _score.setFont(new Font("sans-serif", Constants.FONT_SIZE - 5));
        VBox labelBox = new VBox();
        labelBox.getChildren().addAll(c, _cameraLocation, _score);
        this.getRootModifiable().setTop(labelBox);
        Label help = new Label("Controls:\nW: Camera Forward\nS: Camera Backwards\nA: Camera Left\n"
                + "D: Camera Right\nSPACE: Camera Up\nX: Camera Down\nQ: Camera Rotate Up\n"
                + "Z: Camera Rotate Down\nE: Camera Rotate Left\nC: Camera Rotate Right\n"
                + "Esc: Return to Main Menu\nR: Reset Camera\nF: Toggle Computer Play\n"
                + "V: Change Ball Color\n\n(Arrow Keys)\nUp: Move Board Up\n"
                + "Down: Move Board Down\nLeft: Move Board Left\nRight: Move Board Right\n");
        _root.setRight(help);
        Slider speedControl = new Slider(0, Constants.BALL_VELOCITY*10, Constants.BALL_VELOCITY);
        speedControl.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                _breakout.getBall().setSpeed(newValue.intValue());
            }

        });
        speedControl.setFocusTraversable(false);
        this.getRootModifiable().setBottom(speedControl);
        this.getRootModifiable().setAlignment(speedControl, Pos.TOP_CENTER);
        _breakout = new Breakout(this);
        this.getRootModifiable().setCenter(this._breakout.getRoot());
        this.getRootModifiable().setBackground(new Background(new BackgroundFill(Constants.WINDOW_FILL, null, null)));
        _breakout.resetCamera();
    }
    
    public GameScene(Cartoon cartoon) {
        this(new BorderPane(), cartoon);
    }
    
    public BorderPane getRootModifiable() {
        return this._root;
    }
    
    public Label getCameraLabel() {
        return _cameraLocation;
    }
    
    public Label getScoreLabel() {
        return _score;
    }
    
    public void switchScene(boolean isStartScene) {
        _cartoon.switchScene(isStartScene);
    }
    
}
