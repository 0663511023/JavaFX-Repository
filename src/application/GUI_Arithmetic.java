package application;

// Organize imports
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI_Arithmetic extends Application {
	@Override // Override the start method in the Application class
	public void start(Stage stage) {
		// Create UI elements
		
		Label headerLabel = new Label("Add, subtract, multiply, divide two numbers, and find remainder of two numbers\n\n");
		headerLabel.setStyle("-fx-font-size: 22px");
		
		// Addition UI elements
		Label descriptiveLabel = new Label("Add two numbers");
		TextField field1 = new TextField();
		field1.setPromptText("Enter first number");
		
		TextField field2 = new TextField();
		field2.setPromptText("Enter second number");
		
		Button addButton = new Button("Calculate sum");
		Button clearAddButton = new Button("Clear fields");
		Label resultLabel = new Label("Result: ");
		
		
		// Subtraction UI elements
		Label descriptiveLabel1 = new Label("\nSubtract two numbers");
		TextField field3 = new TextField();
		field3.setPromptText("Enter first number");
		
		TextField field4 = new TextField();
		field4.setPromptText("Enter second number");
		
		Button subtractButton = new Button("Calculate difference");
		Button clearSubtractButton = new Button("Clear fields");
		Label resultLabel1 = new Label("Result:");
		
		
		// Multiplication UI elements
		Label descriptiveLabel2 = new Label("\nMultiply two numbers");
		TextField field5 = new TextField();
		field5.setPromptText("Enter first number");
		
		TextField field6 = new TextField();
		field6.setPromptText("Enter second number");
		
		Button multiplyButton = new Button("Calculate product");
		Button clearMultiplyButton = new Button("Clear fields");
		Label resultLabel2 = new Label("Result:");
		
		
		// Division UI elements
		Label descriptiveLabel3 = new Label("\nDivide two numbers");
		TextField field7 = new TextField();
		field7.setPromptText("Enter first number");
		
		TextField field8 = new TextField();
		field8.setPromptText("Enter second number");
		
		Button divideButton = new Button("Calculate quotient");
		Button clearDivideButton = new Button("Clear fields");
		Label resultLabel3 = new Label("Result:");
		
		// Modulus UI elements
		Label descriptiveLabel4 = new Label("\nFind remainder");
		TextField field9 = new TextField();
		field9.setPromptText("Enter first number");
		
		TextField field10 = new TextField();
		field10.setPromptText("Enter second number");
		
		Button modButton = new Button("Calculate remainder");
		Button clearModButton = new Button("Clear fields");
		Label resultLabel4 = new Label("Result:");
		
		// Button to clear all fields
		Button clearAllFieldsButton = new Button("Clear all fields");
		
		
		// Output sum result when the button is clicked
		addButton.setOnAction(e -> {
			try {
				// Step 1 & 2: Get text and parse/convert it to numbers (double)
				double num1 = Double.parseDouble(field1.getText());
				double num2 = Double.parseDouble(field2.getText());
				
				// Step 3: Perform standard arithmetic (Addition)
				double sum = num1 + num2;
				
				// Display the result on the screen
				resultLabel.setText("Result: " + sum);
			
			} catch (NumberFormatException ex) {
				// Handle cases where the user types letters instead of numbers
				resultLabel.setText("Please enter a valid number!");
			}
		});
		// Clear input fields and result label when button is clicked
		clearAddButton.setOnAction(e -> {
			field1.clear();
			field2.clear();
			resultLabel.setText("Result:");
		});
		
		// Output subtraction result when button is clicked
		subtractButton.setOnAction(e -> {
			try {
				// Step 1 & 2: Get text and parse/convert it to numbers (double)
				double diffNum1 = Double.parseDouble(field3.getText());
				double diffNum2 = Double.parseDouble(field4.getText());
				
				// Step3: Perform standard arithmetic (subtraction)
				double difference = diffNum1 - diffNum2;
				
				// Display the result on the screen
				resultLabel1.setText("Result: " + difference);
			
			} catch (NumberFormatException ex) {
				// Handle cases the users types letters instead of numbers
				resultLabel1.setText("Please enter a valid number!");
			}
		});
		// Clear input fields and reset result label when button is clicked
		clearSubtractButton.setOnAction(e -> {
			field3.clear();
			field4.clear();
			resultLabel1.setText("Result:");
		});
		
		// Output multiplication result when the button is clicked
		multiplyButton.setOnAction(e -> {
			try {
				// Step 1 & 2: Get text and parse/convert it to numbers (double)
				double productNum1 = Double.parseDouble(field5.getText());
				double productNum2 = Double.parseDouble(field6.getText());
				
				// Step 3: Perform standard arithmetic (Multiplication)
				double product = productNum1 * productNum2;
				
				// Display the result on the screen
				resultLabel2.setText("Result: " + product);
			
			} catch (NumberFormatException ex) {
				// Handle cases where the user types letters instead of numbers
				resultLabel2.setText("Please enter a valid number!");
			}
		});
		// Clear input fields and reset result label when button is clicked
		clearMultiplyButton.setOnAction(e -> {
			field5.clear();
			field6.clear();
			resultLabel2.setText("Result:");
		});
		
		// Output multiplication result when the button is clicked
		divideButton.setOnAction(e -> {
			try {
				// Step 1 & 2: Get text and parse/convert it to numbers (double)
				double divNum1 = Double.parseDouble(field7.getText());
				double divNum2 = Double.parseDouble(field8.getText());
				
				// Perform standard arithmetic (Division)
				double quotient = divNum1 / divNum2;
				
				// Display result on the screen
				resultLabel3.setText("Result: " + quotient);
			
			} catch (NumberFormatException ex) {
				resultLabel3.setText("Please enter a valid number!");
			}
		});
		// Clear input fields and reset result label when button is clicked
		clearDivideButton.setOnAction(e -> {
			field7.clear();
			field8.clear();
			resultLabel3.setText("Result:\n");
		});
		// Output the modulus (remainder) when the button is clicked
		modButton.setOnAction(e -> {
			try {
				// Step 1 & 2: Get text and parse/convert it to numbers (double)
				double modNum1 = Double.parseDouble(field9.getText());
				double modNum2 = Double.parseDouble(field10.getText());
				
				// Perform standard arithmetic (modulus)
				double modResult = modNum1 % modNum2;
				
				// Display result on the screen
				resultLabel4.setText("Result: " + modResult);
			
			} catch (NumberFormatException ex) {
				resultLabel4.setText("Please enter a valid number!");
			}
		});
		// Clear input fields and reset result label when button is clicked
		clearModButton.setOnAction(e -> {
			field9.clear();
			field10.clear();
			resultLabel4.setText("Result:");
		});
		
		Label label = new Label("\n");
		
		// Clear all fields when button is clicked
		clearAllFieldsButton.setOnAction(e -> {
			field1.clear();
			field2.clear();
			resultLabel.setText("Result:");
			
			field3.clear();
			field4.clear();
			resultLabel1.setText("Result:");
			
			field5.clear();
			field6.clear();
			resultLabel2.setText("Result:");
			
			field7.clear();
			field8.clear();
			resultLabel3.setText("Result:");
			
			field9.clear();
			field10.clear();
			resultLabel4.setText("Result:");
		});
		
		// Layout setup
		VBox root = new VBox(10);
		root.setPadding(new Insets(15));
		root.setStyle("-fx-font-size: 16px");
		root.getChildren().addAll(headerLabel, descriptiveLabel, field1, field2, addButton, clearAddButton, resultLabel,
								 descriptiveLabel1, field3, field4, subtractButton, clearSubtractButton, resultLabel1,
								 descriptiveLabel2, field5, field6, multiplyButton, clearMultiplyButton, resultLabel2,
								 descriptiveLabel3, field7, field8, divideButton, clearDivideButton, resultLabel3,
								 descriptiveLabel4, field9, field10, modButton, clearModButton, resultLabel4, label,
								 clearAllFieldsButton
								 );
		ScrollPane scrollPane = new ScrollPane(root);
		scrollPane.setFitToWidth(true);
		Scene scene = new Scene(scrollPane, 500, 650);
		stage.setTitle("JavaFX Arithmetic");
		stage.setScene(scene);
		stage.show();
	}
	
	
	public static void main(String[] args) {
		// Launch the Calculator class
		launch(args);

	}

}
