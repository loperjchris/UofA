/**
 * CryptogramController.java
 * 
 * Takes requests from the view and hands them to the model. Can also take any
 * updates from the model and send them back to the view.
 * 
 * Usage instructions:
 * 
 * Construct CryptogramController
 * CryptogramController controller = new CryptogramController(type);
 * - Used to construct a model for the text view
 * CryptogramController controller = new CryptogramController(model)
 * - Used to construct a model for the GUI view
 * 
 * Other Useful Methods:
 * controller.requestAnswerCheck();
 * controller.requestUserString();
 * controller.requestFormatted();
 * controller.requestFormattedUserString();
 * controller.updateUserMap(key, value);
 * controller.requestFrequency();
 * controller.requestHint();
 */


public class CryptogramController {
	
	// Field variable for the construction of CryptogramContoller objects
	private CryptogramModel model;
	
	/*
     * Purpose: Passes in the model object created by the view.
     * 
     * @param model - a CyrptogramModel object created by the GUI view.
     * 
     * @return None.
     */
	public CryptogramController(CryptogramModel model) {
		this.model = model;
	}
	
	// Returns a boolean stating whether the user solved the Cryptogram
	public void requestAnswerCheck(String guess) {
		model.checkAnswer(guess);
	}
	
	// Calls formateQuote in the model to update the views with the formatted
	// encrypted quote.
	public void format() {
		model.formatQuote();
	}
	
	/*
     * Purpose: Tells the model to update the user's guesses (value) for each
     * encrypted character (key).
     * 
     * @param key - an encrypted character that doesn't change.
     * 
     * @param value - a user given character that is the guess associated with
     * the key.
     * 
     * @return None.
     */
	public void updateUserMap(Character key, Character value) {
		model.updateUserMap(key, value);
	}
	
	// Returns a string that shows how many of each letter appears in the
	// encrypted quote.
	public void requestFrequency() {
		model.getFrequency();
	}
	
	// Returns a correct key/value pair string
	public void requestHint() {
		model.getHint();
	}
}

