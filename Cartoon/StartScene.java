package Cartoon;

import Cartoon.utils.ImageUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

/**
 * Represents the loading scene.
 * */
public class StartScene extends Scene {
    private BorderPane _root;
    private Timeline _timeline;
    private SpinningCube _theCube;
    private Group _primaryContainer;
    private Cartoon _cartoon;
    
    public StartScene(BorderPane root, Cartoon cartoon) {
        super(root);
        this._root = root;
        root.setBackground(new Background(
                new BackgroundImage(ImageUtils.decodeImage(ImageDataConstants.STARMAP), null, null, null, null)));
        this._primaryContainer = new Group();
        this._cartoon = cartoon;
        this.getRootModifiable().setCenter(_primaryContainer);
        this.drawLabels();
        this.drawButtons();
        this.addCube();
        root.setMinWidth(Constants.APPLICATION_WIDTH);
        root.setMinHeight(Constants.APPLICATION_HEIGHT);
    }
    
    private void addCube() {
        this._theCube = new SpinningCube(Constants.CUBE_WIDTH, Constants.CUBE_WIDTH, Constants.CUBE_WIDTH,
                Constants.MIN_CUBE_VELOCITY, Constants.MAX_CUBE_VELOCITY);
        PhongMaterial texture = new PhongMaterial(Color.WHITE);
        texture.setDiffuseMap(ImageUtils.decodeImage(ImageDataConstants.SPACE));
        this._theCube.setMaterial(texture);
        this._theCube.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (_theCube.isSpinning()) {
                    return;
                }
                _theCube.setSpin(true);
                StartScene.this.startTimeline();
                event.consume();
                new Timeline(new KeyFrame(Duration.seconds(Constants.SPIN_TIME), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        _theCube.resetVelocity();
                        _theCube.setSpin(false);
                        _cartoon.switchScene(false);
                    }
                })).play();
            }
        });
        AmbientLight light = new AmbientLight(Color.WHITE);
        this._primaryContainer.getChildren().add(this._theCube);
        this._primaryContainer.getChildren().add(light);
    }
    
    public StartScene(Cartoon cartoon) {
        this(new BorderPane(), cartoon);
    }
    
    private void startTimeline() {
        KeyFrame frame = new KeyFrame(Duration.millis(1000 / Constants.FRAMES_PER_SECOND),
                new javafx.event.EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        _theCube.update();
                    }
                });
        this._timeline = new Timeline(frame);
        this._timeline.setCycleCount(Constants.SPIN_TIME * 1000 * Constants.FRAMES_PER_SECOND);
        this._timeline.play();
    }
    
    private void drawButtons() {
        Button b = new Button("Quit!");
        b.setBackground(new Background(
                new BackgroundImage(ImageUtils.decodeImage(ImageDataConstants.STARMAP), null, null, null, null)));
        b.setTextFill(Color.AZURE);
        this.getRootModifiable().setBottom(b);
        this.getRootModifiable().setAlignment(b, Pos.BOTTOM_CENTER);
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Platform.exit();
            }
        });
    }
    
    public BorderPane getRootModifiable() {
        return this._root;
    }
    
    private void drawLabels() {
        Label title = new Label("Breakout");
        this.getRootModifiable().setTop(title);
        this.getRootModifiable().setAlignment(title, Pos.TOP_CENTER);
        title.setFont(new Font("Cambria", Constants.FONT_SIZE));
        title.setTranslateY(20);
        title.setTextFill(Color.AZURE);
    }
    
}
