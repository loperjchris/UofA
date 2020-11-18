package handlers;
/**
 * ImageResourceLoadingHandler.java
 * 
 * Loads all resources
 * 
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import viewable.Viewable;

public class ImageResourceLoadingHandler {
	
	/**
	 * Purpose: fetches resources for each class that requires one
	 * 
	 * @param obj the Viewable object needing a resource fetched
	 * 
	 * @return an ImageView with the resource
	 * 
	 * @throws FileNotFoundException throws if resource can not be found
	 */
	public static ImageView getResource(Viewable obj) throws FileNotFoundException {
		ImageView view;
		if(obj == null) {
			view = new ImageView(new Image(new FileInputStream(Viewable.getDefaultResource())));
		}else {
			view = new ImageView(new Image(new FileInputStream(obj.getResource())));
		}
		
		return view;
	}
}
