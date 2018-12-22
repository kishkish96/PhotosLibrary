package controller;

/**
 * 
 * @author Kishan Patel
 * @author Neal Patel
 *
 */

import app.Photos;
import java.io.IOException;
import java.util.Optional;
import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import java.util.ArrayList;
import model.Photo;

public class CopyMoveController {
	
	/**
	 * display for list of albums
	 */
	@FXML
	private ListView<String> albumList;
	
	/**
	 * observable list of albums, used to populate albumList
	 */
	private ObservableList<String> obsAlbumList;
	
	/**
	 * list of albums
	 */
	public static ArrayList<String> albumNames = new ArrayList<>();
	
	/**
	 * current user administrator
	 */
	public static Admin userAdmin = Photos.admin;
	
	/**
	 * username of current user
	 */
	public static String username;
	
	/**
	 * name of current album
	 */
	public static String album;
	
	/**
	 * Photo object of current photo
	 */
	public static Photo photo;
	
	/**
	 * index of current photo
	 */
	public static int photoIndex;
	
	/**
	 * view to display a photo
	 */
	@FXML
	private ImageView imageView;
	
	/**
	 * calls the AlbumPhotoListController/View
	 * @param event clicking of the back button
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	@FXML
	private void back(ActionEvent event) throws IOException{
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
	 * to move a photo from one album to another, calls the AlbumPhotoListController/View
	 * @param event clicking of move button
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	@FXML
	private void move(ActionEvent event) throws IOException{
		if(albumList.getSelectionModel().isEmpty()) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No Album Selected");
			error.setContentText("Please select an Album to move this photo to.");
			error.showAndWait();
		}
		else {
			userAdmin.getUser(username).getAlbum(albumList.getSelectionModel().getSelectedItem()).addPhoto(photo);
			userAdmin.getUser(username).getAlbum(album).getPhotos().remove(photoIndex);
			Admin.write(userAdmin);
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
		
	}
	
	/**
	 * to copy a photo from one album to another, calls the AlbumPhotoListController/View
	 * @param event clicking of the copy button
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	@FXML
	private void copy(ActionEvent event) throws IOException {
		if(albumList.getSelectionModel().isEmpty()) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No Album Selected");
			error.setContentText("Please select an Album to copy this photo to.");
			error.showAndWait();
		}
		else {
			userAdmin.getUser(username).getAlbum(albumList.getSelectionModel().getSelectedItem()).addPhoto(photo);
			Admin.write(userAdmin);
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
	}
	
	/**
	 * initiates the copy move window/view
	 * @param primaryStage new stage/window
	 */
	public void start(Stage primaryStage) {
		albumNames.clear();
		for(int i = 0; i<userAdmin.getUser(username).getAlbums().size();i++) {
			albumNames.add(userAdmin.getUser(username).getAlbums().get(i).getAlbumName());
		}
		obsAlbumList = FXCollections.observableArrayList(albumNames);
		albumList.setItems(obsAlbumList);
		
		File file = new File(photo.getFilepath().toString());
		Image image = new Image(file.toURI().toString());
		imageView.setImage(image);
		
	}
	
}
