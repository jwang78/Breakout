package Cartoon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import Cartoon.utils.ImageUtils;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.transform.Rotate;

/**
 * Images in this application are from Wikimedia Commons, licensed under the
 * Attribution-ShareAlike 3.0 Unported license License:
 * https://creativecommons.org/licenses/by-sa/3.0/
 * 
 * The Application is built as follows: A Cartoon class is instantiated, passing
 * in the stage. The Cartoon class creates two Scenes: the loading screen and
 * the game scene. Each scene has its own root panes and objects; the loading
 * screen contains a quit button and passes control to the game scene upon the
 * cube being clicked. The game scene initializes a Breakout object, which
 * contains the primary components and the 3D environment. Components include
 * the ball, board, and bricks. There are several timelines: The first animates
 * the cube in the loading scene; the second animates the ball and updates each
 * component's position and color. When a brick is broken, yet another timeline
 * is created to regenerate it after a set number of seconds. Constants are
 * stored in Constants.java and images are stored in ImageDataConstants.java in
 * a base-64 encoding. ImageUtils converts between the string representations
 * and the Images used in this app. The slider at the bottom of the game scene
 * controls the ball's speed.
 * 
 * Known issues: RNG may result in too little z-movement; the ball has some
 * random motion encoded to fix this. Sometimes the ball tunnels into the block;
 * this is usually followed by an extreme frame rate drop. A restart fixes this,
 * but sometimes it resolves itself on its own. This is due to the weak
 * collision checking implementation.
 */
public class App extends javafx.application.Application {
	public static void main(String[] args) throws Exception {
		javafx.application.Application.launch(args);
		/*
		 * File directory = new File("C:\\Users\\James\\Documents\\PeoplePictures");
		 * File[] files = directory.listFiles(); StringBuilder javafile = new
		 * StringBuilder(); javafile.append("public class ImageData {\n"); for (File
		 * file : files) { String name = file.getName(); name = name.substring(0,
		 * name.length() - 4);
		 * javafile.append("    public static final String "+name+" = \"" +
		 * ImageUtils.encodeImage(file) + "\";\n");
		 * System.out.println(file.getName()+" processed."); } javafile.append("}");
		 * FileWriter f = new FileWriter("C:\\Users\\James\\Documents\\ImageData.java");
		 * f.write(javafile.toString()); f.close(); System.out.println("Finished!");
		 * 
		 * FileWriter f = new FileWriter("C:\\Users\\James\\Documents\\ImageData.java");
		 * f.write(ImageUtils.encodeImage(new
		 * File("C:\\Users\\James\\Documents\\arya.jpg"))); f.close();
		 */

	}

	/**
	 * Starts the application by instantiating a Cartoon.
	 */
	public void start(javafx.stage.Stage daStage) {
		new Cartoon(daStage);
	}
}
