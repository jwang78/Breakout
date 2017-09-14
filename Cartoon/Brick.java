package Cartoon;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

/**
 * Represents a brick in the game
 * */
public class Brick extends Box implements BreakoutComponent {
    private int _updateCounter = 0;
    private int _imageNum = 0;
    private boolean _tangible;
    
    /**
     * Creates a brick of a certain color at a certain position.
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param Color The color
     * */
    public Brick(double x, double y, double z, Color color) {
        this(x, y, z, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, Constants.BRICK_DEPTH, new PhongMaterial(color));
    }
    
    /**
     * Creates a brick out of a certain material at a certain position with defined dimensions.
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param w Width
     * @param h Height
     * @param d Depth
     * @param mat Material to construct the brick
     * */
    public Brick(double x, double y, double z, double w, double h, double d, Material mat) {
        super(w, h, d);
        super.getTransforms().add(new Translate(x, y, z));
        super.setMaterial(mat);
        _tangible = true;
    }
    
    /**
     * Creates a brick with a certain image as its texture.
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param img The image
     * */
    public Brick(double x, double y, double z, Image img) {
        this(x, y, z, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, Constants.BRICK_DEPTH,
                new PhongMaterial(Color.WHITE, img, null, null, null));
    }
    
    /**
     * Creates a brick with a certain image index, which determines its image.
     * @param imageIndex The starting index for the images
     * */
    public Brick(double x, double y, double z, int imageIndex) {
        this(x, y, z, Constants.BRICK_IMAGES.get(imageIndex % Constants.BRICK_IMAGES.size()));
        _imageNum = imageIndex;
    }
    
    /**
     * Updates the brick's color
     * */
    @Override
    public void update() {
        if (++_updateCounter % (Constants.FRAMES_PER_SECOND * Constants.BRICK_UPDATE_TIME) == 0) {
            Image next = Constants.BRICK_IMAGES.get(++_imageNum % Constants.BRICK_IMAGES.size());
            super.setMaterial(new PhongMaterial(Color.WHITE, next, null, null, null));
        }
    }
    
    /**
     * Returns the image bounds
     * */
    @Override
    public Bounds getBounds() {
        return this.getBoundsInParent();
    }
    
    
    
    /**
     * Called when hit by a ball
     * */
    @Override
    public void hitByBall(BreakoutBall b) {
        this.setVisible(false);
        _tangible = false;
        // Regens the brick
        new Timeline(new KeyFrame(Duration.seconds(Constants.BRICK_REGEN_TIME), (e) -> {
            _tangible = true;
            this.setVisible(true);
        })).play();
        b.addScore(Constants.SCORE_PER_BRICK);
        b.bounceOff(this);
    }
    
    /**
     * Returns whether this brick has been destroyed.
     * */
    @Override
    public boolean isTangible() {
        return _tangible;
    }
    
}
