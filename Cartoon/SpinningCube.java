package Cartoon;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
/**
 * Represents the spinning cube at the loading screen.
 * */
public class SpinningCube extends Box {
    private double _initialVelocity;
    private double _maximumVelocity;
    private double _velocity;
    private boolean _spinning = false;
    
    public SpinningCube(int length, int width, int height, double initVel, double maxVel) {
        super(length, width, height);
        this._initialVelocity = this.setVelocity(initVel);
        this._maximumVelocity = maxVel;
        this.getTransforms().addAll(new Rotate(15, Rotate.X_AXIS), new Rotate(15, Rotate.Y_AXIS));
    }
    
    public void update() {
        if (!this._spinning) {
            return;
        }
        this.getTransforms().add(new Rotate(this.getVelocity(), Rotate.Y_AXIS));
        // Logistic growth
        double differential = (this._maximumVelocity - this._initialVelocity)
                / (Constants.FRAMES_PER_SECOND * Constants.SPEED_TIME) * this.getVelocity()
                * (this._maximumVelocity - this.getVelocity()) / this._maximumVelocity;
        this.setVelocity(differential + this.getVelocity());
        
    }
    
    public double setVelocity(double v) {
        return this._velocity = v;
    }
    
    public double getVelocity() {
        return this._velocity;
    }
    
    public void setSpin(boolean spinning) {
        this._spinning = spinning;
    }
    
    public boolean isSpinning() {
        return this._spinning;
    }
    
    public void resetVelocity() {
        this._velocity = _initialVelocity;
    }
}
