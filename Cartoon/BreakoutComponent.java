package Cartoon;

/**
 * Interface for a general component of the game.
 * */
public interface BreakoutComponent {
    /**
     * Called by the Timeline to update components
     * */
    public void update();
    
    /**
     * Used to determine collision status.
     * */
    public javafx.geometry.Bounds getBounds();
    
    /**
     * Called when a component is hit by the ball.
     * */
    public void hitByBall(BreakoutBall b);
    
    /**
     * Returns whether the object is part of the game at the moment.
     * */
    public boolean isTangible();
}
