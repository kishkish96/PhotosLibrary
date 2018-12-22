package controller;

/**
 * 
 * @author Kishan Patel
 * @author Neal Patel
 *
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import app.Photos;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import model.Photo;
import model.User;

public class AlbumListController {
	
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
	 * list of albums of the current user
	 */
	public static ArrayList<String> albums = new ArrayList<>();
	
	/**
	 * current user administrator
	 */
	public static Admin userAdmin = Photos.admin;
	
	/**
	 * start date picker to search by date
	 */
	@FXML
	private DatePicker startdate;
	
	/**
	 * end date picker to search by date
	 */
	@FXML
	private DatePicker enddate;
	
	/**
	 * list of albums that will be displayed
	 */
	@FXML
	private ListView<String> albumList;
	
	/**
	 * observable list of albums, used to populate albumList
	 */
	private ObservableList<String> obsAlbumList = FXCollections.emptyObservableList();
	
	/**
	 * calls the CreateAlbumController/View
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of the create album button
	 */
	@FXML
	private void addAlbum(ActionEvent event) throws IOException {
		CreateAlbumController.username = username;
		Pane grid = FXMLLoader.load(getClass().getClassLoader().getResource("view/CreateAlbumView.fxml"));
		Stage stage = new Stage();
		stage.setScene(new Scene(grid));
		stage.show();
		((Node)(event.getSource())).getScene().getWindow().hide();
	
		
		/*int count = 0;
		TextInputDialog userDialog = new TextInputDialog();
		userDialog.setHeaderText("Add Album");
		userDialog.setContentText("Album");
		Optional<String> album = userDialog.showAndWait();
		user = userAdmin.getCurrentUser();
		if(album.isPresent()) {
			String albumAdd = album.get().toString();
			for(int i=0;i<user.getAlbums().size();i++) {
				if(user.getAlbums().get(i).getAlbumName().toLowerCase().equals(albumAdd.toLowerCase())) {
					count = 1;
					Alert renameError = new Alert(AlertType.ERROR);
					renameError.setHeaderText("Error on Add");
					renameError.setContentText("This album already exists");
					renameError.showAndWait();
					break;
				}
			}
			if(count == 0) {
				userAdmin.getCurrentUser().addAlbum(new Album(albumAdd));
				updateAlbumList();
				Admin.write(userAdmin);
			}
		}*/
	}
	
	/**
	 * gives user the option to delete an album
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of the delete album button
	 */
	@FXML
	private void deleteAlbum(ActionEvent event) throws IOException {
		if(albumList.getSelectionModel().isEmpty()) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No Album Selected");
			error.setContentText("Please select an album.");
			error.showAndWait();
		}
		else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Delete Confirmation");
			alert.setContentText("Are you sure you want to delete this album?");
			Optional<ButtonType> btn = alert.showAndWait();
	
			if(btn.get() == ButtonType.CANCEL) {
	
			}
	
			else {
				user = userAdmin.getUser(username);
				String[] result = albumList.getSelectionModel().getSelectedItem().split("\n", 2);
				for(int i = 0; i<user.albums.size(); i++) {
					if(user.getAlbums().get(i).getAlbumName().equals(result[0])) {
						user.getAlbums().remove(i);
						Admin.write(userAdmin);
					}
				}
				updateAlbumList();
			}
		}
	}
	
	/**
	 * gives user the option to rename an album
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of rename album button
	 */
	@FXML
	private void renameAlbum(ActionEvent event) throws IOException {
		try {
			String[] replace = albumList.getSelectionModel().getSelectedItem().split("\n", 2);
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Rename Album");
			dialog.setHeaderText("Rename Selected Album");
			dialog.setContentText("Rename album to: " );
			Optional<String> result  = dialog.showAndWait();
			if(result.isPresent()) {
				user = userAdmin.getUser(username);
				user.getAlbum(replace[0]).setName(result.get());
				Admin.write(userAdmin);
				updateAlbumList();
			}
		}
		catch(Exception e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No Album Selected");
			error.setContentText("Please select an album.");
			error.showAndWait();
		}
	}
	
	/**
	 * calls the AlbumPhotoListController/View
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of open album button
	 */
	@FXML
	private void openAlbum(ActionEvent event) throws IOException{
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("view/AlbumPhotoListView.fxml"));
			GridPane grid = (GridPane)loader.load();
			AlbumPhotoListController.album = albumList.getSelectionModel().getSelectedItem().split("/n",2)[0];
			AlbumPhotoListController.username = username;
			AlbumPhotoListController aplc = loader.getController();
			Stage stage = new Stage();
			aplc.start(stage);
			stage.setTitle(albumList.getSelectionModel().getSelectedItem().split("/n",2)[0]);
			stage.setScene(new Scene(grid));
			stage.setResizable(false);
			stage.show();
			((Node)(event.getSource())).getScene().getWindow().hide();
		}
		catch(Exception e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("No Album Selected");
			error.setContentText("Please select an album.");
			error.showAndWait();
		}
	}

	/**
	 * calls the TagSearchController/View
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of search by tag button
	 */
	@FXML
	private void tagSearch(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getClassLoader().getResource("view/TagSearchView.fxml"));
		GridPane grid = (GridPane)loader.load();
		TagSearchController.username = username;
		TagSearchController tsc = loader.getController();
		Stage stage = new Stage();
		tsc.start(stage);
		stage.setTitle("Search Photos");
		stage.setScene(new Scene(grid));
		stage.setResizable(false);
		stage.show();
		((Node)(event.getSource())).getScene().getWindow().hide();
	}
	
	/**
	 * searches for photos within the specified date range and calls on SearchResultsController/View
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of search by date button
	 */
	@FXML 
	private void dateSearch(ActionEvent event) throws IOException {
		int count = 0;
		ArrayList<Photo> searchDate = new ArrayList<Photo>();
		for(int i=0;i<userAdmin.getUser(username).getAlbums().size();i++) {
			for(int j =0; j<userAdmin.getUser(username).getAlbums().get(i).getPhotos().size();j++) {
				if((startdate.getValue().getYear() < enddate.getValue().getYear()) 
						&& ((userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getYear() > startdate.getValue().getYear() - 1900 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getYear() < enddate.getValue().getYear() - 1900)
						|| (userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getYear() == startdate.getValue().getYear() - 1900 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getMonth() > startdate.getValue().getMonthValue() - 1)
						|| (userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getYear() == enddate.getValue().getYear() - 1900 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getMonth() < enddate.getValue().getMonthValue() - 1)
						|| (userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getYear() == startdate.getValue().getYear() - 1900 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getMonth() == startdate.getValue().getMonthValue() - 1 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getDate() >= startdate.getValue().getDayOfMonth())
						|| (userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getYear() == enddate.getValue().getYear() - 1900 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getMonth() == enddate.getValue().getMonthValue() - 1 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getDate() <= startdate.getValue().getDayOfMonth()))) {
					searchDate.add(userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j));
				}
				else if((startdate.getValue().getYear() == enddate.getValue().getYear() && startdate.getValue().getMonthValue() < enddate.getValue().getMonthValue())
						&& ((userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getYear() == startdate.getValue().getYear() - 1900 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getMonth() > startdate.getValue().getMonthValue() - 1 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getMonth() < enddate.getValue().getMonthValue() - 1)
						|| (userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getYear() == startdate.getValue().getYear() - 1900 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getMonth() == startdate.getValue().getMonthValue() - 1 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getMonth() < enddate.getValue().getMonthValue() - 1 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getDate() >= startdate.getValue().getDayOfMonth())
						|| (userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getYear() == startdate.getValue().getYear() - 1900 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getMonth() > startdate.getValue().getMonthValue() - 1 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getMonth() == enddate.getValue().getMonthValue() - 1 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getDate() <= enddate.getValue().getDayOfMonth()))) {
					searchDate.add(userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j));
				} 
				else if((startdate.getValue().getYear() == enddate.getValue().getYear() && startdate.getValue().getMonthValue() == enddate.getValue().getMonthValue() && startdate.getValue().getDayOfMonth() < enddate.getValue().getDayOfMonth())
						&& (userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getYear() == startdate.getValue().getYear() - 1900 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getMonth() == startdate.getValue().getMonthValue() - 1 && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getDate() <= enddate.getValue().getDayOfMonth() && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getDate().getDate() >= startdate.getValue().getDayOfMonth())) {
					searchDate.add(userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j));
				} else if((startdate.getValue().getYear() > enddate.getValue().getYear())
						|| (startdate.getValue().getYear() == enddate.getValue().getYear() && startdate.getValue().getMonthValue() > enddate.getValue().getMonthValue())
						|| (startdate.getValue().getYear() == enddate.getValue().getYear() && startdate.getValue().getMonthValue() == enddate.getValue().getMonthValue() && startdate.getValue().getDayOfMonth() > enddate.getValue().getDayOfMonth())){
					count = 1;
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Error");
					error.setHeaderText("Date Search Error");
					error.setContentText("Date Format is Incorrect");
					error.showAndWait();
					break;
				}
			}
		}
		if(searchDate.isEmpty() && count == 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Search Results");
			alert.setHeaderText("Search Results");
			alert.setContentText("No photos were found that match your criteria.");
			alert.showAndWait();
		} else if (count == 0){
			SearchResultsController.username = username;
			SearchResultsController.search = startdate.getValue().toString() + " to " + enddate.getValue().toString();
			SearchResultsController.searchResults = searchDate;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getClassLoader().getResource("view/SearchResultsView.fxml"));
			Pane pane = (Pane)loader.load();
			SearchResultsController src = loader.getController();
			Stage stage = new Stage();
			src.start(stage);
			stage.setTitle("Search Results");
			stage.setScene(new Scene(pane));
			stage.setResizable(false);
			stage.show();
			((Node)(event.getSource())).getScene().getWindow().hide();
		}
	}
	
	/**
	 * calls on the loginController/View
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of logout button
	 */
	@FXML
	private void logoutClicked(ActionEvent event) throws IOException {
		GridPane grid = FXMLLoader.load(getClass().getClassLoader().getResource("view/LoginView.fxml"));
		Stage stage = new Stage();
		stage.setScene(new Scene(grid));
		stage.show();
		((Node)(event.getSource())).getScene().getWindow().hide();
	}
	
	/**
	 * updates list of albums in albumList
	 */
	private void updateAlbumList() {
		user = userAdmin.getUser(username);
		albums.clear();
		for(int i=0;i<user.getAlbums().size();i++) {
			albums.add(user.getAlbums().get(i).getAlbumName());
		}
		albumList.refresh();
		obsAlbumList = FXCollections.observableArrayList(albums);
		albumList.setItems(obsAlbumList);
		albumList.setOrientation(Orientation.HORIZONTAL);
		albumList.refresh();
		
		albumList.setCellFactory(param -> new ListCell<String>() {
			private Text ta = new Text();
			public void updateItem(String alb, boolean empty) {
				super.updateItem(alb, empty);
				if(empty) {
					setGraphic(null);
				} else  {
					setPrefWidth(200);
					ta.setText(alb + "\n" + user.getAlbum(alb).getPhotos().size() + "\n" + user.getAlbum(alb).getStartDate() + "\n" + "to" + "\n"  + user.getAlbum(alb).getEndDate());
					ta.setTextAlignment(TextAlignment.CENTER);
					setGraphic(ta);
				}
			}
		});
	}
	
	/**
	 * initiates the album list window
	 * @param stage new stage
	 */
	public void start(Stage stage){
		updateAlbumList();
		
		//user = Photos.admin.getCurrentUser();
		//user.addAlbum(new Album("Hello"));
		//User.write(user);
		//System.out.println(User.read().getAlbums().size());
		/*albumList.refresh();
		albums.clear();
		//System.out.println(Photos.admin.getCurrentUser().getUsername());
		//System.out.println(user.getAlbums().size());
		//System.out.println(user.getAlbums().size());
		for(int i=0;i<Admin.readUser().getCurrentUser().getAlbums().size();i++) {
			albums.add(Admin.readUser().getCurrentUser().getAlbums().get(i).getAlbumName());
			user.addAlbum(Admin.readUser().getCurrentUser().getAlbums().get(i).getAlbumName());
		}
		obsAlbumList = FXCollections.observableArrayList(albums);
		albumList.setItems(obsAlbumList);
		albumList.refresh();*/
	}
	
	
}
