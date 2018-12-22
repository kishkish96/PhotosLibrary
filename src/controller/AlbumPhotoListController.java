package controller;

/**
 * 
 * @author Kishan Patel
 * @author Neal Patel
 *
 */

import java.io.File;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import app.Photos;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Admin;
import model.User;
import model.Album;
import model.Photo;

public class AlbumPhotoListController {
	
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
	 * observable list of photos, used to populate photoList
	 */
	private ObservableList<Photo> obsPhotoList;
	
	/**
	 * list of photos to be displayed
	 */
	@FXML
	private ListView<Photo> photoList;
	
	/**
	 * calls on AlbumListController/View
	 * @param event clicking of the back button
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	@FXML
	private void back(ActionEvent event) throws IOException {
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
	 * calls on LoginController/View
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of logout button
	 */
	@FXML
	private void logout(ActionEvent event) throws IOException {
		GridPane grid = FXMLLoader.load(getClass().getClassLoader().getResource("view/LoginView.fxml"));
		Stage stage = new Stage();
		stage.setScene(new Scene(grid));
		stage.show();
		((Node)(event.getSource())).getScene().getWindow().hide();
	}
	
	/**
	 * allows user to add photos to the current album
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of add photo button
	 */
	@FXML
	private void add(ActionEvent event) throws IOException {
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
			userAdmin.getUser(username).getAlbum(album).getPhotos().add(photo);
			Admin.write(userAdmin);
			photoList.refresh();
			updatePhotoList();
		}
	}
	
	/**
	 * allows user to delete currently selected photo
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of delete photo button
	 */
	@FXML
	private void delete(ActionEvent event) throws IOException {
		if(photoList.getSelectionModel().isEmpty()) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No Photo Selected");
			error.setContentText("Please select a photo.");
			error.showAndWait();
		}
		else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Delete Confirmation");
			alert.setContentText("Are you sure you want to delete this photo");
			Optional<ButtonType> btn = alert.showAndWait();
			if(btn.get() == ButtonType.CANCEL) {
				
			}
			else {
				userAdmin.getUser(username).getAlbum(album).removePhoto(photoList.getSelectionModel().getSelectedItem());
				Admin.write(userAdmin);
				updatePhotoList();
			}
		}
	}
	
	/**
	 * allows user to add or edit caption of currently selected photo
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of add caption/recaption button
	 */
	@FXML
	private void editCaption(ActionEvent event) throws IOException {
		if(photoList.getSelectionModel().isEmpty()) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No Photo Selected");
			error.setContentText("Please select a photo.");
			error.showAndWait();
		}
		else {
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Caption");
			dialog.setHeaderText("Caption or Recaption Your Photo");
			dialog.setContentText("Caption: " );
			Optional<String> result  = dialog.showAndWait();
			if(result.isPresent()) {
				for(int i = 0; i<userAdmin.getUser(username).getAlbum(album).getPhotos().size();i++) {
					if(userAdmin.getUser(username).getAlbum(album).getPhotos().get(i).getFilepath().toURI().toString().equals(photoList.getSelectionModel().getSelectedItem().getFilepath().toURI().toString())) {
						userAdmin.getUser(username).getAlbum(album).getPhotos().get(i).setCaption(result.toString());
					}
				}
				updatePhotoList();
				Admin.write(userAdmin);
			}
		}
	}
	
	/**
	 * calls on TagController/View for currently selected photo
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of add/edit tag button
	 */
	@FXML
	private void editTags(ActionEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("view/TagView.fxml"));
			GridPane grid = (GridPane)loader.load();
			TagController.photo = photoList.getSelectionModel().getSelectedItem();
			TagController.username = username;
			TagController.album = album;
			TagController tc = loader.getController();
			Stage stage = new Stage();
			tc.start(stage);
			stage.setTitle("Tags");
			stage.setScene(new Scene(grid));
			stage.setResizable(false);
			stage.show();
			((Node)(event.getSource())).getScene().getWindow().hide();
		}
		catch(Exception e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No Photo Selected");
			error.setContentText("Please select a photo.");
			error.showAndWait();
		}
	}
	
	/**
	 * calls on SlideshowController/View for current album
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of slideshow button
	 */
	@FXML
	private void slideshow(ActionEvent event) throws IOException {
		if(userAdmin.getUser(username).getAlbum(album).getPhotos().size() == 0 ) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No Photos");
			error.setContentText("This album has no photos to view");
			error.showAndWait();
		}
		else {
			SlideshowController.username = username;
			SlideshowController.album = album;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("view/SlideshowView.fxml"));
			GridPane grid = (GridPane)loader.load();
			SlideshowController sc = loader.getController();
			Stage stage = new Stage();
			sc.start(stage);
			stage.setTitle("Slideshow - " + userAdmin.getUser(username).getAlbum(album).getAlbumName());
			stage.setScene(new Scene(grid));
			stage.setResizable(false);
			stage.show();
			((Node)(event.getSource())).getScene().getWindow().hide();
		}
	}
	
	/**
	 * calls on the PhotoDisplayController/View for currently selected photo
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of open album button
	 */
	@FXML
	private void open(ActionEvent event) throws IOException {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("view/PhotoDisplayView.fxml"));
			GridPane grid = (GridPane)loader.load();
			PhotoDisplayController.photo = photoList.getSelectionModel().getSelectedItem();
			PhotoDisplayController.username = username;
			PhotoDisplayController.album = album;
			PhotoDisplayController pdc = loader.getController();
			Stage stage = new Stage();
			pdc.start(stage);
			stage.setTitle("Photo Display");
			stage.setScene(new Scene(grid));
			stage.setResizable(false);
			stage.show();
			((Node)(event.getSource())).getScene().getWindow().hide();
		}
		catch(Exception e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No Photo Selected");
			error.setContentText("Please select a photo.");
			error.showAndWait();
		}
	}
	
	/**
	 * calls on the CopyMoveView/Controller for currently selected photo
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of copy/move button
	 */
	@FXML
	private void copymove(ActionEvent event) throws IOException {
		if(photoList.getSelectionModel().isEmpty()) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No Photo Selected");
			error.setContentText("Please select a photo.");
			error.showAndWait();
		}
		else {
			try{
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getClassLoader().getResource("view/CopyMoveView.fxml"));
				GridPane grid = (GridPane)loader.load();
				CopyMoveController.album = album;
				CopyMoveController.username = username;
				CopyMoveController.photo = photoList.getSelectionModel().getSelectedItem();
				CopyMoveController.photoIndex = photoList.getSelectionModel().getSelectedIndex();
				CopyMoveController cmc = loader.getController();
				Stage stage = new Stage();
				cmc.start(stage);
				stage.setTitle("Copy or Move Photo");
				stage.setScene(new Scene(grid));
				stage.setResizable(false);
				stage.show();
				((Node)(event.getSource())).getScene().getWindow().hide();
			}
			catch(Exception e) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Error");
				error.setHeaderText("No Photo Selected");
				error.setContentText("Please select a photo.");
				error.showAndWait();
			}
		}

	}
	
	/**
	 * updates the list of displayed photos
	 */
	private void updatePhotoList() {
		obsPhotoList = FXCollections.observableArrayList(userAdmin.getUser(username).getAlbum(album).getPhotos());
		photoList.setItems(obsPhotoList);
		photoList.setOrientation(Orientation.HORIZONTAL);
		photoList.setCellFactory(listView -> new ListCell<Photo>() {
			private final ImageView imageView = new ImageView();
			private Label ta = new Label();
            public void updateItem(Photo pic, boolean empty) {
            	//private Image image = new Image
                super.updateItem(pic, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                	VBox vbox = new VBox();
                	imageView.setImage(new Image(pic.getFilepath().toURI().toString()));
                	imageView.setFitWidth(300);
                    imageView.setFitHeight(300);
                    Optional<String> optionalCaption = Optional.ofNullable(pic.getCaption());
            		if(optionalCaption.isPresent()){
            			String split = optionalCaption.get().split("\\[",2)[1];
            			ta.setText(split.substring(0, split.length()-1));
            		}
            		else {
            			ta.setText("");
            		}
                    ta.setTextAlignment(TextAlignment.CENTER);
                    vbox.getChildren().addAll(imageView,ta);
                    vbox.setAlignment(Pos.CENTER);
                    setGraphic(vbox);
                }
            }
        });
		
	}
	
	/**
	 * initiates the album photo list window
	 * @param stage new stage
	 */
	public void start(Stage stage){
		updatePhotoList();
		
	}
	
}
