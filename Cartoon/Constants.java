package Cartoon;

import java.util.Arrays;
import java.util.List;

import Cartoon.utils.ImageUtils;
import javafx.geometry.Point3D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Constants {
    public static final int FRAMES_PER_SECOND = 25;
    public static final double MAX_CUBE_VELOCITY = 800;
    public static final double MIN_CUBE_VELOCITY = 2;
    public static final int CUBE_WIDTH = 200;
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 400;
    // Exceeds constant maximum size; also not new-lined due to excessive use of space
    public static final int SCREEN_WIDTH = 1440;
    public static final int SCREEN_HEIGHT = 810;
    public static final int BORDER_WIDTH = 80;
    public static final int FONT_SIZE = 30;
    public static final int SPEED_TIME = 1000;
    public static final int BRICK_DEPTH = 50;
    public static final int BRICK_HEIGHT = 50;
    public static final int BRICK_WIDTH = 100;
    public static final int BRICKS_PER_ROW = 14;
    public static final int NUM_ROWS = 10;
    public static final int NUM_LAYERS = 10;
    public static final int CAMERA_SPEED = 10;
    public static final int CAMERA_ROT_SPEED = 2;
    public static final int BRICK_SPACING = 0;
    public static final int SPIN_TIME = 5;
    public static final Paint WINDOW_FILL = Color.WHITE;
    public static final int LIGHT_OFFSET = 200;
    public static final int BOARD_GAP = 700;
    public static final double BOARD_WIDTH = 100;
    public static final double BOARD_HEIGHT = 100;
    public static final double BOARD_DEPTH = 35;
    public static final double BALL_RADIUS = 10;
    public static final int BRICK_UPDATE_TIME = 10;
    public static final int BRICK_REGEN_TIME = 180;
    public static final double CAMERA_Z = 2730;
    public static final double MAX_X = BRICKS_PER_ROW * BRICK_WIDTH;
    public static final double MAX_Y = NUM_ROWS * BRICK_HEIGHT;
    public static final double MAX_Z = BOARD_GAP + 100 + NUM_LAYERS * BRICK_DEPTH;
    public static final double BOARD_VELOCITY = 20;
    public static final List<Image> BRICK_IMAGES = 
            Arrays.asList(ImageUtils.decodeImage(ImageDataConstants.ICE),
                    ImageUtils.decodeImage(ImageDataConstants.PLEXIGLASS),
                    ImageUtils.decodeImage(ImageDataConstants.GOLD),
                    ImageUtils.decodeImage(ImageDataConstants.SILVER),
                    ImageUtils.decodeImage(ImageDataConstants.SHELL),
                    ImageUtils.decodeImage(ImageDataConstants.PEBBLES));
    public static final List<Color> COLORS = Arrays.asList(Color.WHITE, Color.RED, Color.YELLOW, Color.GREEN,
            Color.BLUE, Color.VIOLET, Color.BLACK);
    public static final int SCORE_PER_BRICK = 1;
    public static final int BALL_VELOCITY = 50;
    public static final int Z_BIAS = 4;
    public static final Point3D BALL_INITIAL_VELOCITY = Constants
            .norm(new Point3D(Math.random() - 0.5, Math.random() - 0.5, Z_BIAS * (Math.random() - 0.5)));
    public static final int SCORE_PENALTY = -10;
    public static final double DISTANCE_PROPORTION = 0.4;
    public static final double PROB_CHANGE = 0.002;
    
    public static Point3D norm(Point3D vector) {
        double magnitude = Math.hypot(Math.hypot(vector.getX(), vector.getY()), vector.getZ());
        return new Point3D(vector.getX() / magnitude, vector.getY() / magnitude, vector.getZ() / magnitude);
    }
    
    // Image data; several hundred thousand characters in each line
    }