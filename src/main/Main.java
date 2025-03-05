package main;

import controller.LoginController;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application{

	public void start(Stage primaryStage) {
		LoginController loginController = new LoginController(primaryStage);
		Scene scene = new Scene(loginController.getView(), 350, 430);
		primaryStage.setTitle("Login - Marvis Electronics");
 		primaryStage.setScene(scene);
 		primaryStage.setResizable(false);
 	
 	    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
 	    primaryStage.setX((screenBounds.getWidth() - 350) / 2);
 	    primaryStage.setY((screenBounds.getHeight() - 430) / 2);

 	    primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
