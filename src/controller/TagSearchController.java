package controller;

/**
 * 
 * @author Kishan Patel
 * @author Neal Patel
 *
 */

import java.util.ArrayList;
import java.io.IOException;
import javafx.stage.Stage;

import app.Photos;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import model.Admin;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import model.Photo;
import javafx.scene.layout.Pane;

public class TagSearchController {
	
	/**
	 * Admin object
	 */
	public static Admin userAdmin = Photos.admin;
	
	/**
	 * username of current user
	 */
	public static String username;
	
	/**
	 * list of values for conjunctive/disjunctive search
	 */
	public static ArrayList<String> andor = new ArrayList<String>();
	
	/**
	 * list of current users tag keys
	 */
	public static ArrayList<String> tagKeys = new ArrayList<String>();
	
	/**
	 * observable list of current users tag keys, used to populate firstTagKey and secondTagKey
	 */
	private ObservableList<String> obsTagKeys;
	
	/**
	 * observable list of values for conjunctive/disjunctive search, used to populate andOrList
	 */
	private ObservableList<String> obsAndOr;
	
	/**
	 * choice box for and/or values
	 */
	@FXML
	private ChoiceBox<String> andOrList;
	
	/**
	 * choice box for users tag key values
	 */
	@FXML
	private ChoiceBox<String> firstTagKey;
	
	/**
	 * choice box for users tag key values
	 */
	@FXML
	private ChoiceBox<String> secondTagKey;
	
	/**
	 * text field to search for a tag value
	 */
	@FXML
	private TextField firstTagValue;
	
	/**
	 * text field to search for a second tag value (used when an option is selected in andOrList)
	 */
	@FXML
	private TextField secondTagValue;
	
	/**
	 * searches all of the current users photos based on tag search parameters
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of search button
	 */
	@FXML
	private void searchByTag(ActionEvent event) throws IOException {
		ArrayList<Photo> searchResults = new ArrayList<Photo>();
		if(andOrList.getSelectionModel().isEmpty()) {
			if(firstTagKey.getSelectionModel().isEmpty() || (firstTagValue.getText() == null || firstTagValue.getText().trim().isEmpty())) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Error");
				error.setHeaderText("Tag Search Error");
				error.setContentText("No tag type is selected or a tag value has not been entered.");
				error.showAndWait();
			}
			else {
				for(int i = 0; i<userAdmin.getUser(username).getAlbums().size();i++) {
					for(int j =0; j<userAdmin.getUser(username).getAlbums().get(i).getPhotos().size();j++) {
						for(int k = 0; k<userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().size(); k++){
							if(userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().get(k).getKeyTag().toLowerCase().equals(firstTagKey.getSelectionModel().getSelectedItem().toLowerCase()) && userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().get(k).getValueTag().toLowerCase().equals(firstTagValue.getText().toLowerCase())) {
								searchResults.add(userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j));
								break;
							}
						}
					}
				}
			}
		}
		else {
			if(firstTagKey.getSelectionModel().isEmpty() || (firstTagValue.getText() == null || firstTagValue.getText().trim().isEmpty()) || secondTagKey.getSelectionModel().isEmpty() || (secondTagValue.getText() == null || secondTagValue.getText().trim().isEmpty()) ) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Error");
				error.setHeaderText("Tag Search Error");
				error.setContentText("Tag type or Tag Values are empty.");
				error.showAndWait();
			}
			else {
				if(andOrList.getSelectionModel().getSelectedItem().equals("or")){
					for(int i = 0; i<userAdmin.getUser(username).getAlbums().size();i++) {
						for(int j =0; j<userAdmin.getUser(username).getAlbums().get(i).getPhotos().size();j++) {
							for(int k = 0; k<userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().size(); k++){
								if((userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().get(k).getKeyTag().toLowerCase().equals(firstTagKey.getSelectionModel().getSelectedItem().toLowerCase())
									&& userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().get(k).getValueTag().toLowerCase().equals(firstTagValue.getText().toLowerCase())) 
									|| userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().get(k).getKeyTag().toLowerCase().equals(secondTagKey.getSelectionModel().getSelectedItem().toLowerCase())
									&& userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().get(k).getValueTag().toLowerCase().equals(secondTagValue.getText().toLowerCase())){
									searchResults.add(userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j));
									break;
								}
							}
						}
					}
				}
				else {
					for(int i = 0; i<userAdmin.getUser(username).getAlbums().size();i++) {
						for(int j =0; j<userAdmin.getUser(username).getAlbums().get(i).getPhotos().size();j++) {
							for(int k = 0; k<userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().size(); k++){
								if(userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().get(k).getKeyTag().toLowerCase().equals(firstTagKey.getSelectionModel().getSelectedItem().toLowerCase())
										&& userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().get(k).getValueTag().toLowerCase().equals(firstTagValue.getText().toLowerCase())){
									for(int l=0;l<userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().size(); l++) {
										if(userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().get(l).getKeyTag().toLowerCase().equals(secondTagKey.getSelectionModel().getSelectedItem().toLowerCase())
												&& userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j).getTags().get(l).getValueTag().toLowerCase().equals(secondTagValue.getText().toLowerCase())) {
											searchResults.add(userAdmin.getUser(username).getAlbums().get(i).getPhotos().get(j));
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if(searchResults.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Search Results");
			alert.setHeaderText("Search Results");
			alert.setContentText("No photos were found that match your criteria.");
			alert.showAndWait();
		}
		else {
			for(int i = 0; i<searchResults.size()-1;i++) {
				for(int j =i+1;j<searchResults.size();j++) {
					if(searchResults.get(i).getFilepath().equals(searchResults.get(j).getFilepath())){
						searchResults.remove(j);
					}
				}
			}
			SearchResultsController.search = firstTagKey.getValue().toString() + ":" + firstTagValue.getText().toString() + " " + andOrList.getValue().toString() + " " + secondTagKey.getValue().toString() + ":" + secondTagValue.getText().toString(); 
			SearchResultsController.username = username;
			SearchResultsController.searchResults = searchResults;
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
	 * calls on the AlbumListController/View
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param event clicking of back button
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
	 * initializes the tag search window
	 * @param stage new stage
	 */
	public void start(Stage stage) {
		tagKeys.clear();
		andor.clear();
		for(int i = 0; i<userAdmin.getUser(username).getTagKeys().size();i++) {
			tagKeys.add(userAdmin.getUser(username).getTagKeys().get(i));
		}
		obsTagKeys = FXCollections.observableArrayList(tagKeys);
		firstTagKey.setItems(obsTagKeys);
		secondTagKey.setItems(obsTagKeys);
		andor.add("or");
		andor.add("and");
		obsAndOr = FXCollections.observableArrayList(andor);
		andOrList.setItems(obsAndOr);
	}
}
