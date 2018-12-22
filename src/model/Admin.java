package model;

/**
 * 
 * @author Kishan Patel
 * @author Neal Patel
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Admin implements Serializable{
	
	/**
	 * name of directory to store serialized data
	 */
	public static final String storeDir = "dat";
	
	/**
	 * name of file to store serialized data
	 */
	public static final String storeFile = "serialization.dat";
	
	/**
	 * serial version uid used for serialization
	 */
	static final long serialVersionUID = 1L;
	
	/**
	 * User object for current user
	 */
	private User currentUser;
	
	/**
	 * list of users
	 */
	private ArrayList<User> users;
	
	/**
	 * Admin constructor
	 */
	public Admin() {
		users = new ArrayList<User>();
		users.add(new User("admin"));
	}
	
	/**
	 * to write/serialize data into a file
	 * @param app the information to serialize
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	public static void write(Admin app) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(app);
		oos.close();
	} 
	
	/**
	 * to read/deserialize data from a file
	 * @return returns deserialized information from the serialized file 
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @throws ClassNotFoundException Signals class not found
	 */
	public static Admin read() throws IOException, ClassNotFoundException {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
			Admin app = (Admin)ois.readObject();
			ois.close();
			return app;
	} 

	/**
	 * to add a user
	 * @param user the user to add (String)
	 */
	public void addUser(String user) {
		users.add(new User(user));
	}

	/**
	 * to delete a user
	 * @param user the user to delete (String)
	 */
	public void deleteUser(String user) {
		User u = new User(user);
		users.remove(u);
	}

	/**
	 * the list of users
	 * @return returns the list of users
	 */
	public ArrayList<User> getUsers() {
		return users;
	}
	
	/**
	 * to get the current user
	 * @return current user
	 */
	public User getCurrentUser() {
		return currentUser;
	}
	
	/**
	 * to set the current user
	 * @param currentUser the current user that is set to
	 */
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * to set users
	 * @param users the list of users to set to
	 */
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	
	/**
	 * to add a user 
	 * @param user the user to add (User object)
	 */
	public void addUser(User user) {
		users.add(user);
	}
	
	/**
	 * to remove a user
	 * @param index the index in the list of users
	 */
	public void removeUser(int index) {
		users.remove(index);
	}
	
	/**
	 * to get a user object
	 * @param username the username of a user (String)
	 * @return returns the user (User object)
	 */
	public User getUser(String username) {
		for(User user : users) {
			if(user.getUsername().equals(username)) {
				return user;
			}
		}
		
		return null;
	}
	
}
