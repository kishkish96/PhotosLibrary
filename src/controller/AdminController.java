package controller;

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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import app.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;

public class AdminController{
	
	/**
	 * reference to admin class from Main photos file
	 */
	public static Admin admin = Photos.admin;
	
	/**
	 * list of users
	 */
	public static ArrayList<String> users = new ArrayList<>();
	
	/**
	 * display for list of users
	 */
	@FXML
	private ListView<String> userList;
	
	/**
	 * observable list of albums, used to populate userList
	 */
	private ObservableList<String> obsUserList = FXCollections.emptyObservableList();
	
	/**
	 * reference to rename user button
	 */
	@FXML 
	private Button renameUser;
	
	/**
	 * reference to create user button
	 */
	@FXML
	private Button createUser;
	
	/**
	 * reference to delete user button
	 */
	@FXML
	private Button deleteUser;
	
	/**
	 * reference to logout button
	 */
	@FXML 
	private Button logoutAdmin;
	
	/**
	 * gives only admin user option to rename user
	 * @param event clicking of the rename user button
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	@FXML
	private void renameClicked(ActionEvent event) throws IOException {
		int count = 0;
		int index = userList.getSelectionModel().getSelectedIndex();
		String userSelected = admin.getUsers().get(index).getUsername();
		TextInputDialog userDialog = new TextInputDialog(userSelected);
		userDialog.setHeaderText("Rename User");
		userDialog.setContentText("Username");
		Optional<String> user = userDialog.showAndWait();
		if(user.isPresent()) {
			String userUpdate = user.get().toString();
			for(int i=0;i<obsUserList.size();i++) {
				if(admin.getUsers().get(i).getUsername().toLowerCase().equals(userUpdate.toLowerCase())) {
					count = 1;
					Alert renameError = new Alert(AlertType.ERROR);
					renameError.setHeaderText("Error on Rename");
					renameError.setContentText("This username already exists");
					renameError.showAndWait();
					break;
				}
			}
			if(count == 0) {
				//userList.getSelectionModel().getSelectedItem().setUsername(user.get());
				int ind = userList.getSelectionModel().getSelectedIndex();
				admin.getUsers().get(index).setUsername(user.get().toString());
				updateUserList();
				Admin.write(admin);
			}
		}
	}
	
	/**
	 * gives only admin user option to create user 
	 * @param event clicking of create user button
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	@FXML
	private void createClicked(ActionEvent event) throws IOException {
		int count = 0;
		TextInputDialog userDialog = new TextInputDialog();
		userDialog.setHeaderText("Add User");
		userDialog.setContentText("Username");
		Optional<String> user = userDialog.showAndWait();
		if(user.isPresent()) {
			String userAdd = user.get().toString();
			for(int i=0;i<admin.getUsers().size();i++) {
				if(admin.getUsers().get(i).getUsername().toLowerCase().equals(userAdd.toLowerCase())) {
					count = 1;
					Alert renameError = new Alert(AlertType.ERROR);
					renameError.setHeaderText("Error on Add");
					renameError.setContentText("This username already exists");
					renameError.showAndWait();
					break;
				}
			}
			if(count == 0) {
				//obsUserList.addAll(new User(userAdd.toString()));
				admin.addUser(new User(userAdd));
				admin.getUser(userAdd).addAlbum(new Album("stock"));
				admin.getUser(userAdd).getAlbum("stock").addPhoto(new Photo(new File(".\\stock\\stock1.jpg")));
				admin.getUser(userAdd).getAlbum("stock").addPhoto(new Photo(new File(".\\stock\\stock2.jpg")));
				admin.getUser(userAdd).getAlbum("stock").addPhoto(new Photo(new File(".\\stock\\stock3.jpg")));
				admin.getUser(userAdd).getAlbum("stock").addPhoto(new Photo(new File(".\\stock\\stock4.jpg")));
				admin.getUser(userAdd).getAlbum("stock").addPhoto(new Photo(new File(".\\stock\\stock5.jpg")));
				admin.getUser(userAdd).getAlbum("stock").addPhoto(new Photo(new File(".\\stock\\stock6.jpg")));
				updateUserList();
				Admin.write(admin);
			}
		}
	}
	
	/**
	 * gives only the admin user option to delete user
	 * @param event clicking of delete user button
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	@FXML
	private void deleteClicked(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Delete Confirmation");
		alert.setContentText("Are you sure you want to delete this user?");
		Optional<ButtonType> btn = alert.showAndWait();
		if(btn.get() == ButtonType.CANCEL) {
			
		} else if(btn.get() == ButtonType.OK) {
			//obsUserList.remove(userList.getSelectionModel().getSelectedItem());
			int index = userList.getSelectionModel().getSelectedIndex();
			admin.removeUser(index);
			updateUserList();
			Admin.write(admin);
		}
	}
	
	/**
	 * calls to loginController/View
	 * @param event clicking of logout button
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	@FXML
	private void logoutClicked(ActionEvent event) throws IOException {
		GridPane grid = FXMLLoader.load(getClass().getClassLoader().getResource("view/LoginView.fxml"));
		Stage stage = new Stage();
		stage.setScene(new Scene(grid));
		stage.show();
		((Node)(event.getSource())).getScene().getWindow().hide();
	}
	
	/*private void updateUserList() {
		userList.setCellFactory(lv -> new ListCell<User>() {
		    @Override
		    public void updateItem(User item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty) {
		            setText(null);
		        } else {
		            String text = item.getUsername(); 
		            setText(text);
		        }
		    }
		});
	}*/
	
	/**
	 * updates list of users in userList
	 */
	private void updateUserList() {
		users.clear();
		for(int i=0;i<admin.getUsers().size();i++) {
			users.add(admin.getUsers().get(i).getUsername());
		}
		userList.refresh();
		obsUserList = FXCollections.observableArrayList(users);
		userList.setItems(obsUserList);
		userList.refresh();
	}
	
	/**
	 * initiates the admin window/view
	 * @param mainStage new window/stage
	 * @throws ClassNotFoundException Signals that a class was not found
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	public void start(Stage mainStage) throws ClassNotFoundException, IOException {
		//admin.addUser(new User("hello"));
		//Admin.writeUser(admin);
		
		updateUserList();
		
		//User.writeUser(obsUserList);
		
		//readUser();
		
		//obsUserList = User.readUser();
		
		//userList.setItems(obsUserList);
		
		//updateUserList();
		
		/*obsUserList = FXCollections.observableArrayList();
		
		userList.setItems(obsUserList);
		
		updateUserList();
		
		if(obsUserList.isEmpty()) {
			renameUser.setDisable(true);
			deleteUser.setDisable(true);
		} else {
			renameUser.setDisable(false);
			deleteUser.setDisable(false);
			userList.getSelectionModel().select(0);;
		}*/
		
	}
	
}
