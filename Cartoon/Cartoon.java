package Cartoon;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * The top-level class; creates the two primary scenes.
 * */
public class Cartoon {
    private Stage _stage;
    
    public Cartoon(Stage theStage) {
        this._stage = theStage;
        this.switchScene(true);
        this._stage.show();
    }
    
    /**
     * Switches the scene.
     * @param start Whether the scene to switch to is the start
     * */
    public void switchScene(boolean start) {
        if (start) {
            this._stage.setScene(new StartScene(this));
            
        } else {
            this._stage.setScene(new GameScene(this));
            
        }
    }
    
}
