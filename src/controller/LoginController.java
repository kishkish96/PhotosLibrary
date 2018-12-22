package controller;

/**
 * 
 * @author Kishan Patel
 * @author Neal Patel
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.Photos;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.FileInputStream;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import model.User;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class LoginController{
	
	/**
	 * login button
	 * calls loginClicked method when clicked
	 */
	@FXML
	private Button login;
	
	/**
	 * username textfield
	 */
	@FXML
	private TextField username;
	
	/**
	 * checks login credentials
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @throws ClassNotFoundException Indicates that a class has not been found
	 * @param event click of login button
	 */
	@FXML
	private void loginClicked(ActionEvent event) throws IOException, ClassNotFoundException {
		if(username.getText().toString().equals("")) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No Username Entered");
			error.setContentText("Please enter a username");
			error.showAndWait();
		}
		else if(username.getText().toString().equals("admin")){
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("view/AdminView.fxml"));
			GridPane grid = (GridPane)loader.load();
			AdminController ac = loader.getController();
			Stage stage = new Stage();
			ac.start(stage);
			stage.setTitle("Administrative Controls");
			stage.setScene(new Scene(grid));
			stage.setResizable(false);
			stage.show();
			((Node)(event.getSource())).getScene().getWindow().hide();
		}
		else {
			//try{
				boolean exists = false;
				for(int i=0;i<Photos.admin.getUsers().size();i++) {
					if(Photos.admin.getUsers().get(i).getUsername().toLowerCase().equals(username.getText().toString().toLowerCase())){
						exists = true;
					}
				}
				if(exists==true) {
					//User cUser = Photos.admin.getCurrentUser();
					//ArrayList<Album> albums = cUser.getAlbums();
					AlbumListController.username = username.getText().toString();
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getClassLoader().getResource("view/AlbumListView.fxml"));
					GridPane grid = (GridPane)loader.load();
					AlbumListController alc = loader.getController();
					Stage stage = new Stage();
					alc.start(stage);
					stage.setTitle("My Albums");
					stage.setScene(new Scene(grid));
					stage.setResizable(false);
					stage.show();
					((Node)(event.getSource())).getScene().getWindow().hide();
				}
				else {
					Alert dneError = new Alert(AlertType.ERROR);
					dneError.setHeaderText("Invalid User");
					dneError.setContentText("This username does not exist.");
					dneError.showAndWait();
				}
			/*}
			catch(NullPointerException empty) {
				Alert dneError = new Alert(AlertType.ERROR);
				dneError.setHeaderText("Invalid User");
				dneError.setContentText("This username does not exist.");
				dneError.showAndWait();
			}*/
		}

	}

	/*public ArrayList<User> readUsers(FileInputStream a) throws Exception{
		ArrayList<User> temp = new ArrayList<User>();
		try{
			ObjectInputStream users = new ObjectInputStream(a);
			while(users.available()>0) {
				temp.add((User)users.readObject());
			}
			users.close();
		}
		catch(Exception e) {
			a.close();
		}
		return temp;
	}*/

	/**
	 * initiates the login window
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param mainStage initial stage
	 */
	public void start(Stage mainStage)  throws Exception {
		/*try{
			FileInputStream userList = new FileInputStream("src/userlist.ser");
			users = readUsers(userList);
		}
		catch(FileNotFoundException err){
			File file = new File("src/userlist.ser");
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
		}*/
		
	}
}
