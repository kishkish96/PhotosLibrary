package controller;

/**
 * 
 * @author Kishan Patel
 * @author Neal Patel
 *
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import app.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.stage.FileChooser;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;

public class CreateAlbumController {
	
	/**
	 * current user administrator
	 */
	public static Admin albumAdmin = Photos.admin;
	
	/**
	 * User object of current user
	 */
	public static User user;
	
	/**
	 * username of current user
	 */
	public static String username;
	
	/**
	 * display for list of photos
	 */
	@FXML
	private ListView<Photo> photoList;
	
	/**
	 * observable list of photos, used to populate photoList
	 */
	private ObservableList<Photo> obsPhotoList;
	
	/**
	 * list of photos
	 */
	private static ArrayList<Photo> photos = new ArrayList<>();
	
	/**
	 * display for the wanted name of the album
	 */
	@FXML
	private TextField newAlbumName;
	
	/**
	 * to add photo to view/window
	 * @param event clicking of add photo button
	 */
	@FXML
	private void addPhotoClicked(ActionEvent event) {
		FileChooser fc = new FileChooser();
		FileChooser.ExtensionFilter fce  = new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg");
		fc.getExtensionFilters().add(fce);
		File imageFile = fc.showOpenDialog(null);
		if(imageFile == null) {
			Alert imageAlert = new Alert(AlertType.ERROR);
			imageAlert.setHeaderText("Error on Adding Image");
			imageAlert.setContentText("Nothing in file");
			imageAlert.showAndWait();
		} else {
			Photo photo = new Photo(imageFile);
			photos.add(photo);
			photo.setDate(new Date(imageFile.lastModified()));
			photoList.refresh();
			obsPhotoList = FXCollections.observableArrayList(photos);
			photoList.setItems(obsPhotoList);
			photoList.setOrientation(Orientation.HORIZONTAL);
			photoList.setCellFactory(param -> new ListCell<Photo>() {
	            private ImageView imageView = new ImageView();
	            public void updateItem(Photo pic, boolean empty) {
	                super.updateItem(pic, empty);
	                if (empty) {
	                    setGraphic(null);
	                } else {
	                	//System.out.println(new Date(imageFile.lastModified()));
	                    imageView.setImage(new Image(pic.getFilepath().toURI().toString()));
	                    imageView.setFitWidth(300);
	                    imageView.setFitHeight(175);
	                    setGraphic(imageView);
	                }
	            }
	        });
			/*photoList.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>(){
				@Override
				public ListCell<Photo> call(ListView<Photo> p){
					return new EachPhoto();
				}
				
			});*/
			//updatePhotoList();
			//albumAdmin.getUser(username).getAlbum(albumName)
		}
	}
	
	/**
	 * to save photos into the album created and save the album to the current user
	 * @param event clicking of the save button
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	@FXML
	private void saveClicked(ActionEvent event) throws IOException {
		int count = 0;
		user = albumAdmin.getUser(username);
		for(int i=0;i<user.getAlbums().size();i++) {
			if(user.getAlbums().get(i).getAlbumName().equals(newAlbumName.getText().toString())) {
				count = 1;
				Alert addAlbumError = new Alert(AlertType.ERROR);
				addAlbumError.setHeaderText("Error on Add Album");
				addAlbumError.setContentText("This album already exists");
				addAlbumError.showAndWait();
				break;
			}
		}
		if(count == 0) {
			user.addAlbum(new Album(newAlbumName.getText().toString()));
			for(int i=0;i<photos.size();i++) {
				user.getAlbum(newAlbumName.getText().toString()).addPhoto(photos.get(i));
			}
			Admin.write(albumAdmin);
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
	
	/**
	 * calls the AlbumListController/View
	 * @param event clicking of the cancel button
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	@FXML
	private void cancelClicked(ActionEvent event) throws IOException {
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
	
	/*@Override
    public void initialize(URL location, ResourceBundle resources) {
        //File file = new File("C:\Users\kisha\OneDrive\Pictures\Saved Pictures\eagles.jpg");
        Image image = new Image("file:/C:/Users/kisha/OneDrive/Pictures/Saved%20Pictures/eagles2.jpg");
        imageView.setImage(image);
    }*/
	
	/*private void updatePhotoList() {
		photoList.setCellFactory(param -> new ListCell<Photo>() {
            private ImageView imageView = new ImageView();
            public void updateItem(Photo pic, boolean empty) {
            	//public Image image = new Image();
                super.updateItem(pic, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                	File file = pic.getFilepath();
                	Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                    imageView.setFitWidth(300);
                    imageView.setFitHeight(175);
                    setGraphic(imageView);
                }
            }
        });
	}*/

	/**
	 * initiates the create album window/view
	 * @param primaryStage new stage/window
	 */
	public void start(Stage primaryStage) {
		/*obsPhotoList = FXCollections.observableArrayList();
		
		photoList.setItems(obsPhotoList);
		photoList.setOrientation(Orientation.HORIZONTAL);
		
		updatePhotoList();*/
		
		/*File file = new File("C:\\Users\\kisha\\OneDrive\\Pictures\\Saved Pictures\\ru1.jpg");
		System.out.println(new Date(file.lastModified()));
		
		System.out.println();*/
		
	}

}
