package chat;

/**
 * User.java
 * 
 * Establishes a new user and ties their username to a password.
 * 
 * Usage instructions:
 * 
 * Construct User
 * User user = new User(userName, password)
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;


public class User implements Serializable{

	// Field variables for User objects
	private static final long serialVersionUID = 9215472150552409030L;
	private String user;
	private String pass;
	private File file;
	private static byte[] salt;
	private static MessageDigest md;
	
	/**
	 * purpose: Initializes a User object and its attributes.
	 * 
	 * @param userName - a String representing the user's login ID
	 * 
	 * @param password - a String that is the user's password
	 * 
	 * @throws IOException - throws an exception if the port number is 
	 * incorrect or closes before the connection is established.
	 * 
	 * @throws NoSuchAlgorithmException - throws an exception if the user's username and/or
	 * password is incorrect.
	 * 
	 */
	public User(String userName, String password) throws IOException, NoSuchAlgorithmException {
		user = userName;
		md = MessageDigest.getInstance("SHA-512");
		file = new File("hash.txt");
		salt = new byte[16];
		if(file.createNewFile()) {
			salt = generateRandomSalt(salt);
		}else {
			Scanner scan = new Scanner(file);
			if(!scan.hasNextLine()) {
				salt = generateRandomSalt(salt);
				FileOutputStream out = new FileOutputStream(file);
				out.write(salt);
				out.close();
			}else {
				String bytes = scan.nextLine();
				salt = bytes.getBytes();
			}
			scan.close();
		}
		pass = generateSHA512Password(password);
	}
	
	// Getter method for pass
	public String getPassword() {
		return pass;
	}
	
	/**
	 * purpose: Checks to see if the entered password is correct.
	 * 
	 * @param check - a String that is the text input of the user's password
	 * to be checked
	 * 
	 * @throws FileNotFoundException - throws an error if the given password doesn't
	 * match any username/password combinations in the system
	 * 
	 * @return true if password found; false otherwise
	 */
	public boolean checkPassword(String check) throws FileNotFoundException {
		System.out.println(generateSHA512Password(check)+"\n"+pass);
		System.out.println(generateSHA512Password(check).equals(pass));
		if(generateSHA512Password(check).equals(pass)) {
			return true;
		}
		return false;
	}
	
	// Getter method for user
	public String getUsername() {
		return user;
	}
	
	/**
	 * purpose: Encrypts the user's password using a salting method.
	 * 
	 * @param salt - an array of random numbers
	 * 
	 * 
	 */
	private static byte[] generateRandomSalt(byte[] salt) {
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);
		return salt;
	}
	
	/**
	 * purpose: Encrypts the password using SHA512.
	 * 
	 * @param pass - the user generated password
	 * 
	 * 
	 */
	private String generateSHA512Password(String pass) {
		md.reset();
        byte[] bytes = md.digest(pass.getBytes());
        String sb = "";
        for(int i=0; i< bytes.length ;i++)
        {
            sb+= Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return sb;
	}
}
