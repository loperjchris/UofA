package viewable.mapObjects;
/**
 * Path.java
 * 
 * Handles the resource for the Path object. It is the object that tells 
 * the AI where to path to and tells the game to not allow users to place
 * towers on it.
 * 
 */

import java.io.File;
import java.io.IOException;

import viewable.Viewable;

public class Path extends Viewable{

	/**
	 *  Purpose - gets the resource for the object
	 *  
	 *  @return - returns an image path
	 */
	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/path.png")).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

}
