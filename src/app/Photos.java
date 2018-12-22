package app;

/**
 * 
 * @author Kishan Patel
 * @author Neal Patel
 *
 */

import java.io.IOException;

import controller.AdminController;
import controller.CreateAlbumController;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Admin;

public class Photos extends Application{
	
	/**
	 * Admin Object thats will administrate application
	 */
	public static Admin admin = new Admin();
	
	/**
	 * initiates the login window
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 * @param primaryStage new stage/window
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/LoginView.fxml"));
		//loader.setLocation(getClass().getResource("/view/AdminView.fxml"));
		//loader.setLocation(getClass().getResource("/view/CreateAlbumView.fxml"));
		GridPane grid = (GridPane)loader.load();
		
		LoginController slc = loader.getController();
		//AdminController slc = loader.getController();
		//CreateAlbumController slc = loader.getController();
		slc.start(primaryStage);
		
		Scene scene = new Scene(grid);
		primaryStage.setTitle("Photo Library");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		try {
			Admin.write(admin);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * main method for starting the application
	 * @param args main method parameter
	 * @throws ClassNotFoundException Signals class not found
	 * @throws IOException Signals that an I/O exception of some sort has occurred
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		try {
			admin = Admin.read();
		} catch(IOException e) {
			e.printStackTrace();
		}
		launch(args);
	}

}
