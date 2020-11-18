package chat;

import java.io.IOException;

/**
 * ChatViewModel.java
 * 
 * Keeps track of the users of the chat system.
 * 
 * Usage instructions:
 * 
 * Construct ChatViewModel
 * ChatViewModel cvm = new ChatViewModel()
 * 
 * Other Useful Methods:
 * cvm.getSelectedUser()
 * cvm.setSelectedUser()
 * 
 */

public class ChatViewModel {
	private String selectedUser;
	
	/**
	 * purpose: Initializes ChatViewModel objects and their attributes.
	 */
	public ChatViewModel() {
		selectedUser = "";
	}
	
	// Getter method for selectedUser
	public String getSelectedUser() {
		return selectedUser;
	}
	
	// Setter method for selectedUser
	public void setSelectedUser(String u) {
		selectedUser = u;
	}
}
