package Cartoon;

import java.util.ArrayList;
import Cartoon.utils.ImageUtils;
import java.util.List;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

/**
 * Represents the board in the game.
 * */
public class BreakoutBoard implements BreakoutComponent {
    
    private Box _body;
    private Cylinder[] _sides;
    private Sphere _corners[];
    private Color[] colors;
    private boolean _isAIControlled = false;
    private List<Node> _nodes;
    private BreakoutBall _ball;
    
    public BreakoutBoard() {
        PhongMaterial boardMat = new PhongMaterial(
                Color.WHITE, ImageUtils.decodeImage(ImageDataConstants.MARBLE), null, null, null);
        _sides = new Cylinder[4];
        _corners = new Sphere[4];
        _nodes = new ArrayList<Node>();
        _body = new Box(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT, Constants.BOARD_DEPTH);
        
        _sides[0] = new Cylinder(Constants.BOARD_DEPTH / 2, Constants.BOARD_WIDTH);
        _sides[1] = new Cylinder(Constants.BOARD_DEPTH / 2, Constants.BOARD_HEIGHT);
        _sides[2] = new Cylinder(Constants.BOARD_DEPTH / 2, Constants.BOARD_WIDTH);
        _sides[3] = new Cylinder(Constants.BOARD_DEPTH / 2, Constants.BOARD_HEIGHT);
        _body.setMaterial(boardMat);
        for (int i = 0; i < 4; i++) {
            _corners[i] = new Sphere(Constants.BOARD_DEPTH / 2);
        }
        
        for (int i = 0; i < 4; i++) {
            _corners[i].setMaterial(boardMat);
            _sides[i].setMaterial(boardMat);
        }
        
        Translate boardOffset = new Translate(Constants.BOARD_WIDTH / 2, Constants.BOARD_HEIGHT / 2,
                Constants.BOARD_DEPTH / 2);
        _body.getTransforms().addAll(boardOffset);
        _sides[0].getTransforms().addAll(boardOffset, new Translate(0, Constants.BOARD_HEIGHT / 2, 0),
                new Rotate(90, Rotate.Z_AXIS));
        _sides[2].getTransforms().addAll(boardOffset, new Translate(0, -Constants.BOARD_HEIGHT / 2, 0),
                new Rotate(90, Rotate.Z_AXIS));
        _sides[1].getTransforms().addAll(boardOffset, new Translate(Constants.BOARD_WIDTH / 2, 0, 0));
        _sides[3].getTransforms().addAll(boardOffset, new Translate(-Constants.BOARD_WIDTH / 2, 0, 0));
        _corners[0].getTransforms().addAll(new Translate(0, 0, Constants.BOARD_DEPTH / 2));
        _corners[1].getTransforms().add(new Translate(0, Constants.BOARD_HEIGHT, Constants.BOARD_DEPTH / 2));
        _corners[2].getTransforms()
                .add(new Translate(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT, Constants.BOARD_DEPTH / 2));
        _corners[3].getTransforms().add(new Translate(Constants.BOARD_WIDTH, 0, Constants.BOARD_DEPTH / 2));
        this.translate(-Constants.BRICK_WIDTH / 2, -Constants.BRICK_HEIGHT / 2, -Constants.BRICK_DEPTH / 2);
        this.translate((Constants.BRICKS_PER_ROW - 1) * Constants.BRICK_WIDTH / 2,
                (Constants.NUM_ROWS - 2) * Constants.BRICK_HEIGHT / 2, Constants.BRICK_DEPTH * Constants.NUM_LAYERS);
        this.translate(0, 0, Constants.BOARD_GAP);
        for (Cylinder cyl : _sides) {
            _nodes.add(cyl);
        }
        for (Sphere sph : _corners) {
            _nodes.add(sph);
        }
        _nodes.add(_body);
        
    }
    /**
     * Returns the body of the board, for retrieving location information.
     * @return The body of the board.
     * */
    public Box getBody() {
        return _body;
    }
    
    /**
     * Returns the nodes part of this board for addition to the scene graph.
     * @return A list of nodes part of this board.
     * */
    public List<Node> getNodes() {
        return _nodes;
    }
    
    /**
     * Updates the board position if AI controlled; otherwise does nothing
     * */
    @Override
    public void update() {
        if (!this.isAIControlled()) {
            return;
        }
        Point3D a = _ball.localToParent(0, 0, 0);
        Point3D b = _body.localToParent(0, 0, 0);
        Point3D trajectory = new Point3D(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
        this.move(trajectory.getX() * -Constants.DISTANCE_PROPORTION,
                trajectory.getY() * -Constants.DISTANCE_PROPORTION);
    }
    
    // Applies a transform to all the shapes
    private void applyTransform(Transform transform) {
        // this.getTransforms().add(transform); - Not having this is really
        // making this difficult
        for (int i = 0; i < 4; i++) {
            _corners[i].getTransforms().add(0, transform);
            _sides[i].getTransforms().add(0, transform);
        }
        _body.getTransforms().add(0, transform);
    }
    
    /**
     * Moves the board in the XY plane; checks to see if the position would be legal
     * before movement
     * */
    public Point3D move(double x, double y) {
        Point3D boardLocation = _body.localToScene(0, 0, 0);
        double minX = Constants.BOARD_WIDTH / 2;
        double minY = Constants.BOARD_HEIGHT / 2;
        double maxX = Constants.MAX_X - minX;
        double maxY = Constants.MAX_Y - minY;
        double moveX = x;
        double moveY = y;
        // Board too far to the left
        if (boardLocation.getX() + x < minX) {
            moveX = minX - boardLocation.getX();
        }
        // Board too far to the right
        if (boardLocation.getX() + x > maxX) {
            moveX = maxX - boardLocation.getX();
        }
        // Board too low
        if (boardLocation.getY() + y < minY) {
            moveY = minY - boardLocation.getY();
        }
        // Board too high
        if (boardLocation.getY() + y > maxY) {
            moveY = maxY - boardLocation.getY();
        }
        this.translate(moveX, moveY, 0);
        return new Point3D(moveX, moveY, 0);
    }
    // Moves the board in the x, y, and z directions.  Not checked.
    private void translate(double x, double y, double z) {
        this.applyTransform(new Translate(x, y, z));
    }
    
    /**
     * A flag for whether the board is controlled by the computer.
     * @return Whether the board is controlled by the computer.
     * */
    public boolean isAIControlled() {
        return _isAIControlled;
    }
    
    /**
     * Sets the AI control status of this board.
     * @param isAIControlled Whether the board should be computer controlled.
     * */
    public void setAIControlled(boolean isAIControlled) {
        _isAIControlled = isAIControlled;
    }
    
    /**
     * Toggles the AI control status of this board.
     * */
    public void toggleAIControlled() {
        this.setAIControlled(!this.isAIControlled());
    }
    
    /**
     * Returns the bounds of the board for collision checking.
     * @return The bounds of the body of the board; the edges are decorative.
     * */
    @Override
    public Bounds getBounds() {
        Bounds boardLoc = _body.getBoundsInParent();
        return boardLoc;
    }
    
    /**
     * Called when the board is impacted by the ball.  Bounces the ball.
     * */
    @Override
    public void hitByBall(BreakoutBall b) {
        b.bounceOff(this);
    }
    
    /**
     * Returns whether this board is part of the game.
     * @return true
     * */
    @Override
    public boolean isTangible() {
        return true;
    }
    
    /**
     * Used to set the reference to the ball.
     * @param b The BreakoutBall to reference
     * */
    public void setBall(BreakoutBall b) {
        this._ball = b;
    }
}
