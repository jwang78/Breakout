package Cartoon;

import java.awt.event.MouseEvent;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

/**
 * Represents the ball in the game.
 * */
public class BreakoutBall extends Sphere implements BreakoutComponent {
    // The ball's direction
    private Point3D _velocity;
    // Prevents the ball from being stuck on a wall
    private short _lastWallHit = 0;
    // The ball's speed
    private double _speed;
    // What color index the ball is currently on
    private int _colorNum;
    // Provides immunity for the first back wall hit for player to get oriented
    private boolean _immune = true;
    // A reference to the score label for incrementing
    private Label _scoreLabel;
    // The last collision with an element; used to prevent ball from being stuck
    // on a wall
    private BreakoutComponent _lastCollision;
    // The current score
    private int _score;
    
    /**
     * Creates a BreakoutBall, given a reference to the score label and the
     * initial direction
     * 
     * @param velocity
     *            A unit vector representing the initial direction of the ball
     * @param scoreLabel
     *            A reference to the score label to display the score
     */
    public BreakoutBall(Point3D velocity, Label scoreLabel) {
        super(Constants.BALL_RADIUS);
        _velocity = velocity;
        _speed = Constants.BALL_VELOCITY;
        _colorNum = 0;
        _scoreLabel = scoreLabel;
        _score = 0;
        this.changeColor();
    }
    
    // Returns the location of this ball
    private Point3D getLocation() {
        return this.localToScene(0, 0, 0);
    }
    
    /**
     * Updates the ball's position and checks for intersections with walls.
     * Randomly reassigns the ball's velocity for the cases unlucky RNG results
     * in too little Z-movement and therefore slow bouncing. These changes are
     * indicated by a change in ball color.
     */
    @Override
    public void update() {
        double moveX = _velocity.getX() * _speed;
        double moveY = _velocity.getY() * _speed;
        double moveZ = _velocity.getZ() * _speed;
        Point3D loc = this.getLocation();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        double resX = moveX;
        double resY = moveY;
        double resZ = moveZ;
        // Ball is too far to the right
        if (x + moveX > Constants.MAX_X && _lastWallHit != 1) {
            resX = Constants.MAX_X - x;
            moveX = -moveX;
            _lastWallHit = 1;
        } 
        // Ball is too far to the left
        else if (x + moveX < 0 && _lastWallHit != 2) {
            resX = -x;
            moveX = -moveX;
            _lastWallHit = 2;
        }
        // Ball is too high
        if (y + moveY > Constants.MAX_Y && _lastWallHit != 3) {
            resY = Constants.MAX_Y - y;
            moveY = -moveY;
            _lastWallHit = 3;
        } 
        // Ball is too low
        else if (y + moveY < 0 && _lastWallHit != 4) {
            resY = -y;
            moveY = -moveY;
            _lastWallHit = 4;
        }
        // Ball is behind the board
        if (z + moveZ > Constants.MAX_Z && _lastWallHit != 5) {
            resZ = Constants.MAX_Z - z;
            moveZ = -moveZ;
            if (!_immune) {
                this.addScore(Constants.SCORE_PENALTY);
            }
            _immune = false;
            _lastWallHit = 5;
        }
        // Ball has hit the far wall
        else if (z + moveZ < 0 && _lastWallHit != 6) {
            resZ = -z;
            moveZ = -moveZ;
            _lastWallHit = 6;
        }
        
        this.getTransforms().add(0, new Translate(resX, resY, resZ));
        // Randomly changing ball direction
        if (Math.random() < Constants.PROB_CHANGE) {
            this.setVelocity(
                    Constants.norm(new Point3D(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5)));
            this.changeColor();
        }
        this.setVelocity(Constants.norm(new Point3D(moveX, moveY, moveZ)));
        
    }
    /**
     * Returns the bounds of this ball for collision checking
     * @return The bounding box of this ball
     * */
    @Override
    public Bounds getBounds() {
        return this.getBoundsInParent();
    }
    
    /**
     * Changes the color of the ball to the next index in the constant color list
     * */
    public void changeColor() {
        this.setMaterial(new PhongMaterial(Constants.COLORS.get(_colorNum++ % Constants.COLORS.size())));
    }
    
    /**
     * Called when the ball bounces against a component
     * @param comp The component which the ball is bouncing off
     * */
    public void bounceOff(BreakoutComponent comp) {
        this._lastCollision = comp;
        this.setVelocity(this.getVelocity().getX(), this.getVelocity().getY(), this.getVelocity().getZ() * -1);
        _lastWallHit = 0;
    }
    
    // Returns the direction vector of this ball
    private Point3D getVelocity() {
        return this._velocity;
    }
    
    /**
     * Increments the score.
     * @param s The amount to increment the score by
     * */
    public void addScore(int s) {
        _score += s;
        _scoreLabel.setText("Score: " + _score);
    }
    
    /**
     * Called when this component is hit by a ball.  Since this is the ball, this
     * method does nothing and is never called.
     * */
    @Override
    public void hitByBall(BreakoutBall b) {
        // This is the ball
        return;
    }
    
    @Override
    /**
     * Returns whether this ball is tangible; whether the ball is part of the game
     * at this time.
     * @return true
     * */
    public boolean isTangible() {
        return true;
    }
    
    // Sets the direction vector, given x, y, z components.
    private void setVelocity(double x, double y, double z) {
        this.setVelocity(new Point3D(x, y, z));
    }
    
    // Sets the direction vector given a Point3D
    private void setVelocity(Point3D vel) {
        this._velocity = vel;
    }
    
    /**
     * Sets the speed of the ball
     * @param speed The desired speed
     * */
    public void setSpeed(double speed) {
        this._speed = speed;
    }
    
    /**
     * Returns the last element the ball collided with.  Used to prevent ball from getting stuck.
     * @return The last component the ball collided with.
     * */
    public BreakoutComponent getLastCollision() {
        return _lastCollision;
    }
}
