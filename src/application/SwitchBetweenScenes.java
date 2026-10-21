package application;

/** Main.java
 * Descriptions: Contains application to switch between scenes
*/

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class SwitchBetweenScenes extends Application{
	
	Button button;
	
	@Override
	public void start(Stage window) {
		VBox vbox = new VBox(20); // Create a vertical box to order nodes 20px apart vertically
		vbox.setStyle("-fx-background-color: black");
		
		StackPane stpane = new StackPane(); // Create a stack pane to hold nodes
		stpane.setStyle("-fx-background-color:black");
		
		// Scenes 1 and 2
		// Place the box and the stack pane onto scenes 1 and 2 respectively
		Scene scene1 = new Scene(vbox, 450, 450); // Scene 1
		Scene scene2 = new Scene(stpane, 450, 450); // Scene 2
		
		// Label 1
		Label label1 = new Label("Welcome to the first scene"); // Label of text
		label1.setStyle("-fx-font-size: 30; -fx-: white");
		
		// Button 1
		Button button1 = new Button("Click to go to the second scene"); // Button 1
		button1.setStyle("-fx-font-size: 20px; font-weight: bold;");
		
		// Add nodes button and label to the vertical box
		vbox.getChildren().addAll(label1, button1); // Add all nodes to the vertical box
		
		// On button click, we escape to scene 2
		button1.setOnAction(e -> {
			window.setTitle("The Second Scene");
			window.setScene(scene2);
		});
		
		// Button 2
		Button button2 = new Button("Oops! Go back to scene 2");
		button2.setStyle("-fx-font-size: 20px; font-weight: bold");
		
		// Add the button node to the stack pane
		stpane.getChildren().add(button2);
		
		// On button2 click, we escape back to scene 1
		button2.setOnAction(e -> {
			window.setTitle("The First Scene");
			window.setScene(scene1);
	});
		
		window.setScene(scene1);
		window.setTitle("The First Scene");
		window.show();
		window.setResizable(false);
	}
	
	public static void main(String[] args) {
		launch(args); // Launch the Application class
	}
}
