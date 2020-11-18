package viewable;

/**
 * Viewable.java
 * 
 * A super class of all viewable objects. Allows its subclasses to be viewed
 * by the view in order to update the board with any changes that occur
 * 
 */

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public abstract class Viewable implements Serializable{
	
	// Field variables for Viewable objects
	private static final long serialVersionUID = -1121270826949137583L;
	
	public String getResource;

	public abstract String getResource();
	
	/**
	 * purpose: Gets the default grass image for squares.
	 * 
	 * @return string path of the resource
	 *
	 */
	public static String getDefaultResource() {
		try {
			return (new File("./resources/images/Grass.png")).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
