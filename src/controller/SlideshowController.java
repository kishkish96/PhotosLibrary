package controller;

/**
 * 
 * @author Kishan Patel
 * @author Neal Patel
 *
 */

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

import app.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Admin;
import model.Photo;
import model.User;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class SlideshowController {
	
	/**
	 * admin class
	 */
	public static Admin admin = Photos.admin;
	
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
	 * list of photos in current album
	 */
	public static ArrayList<Photo> photos = new ArrayList<>();
	
	/**
	 * current user administrator
	 */
	public static Admin userAdmin = Photos.admin;
	
	/**
	 * index of currently displayed photo
	 */
	public static int photoIndex = 0;
	
	/**
	 * button to go to previous photo
	 */
	@FXML 
	private Button previousButton;
	
	/**
	 * button to go to next photo
	 */
	@FXML 
	private Button nextButton;
	
	/**
	 * control which holds the current image
	 */
	@FXML
	private ImageView imageView;
	
	/**
	 * calls on the AlbumPhotoListController/View
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of back button
	 */
	@FXML
	private void back(ActionEvent event) throws IOException {
		AlbumPhotoListController.username = username;
		AlbumPhotoListController.album = album;
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
	 * goes to the previous image in the album
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of previous button
	 */
	@FXML
	private void previous(ActionEvent event) throws IOException {
		SlideshowController.photoIndex = SlideshowController.photoIndex-1;
		if(SlideshowController.photoIndex==0) {
			previousButton.setDisable(true);
		}
		nextButton.setDisable(false);
		File file = new File(userAdmin.getUser(username).getAlbum(album).getPhotos().get(photoIndex).getFilepath().toString());
		Image image = new Image(file.toURI().toString());
		imageView.setImage(image);
	}
	
	/**
	 * goes to the next image in the album
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of next button
	 */
	@FXML
	private void next(ActionEvent event) throws IOException {
		SlideshowController.photoIndex = SlideshowController.photoIndex+1;
		if(SlideshowController.photoIndex == userAdmin.getUser(username).getAlbum(album).getPhotos().size()-1) {
			nextButton.setDisable(true);
		}
		previousButton.setDisable(false);
		File file = new File(userAdmin.getUser(username).getAlbum(album).getPhotos().get(photoIndex).getFilepath().toString());
		Image image = new Image(file.toURI().toString());
		imageView.setImage(image);
	}
	
	/**
	 * initiates the slideshow window
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param mainStage new stage
	 */
	public void start(Stage mainStage) throws IOException{
		File file = new File(userAdmin.getUser(username).getAlbum(album).getPhotos().get(photoIndex).getFilepath().toString());
		Image image = new Image(file.toURI().toString());
		imageView.setImage(image);
		if(userAdmin.getUser(username).getAlbum(album).getPhotos().size() == 1) {
			previousButton.setDisable(true);
			nextButton.setDisable(true);
		}
		else {
			previousButton.setDisable(true);
		}
	}
	
	
}
