package controller;

/**
 * 
 * @author Kishan Patel
 * @author Neal Patel
 *
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.io.IOException;

import app.Photos;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Admin;
import model.Photo;
import model.User;

public class PhotoDisplayController {
	
	/**
	 * container where image will be displayed
	 */
	@FXML
	private ImageView imageView;
	
	/**
	 * text field for caption of a photo to display
	 */
	@FXML
	private TextField captionText;
	
	/**
	 * text field for tags of a photo to display
	 */
	@FXML
	private TextField tagsText; //maybe an arraylist
	
	/**
	 * text field for date of a photo to display
	 */
	@FXML
	private TextField dateText;
	
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
	 * list of a users in current album
	 */
	public static ArrayList<Photo> photos = new ArrayList<>();
	
	/**
	 * current user administrator
	 */
	public static Admin userAdmin = Photos.admin;
	
	/**
	 * calls on AlbumPhotoListController/View
	 * @param event clicking of the back button
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	@FXML
	private void backClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getClassLoader().getResource("view/AlbumPhotoListView.fxml"));
		GridPane grid = (GridPane)loader.load();
		AlbumPhotoListController aplc = loader.getController();
		Stage stage = new Stage();
		aplc.start(stage);
		stage.setTitle(userAdmin.getUser(username).getAlbum(album).getAlbumName());
		stage.setScene(new Scene(grid));
		stage.setResizable(false);
		stage.show();
		((Node)(event.getSource())).getScene().getWindow().hide();
	}
	
	/**
	 * initiates the photo display view/window
	 * @param primaryStage new stage/window
	 */
	public void start(Stage primaryStage) {
		File file = new File(photo.getFilepath().toString());
		Image image = new Image(file.toURI().toString());
		imageView.setImage(image);
		
		Optional<String> optionalCaption = Optional.ofNullable(photo.getCaption());
		if(optionalCaption.isPresent()){
			String split = optionalCaption.get().split("\\[",2)[1];
			captionText.setText(split.substring(0, split.length()-1));
		}
		else {
			captionText.setText("n/a");
		}
		captionText.setDisable(true);
		captionText.setOpacity(1.0);
		dateText.setText(photo.getDate().toString());
		dateText.setDisable(true);
		dateText.setOpacity(1.0);
		String tags = "";
		for(int i = 0; i<photo.getTags().size(); i++) {
			tags = photo.getTags().get(i).getValueTag() + ", " + tags;
			/*if(i == photo.getTags().size()-1) {
				tags = photo.getTags().get(i).getValueTag();
			}
			else {
				tags = photo.getTags().get(i).getValueTag() + ",";
			}*/
		}
		tagsText.setText(tags);
		tagsText.setDisable(true);
		tagsText.setOpacity(1.0);
		
		
	}
	

}
