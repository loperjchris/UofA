package viewable.mapObjects;
/**
 * Placeable.java
 * 
 * Handles the resource for the Placeable objects. Tells the game where
 * users can place towers.
 */

import java.io.File;
import java.io.IOException;

import viewable.Viewable;

public class Placeable extends Viewable{

	/**
	 *  Purpose - gets the resource for the object
	 *  
	 *  @return - returns an image path
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/Grass.png")).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}
	
}
