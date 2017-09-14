package Cartoon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Cartoon.utils.ImageUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Breakout {
    // The pane in which the game subscene is contained
    private Pane _gamePane;
    // The scene which contains the 3D environment
    private SubScene _gameScene;
    // The group containing all the game components
    private Group _gameGroup;
    // The camera
    private PerspectiveCamera _camera;
    // The ball
    private BreakoutBall _ball;
    // The board
    private BreakoutBoard _board;
    // A list of components to update
    private List<BreakoutComponent> _components;
    // A reference to the parent scene to access the controls
    private GameScene _mainScene;
    
    /**
     * Creates a Breakout, which represents the core of the game without the controls
     * @param mainScene The parent scene with the control labels, sliders, and event listeners
     */
    public Breakout(GameScene mainScene) {
        _mainScene = mainScene;
        _gameGroup = new Group();
        _components = new ArrayList<BreakoutComponent>();
        _gamePane = new Pane();
        _gameScene = new SubScene(this._gameGroup, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, true,
                SceneAntialiasing.BALANCED);
        _gamePane.getChildren().add(_gameScene);
        this.generateBricks();
        _gameGroup.getTransforms().add(
                new Translate(0.5 * Constants.BRICK_WIDTH, 0.5 * Constants.BRICK_HEIGHT, 0.5 * Constants.BRICK_DEPTH));
        _board = new BreakoutBoard();
        _gameGroup.getChildren().addAll(_board.getNodes());
        _components.add(_board);
        this.createBall();
        this.createLights();
        this.createEventHandlers();
        this.startTimeline();
    }
    // Starts the primary timeline, which animates the ball and bricks
    private void startTimeline() {
        KeyFrame kf = new KeyFrame(Duration.millis(1000 / Constants.FRAMES_PER_SECOND), new TimeHandler());
        Timeline t = new Timeline(kf);
        t.setCycleCount(Animation.INDEFINITE);
        t.play();
    }
    
    // Creates the light for the 3D environment; ambient light leads to a more uniform lighting scheme
    private void createLights() {
        PointLight light = new PointLight(Color.WHITE);
        light.getTransforms().add(new Translate(Constants.BRICK_WIDTH * Constants.BRICKS_PER_ROW / 2,
                Constants.LIGHT_OFFSET + Constants.BOARD_HEIGHT, Constants.BRICK_DEPTH * Constants.NUM_LAYERS / 2));
        /*
         * AmbientLight light = new AmbientLight(Color.WHITE);
         * this._gameGroup.getChildren().add(light);
         */
    }
    
    // Creates a BreakoutBall and moves it out of the brick block
    private void createBall() {
        _ball = new BreakoutBall(Constants.BALL_INITIAL_VELOCITY, _mainScene.getScoreLabel());
        _gameGroup.getChildren().add(_ball);
        Point3D boardLoc = _board.getBody().localToScene(0, 0, 0);
        _ball.getTransforms().add(new Translate(boardLoc.getX(), boardLoc.getY(), boardLoc.getZ()));
        _board.setBall(_ball);
    }
    
    // Generates the bricks
    private void generateBricks() {
        for (int k = 0; k < Constants.NUM_LAYERS; k++) {
            for (int j = 0; j < Constants.NUM_ROWS; j++) {
                for (int i = 0; i < Constants.BRICKS_PER_ROW; i++) {
                    this.addBrick(new Brick((i) * (Constants.BRICK_WIDTH + Constants.BRICK_SPACING),
                            (j) * (Constants.BRICK_HEIGHT + Constants.BRICK_SPACING),
                            (k) * (Constants.BRICK_DEPTH + Constants.BRICK_SPACING),
                            // images.get(k % images.size())
                            k));
                }
            }
        }
    }
    
    // Returns the game SubScene
    public SubScene getRoot() {
        return _gameScene;
    }
    
    // A helper method for generateBricks(), adds a brick to the game
    private void addBrick(Brick brick) {
        _gameGroup.getChildren().add(brick);
        this.getComponents().add(brick);
    }
    
    // Returns a list of the components in this game
    private List<BreakoutComponent> getComponents() {
        return _components;
    }
    
    // Returns the pane that contains the SubScene
    private Pane getPane() {
        return _gamePane;
    }
    
    /**
     * Resets the camera position to the default; not advised when manually controlling
     * the board.
     * */
    public void resetCamera() {
        _camera = new PerspectiveCamera(true);
        _camera.setFarClip(4500);
        this.translateCamera(Constants.BRICK_WIDTH * Constants.BRICKS_PER_ROW / 2,
                Constants.BRICK_HEIGHT * Constants.NUM_ROWS / 2, Constants.CAMERA_Z);
        this.rotateCamera(0, 180);
        _camera.setFieldOfView(20);
        _camera.setDepthTest(DepthTest.ENABLE);
        _gameScene.setCamera(_camera);
    }
    
    // Moves the camera
    private void translateCamera(double x, double y, double z) {
        Point3D point = // c.getLocalToSceneTransform().deltaTransform(x, y, z);
                new Point3D(x, y, z);
        _camera.getTransforms().addAll(new Translate(point.getX(), point.getY(), point.getZ()));
    }
    
    // Rotates the camera
    private void rotateCamera(double theta, double phi) {
        _camera.getTransforms().addAll(new Rotate(theta, Rotate.Y_AXIS), new Rotate(phi, Rotate.X_AXIS));
    }
    
    // Defines and initializes the key handlers. See the help label to see control mappings.
    private void createEventHandlers() {
        _mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if (keyCode.equals(KeyCode.W)) {
                    Breakout.this.translateCamera(0, 0, Constants.CAMERA_SPEED);
                } else if (keyCode.equals(KeyCode.S)) {
                    Breakout.this.translateCamera(0, 0, -Constants.CAMERA_SPEED);
                } else if (keyCode.equals(KeyCode.Q)) {
                    Breakout.this.rotateCamera(0, Constants.CAMERA_ROT_SPEED);
                } else if (keyCode.equals(KeyCode.Z)) {
                    Breakout.this.rotateCamera(0, -Constants.CAMERA_ROT_SPEED);
                } else if (keyCode.equals(KeyCode.E)) {
                    Breakout.this.rotateCamera(Constants.CAMERA_ROT_SPEED, 0);
                } else if (keyCode.equals(KeyCode.C)) {
                    Breakout.this.rotateCamera(-Constants.CAMERA_ROT_SPEED, 0);
                } else if (keyCode.equals(KeyCode.SPACE)) {
                    Breakout.this.translateCamera(0, -Constants.CAMERA_SPEED, 0);
                } else if (keyCode.equals(KeyCode.X)) {
                    Breakout.this.translateCamera(0, Constants.CAMERA_SPEED, 0);
                } else if (keyCode.equals(KeyCode.D)) {
                    Breakout.this.translateCamera(Constants.CAMERA_SPEED, 0, 0);
                } else if (keyCode.equals(KeyCode.A)) {
                    Breakout.this.translateCamera(-Constants.CAMERA_SPEED, 0, 0);
                } else if (keyCode.equals(KeyCode.ESCAPE)) {
                    Breakout.this.resetCamera();
                    Breakout.this._mainScene.switchScene(true);
                } else if (keyCode.equals(KeyCode.R)) {
                    Breakout.this.resetCamera();
                } else if (keyCode.equals(KeyCode.F)) {
                    _board.toggleAIControlled();
                } else if (keyCode.equals(KeyCode.V)) {
                    _ball.changeColor();
                } else if (!_board.isAIControlled()) {
                    if (keyCode.equals(KeyCode.UP)) {
                        double y = _board.move(0, Constants.BOARD_VELOCITY).getY();
                        Breakout.this.translateCamera(0, -y, 0);
                    } else if (keyCode.equals(KeyCode.DOWN)) {
                        double y = _board.move(0, -Constants.BOARD_VELOCITY).getY();
                        Breakout.this.translateCamera(0, -y, 0);
                    } else if (keyCode.equals(KeyCode.LEFT)) {
                        double x = _board.move(-Constants.BOARD_VELOCITY, 0).getX();
                        Breakout.this.translateCamera(x, 0, 0);
                    } else if (keyCode.equals(KeyCode.RIGHT)) {
                        double x = _board.move(Constants.BOARD_VELOCITY, 0).getX();
                        Breakout.this.translateCamera(x, 0, 0);
                    }
                }
                Point3D loc = _camera.getLocalToParentTransform().transform(0, 0, 0);
                _mainScene.getCameraLabel().setText("Camera Location: " + loc);
                event.consume();
            }
        });
    }
    // This class represents the event handler for the timeline
    private class TimeHandler implements EventHandler<ActionEvent> {
        
        @Override
        public void handle(ActionEvent event) {
            // Moves the ball
            _ball.update();
            // Checks for collisions
            for (BreakoutComponent comp : _components) {
                comp.update();
                // Checks if the brick is visible and the ball bounds intersect the brick bounds
                if (comp.isTangible() && _ball.getBounds().intersects(comp.getBounds())
                        && !comp.equals(_ball.getLastCollision())) {
                    comp.hitByBall(_ball);
                    break;
                }
            }
            
        }
        
    }

    /** 
     * Returns a reference to the ball; for the speed selection slider
     * @return The Breakout ball associated with this game
     * */
    public BreakoutBall getBall() {
        return _ball;
    }
}
