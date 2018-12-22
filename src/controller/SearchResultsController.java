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

import app.Photos;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import model.Photo;

public class SearchResultsController {
	
	/**
	 * list of photos based on search
	 */
	public static ArrayList<Photo> searchResults;
	
	/**
	 * username of current user
	 */
	public static String username;
	
	/**
	 * the text of what was searched 
	 */
	public static String search;
	
	/**
	 * current user administrator
	 */
	public static Admin userAdmin = Photos.admin;
	
	/**
	 * display for photos based off search
	 */
	@FXML
	private ListView<Photo> searchPhotoList;
	
	/**
	 * observable list of searched photos, used to populate searchPhotoList
	 */
	private ObservableList<Photo> obsSearchPhotoList;
	
	/**
	 * to display the text of what was searched 
	 */
	@FXML
	private TextField searchText;
	
	/**
	 * to create album based on search results
	 * @param event clicking of create album button
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	@FXML
	private void createClicked(ActionEvent event) throws IOException {
		int count = 0;
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Create Album");
		dialog.setHeaderText("What do you want to name this album?");
		dialog.setContentText("Album Name: " );
		Optional<String> result  = dialog.showAndWait();
		System.out.println(result.get().toString());
		if(result.isPresent()) {
			for(int i=0;i<userAdmin.getUser(username).getAlbums().size();i++) {
				if(userAdmin.getUser(username).getAlbums().get(i).getAlbumName().equals(result.get().toString())) {
					count = 1;
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Error");
					error.setHeaderText("Album already exists");
					error.setContentText("Use a name that doesn't exist");
					error.showAndWait();
					break;
				}
			}
			if(count == 0) {
				userAdmin.getUser(username).addAlbum(new Album(result.get().toString()));
				for(int i=0;i<searchResults.size();i++) {
					userAdmin.getUser(username).getAlbum(result.get().toString()).addPhoto(searchResults.get(i));
				}
				Admin.write(userAdmin);
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
		}
	}
	
	/**
	 * calls the AlbumListController/View
	 * @param event clicking of the back button
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	@FXML
	private void backClicked(ActionEvent event) throws IOException{
		AlbumListController.username = username;
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
	
	/**
	 * updates list of photos searched in searchPhotoList
	 */
	private void updatePhotoList() {
		obsSearchPhotoList = FXCollections.observableArrayList(searchResults);
		searchPhotoList.setItems(obsSearchPhotoList);
		searchPhotoList.setOrientation(Orientation.HORIZONTAL);
		searchPhotoList.setCellFactory(listView -> new ListCell<Photo>() {
            private final ImageView imageView = new ImageView();
            public void updateItem(Photo pic, boolean empty) {
            	//private Image image = new Image
                super.updateItem(pic, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(pic.getFilepath().toURI().toString()));
                    imageView.setFitWidth(175);
                    imageView.setFitHeight(300);
                    setGraphic(imageView);
                }
            }
        });
	}
	
	/**
	 * initiates the search results window/view
	 * @param primaryStage new stage/window
	 */
	public void start(Stage primaryStage) {
		searchText.setText(search);
		updatePhotoList();
	}
	
}
