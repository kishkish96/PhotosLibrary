package controller;

/**
 * 
 * @author Kishan Patel
 * @author Neal Patel
 *
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import app.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Admin;
import model.Photo;
import model.User;
import model.Tag;

public class TagController {
	
	/**
	 * container where image will be displayed
	 */
	@FXML
	private ImageView imageView;
	
	/**
	 * text field for a new tag
	 */
	@FXML
	private TextField newTag;

	/**
	 * User object of current user
	 */
	public static User user;
	
	/**
	 * username of current user
	 */
	public static String username;
	
	/**
	 * name of current album
	 */
	public static String album;
	
	/**
	 * Photo object for current photo
	 */
	public static Photo photo;

	/**
	 * list of a users tag keys
	 */
	public static ArrayList<String> tagKeys = new ArrayList();
	
	/**
	 * list of current photos tag values
	 */
	public static ArrayList<String> tagValues = new ArrayList();
	
	/**
	 * current user administrator
	 */
	public static Admin userAdmin = Photos.admin;
	
	/**
	 * observable list of a users tag keys, used to populate tagKeyList
	 */
	private ObservableList<String> obsTagKeys;
	
	/**
	 * observable list of current photos tag values, used to populate tagValueList
	 */
	private ObservableList<String> obsTagValues;
	
	/**
	 * list of current photos tag values that will be displayed
	 */
	@FXML
	private ListView<String> tagValueList;
	
	/**
	 * list of current users tag keys to be listed in the choice box
	 */
	@FXML
	private ChoiceBox<String> tagKeyList;
	
	/**
	 * allows for the addition of a new tag key
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of new tag type button
	 */
	@FXML
	private void newTagType(ActionEvent event) throws IOException{
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Tags");
		dialog.setHeaderText("New Tag Type");
		dialog.setContentText("Tag Type: " );
		Optional<String> result  = dialog.showAndWait();
		if(result.isPresent()) {
			boolean temp = false;
			for(int i = 0; i<userAdmin.getUser(username).getTagKeys().size();i++) {
				if(userAdmin.getUser(username).getTagKeys().get(i).toLowerCase().equals(result.get().toLowerCase())){
					temp = true;
				}
			}
			if(temp == false) {
				userAdmin.getUser(username).addTagKey(result.get());
				Admin.write(userAdmin);
				refreshTagKeys();
			}
			else if(temp == true) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Error");
				error.setHeaderText("Existing Tag Type");
				error.setContentText("This tag type already exists");
				error.showAndWait();
			}
		}
	}
	
	/**
	 * allows for the deletion of the currently selected tag
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of delete tag button
	 */
	@FXML
	private void deleteTag(ActionEvent event) throws IOException{
		try {
			photo.getTags().remove(tagValueList.getSelectionModel().getSelectedIndex());
			Admin.write(userAdmin);
			refreshTagValues();
		}
		catch(Exception e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No Tag Selected");
			error.setContentText("Please select a Tag");
			error.showAndWait();
		}
	}
	
	/**
	 * allows for the addition of a new tag 
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of add tag button
	 */
	@FXML
	private void addTag(ActionEvent event) throws IOException{
		if((newTag.getText() == null || newTag.getText().trim().isEmpty()) || tagKeyList.getSelectionModel().isEmpty()) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("Add Tag Error");
			error.setContentText("No tag type is selected or a new tag value has not been typed in.");
			error.showAndWait();
		}
		else {
			boolean a = false;
			for(int i =0; i<photo.getTags().size();i++) {
				if(photo.getTags().get(i).getKeyTag().toLowerCase().equals(tagKeyList.getSelectionModel().getSelectedItem().toLowerCase()) && photo.getTags().get(i).getValueTag().toLowerCase().equals(newTag.getText().toLowerCase())){
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Error");
					error.setHeaderText("Add Tag Error");
					error.setContentText("This tag-value pair already exists");
					error.showAndWait();
					a = true;
					break;
				}
			}
			if(a==false) {
				Tag tag = new Tag(tagKeyList.getSelectionModel().getSelectedItem(),newTag.getText());
				photo.addTag(tag);
				Admin.write(userAdmin);
				refreshTagValues();
			}
		}
	}
	
	/**
	 * calls on the AlbumPhotoListController/View
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of back button
	 */
	@FXML
	private void back(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getClassLoader().getResource("view/AlbumPhotoListView.fxml"));
		GridPane grid = (GridPane)loader.load();
		AlbumPhotoListController aplc = loader.getController();
		Stage stage = new Stage();
		aplc.start(stage);
		stage.setTitle(album);
		stage.setScene(new Scene(grid));
		stage.setResizable(false);
		stage.show();
		((Node)(event.getSource())).getScene().getWindow().hide();
	}
	
	/**
	 * refreshes the list of tag keys
	 */
	public void refreshTagKeys() {
		tagKeys.clear();
		for(int i = 0; i<userAdmin.getUser(username).getTagKeys().size();i++) {
			tagKeys.add(userAdmin.getUser(username).getTagKeys().get(i));
		}
		obsTagKeys = FXCollections.observableArrayList(tagKeys);
		tagKeyList.setItems(obsTagKeys);
	}
	
	/**
	 * refreshes the list of current photos tag values
	 */
	public void refreshTagValues() {
		tagValues.clear();
		for(int i = 0; i<photo.getTags().size(); i++) {
			tagValues.add(photo.getTags().get(i).getValueTag());
		}
		obsTagValues = FXCollections.observableArrayList(tagValues);
		tagValueList.setItems(obsTagValues);
	}
	
	/**
	 * initializes the tag controller window
	 * @param primaryStage new stage/window
	 */
	public void start(Stage primaryStage) {
		File file = new File(photo.getFilepath().toString());
		Image image = new Image(file.toURI().toString());
		imageView.setImage(image);
		
		refreshTagValues();
		refreshTagKeys();
	}
}
