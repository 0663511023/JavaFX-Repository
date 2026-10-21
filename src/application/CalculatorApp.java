package application;

// Organize the imports
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CalculatorApp extends Application {
	
	// Global/class variables
	private TextField displayField = new TextField();
	private double firstNumber = 0;
	private String operator = "";
	private boolean startNewNumber = true;
	
	@Override // Override the start method in the Application class
	public void start(Stage stage) {
		// Create UI elements
		displayField.setStyle("-fx-padding: 15px; -fx-font-size: 22px; -fx-alignment: center-right");
		displayField.setEditable(false);
		
		Button seven = new Button("7");
		
		Button eight = new Button("8");
		
		Button nine = new Button("9");
		
		Button delete = new Button("\u232B");
		
		Button clear = new Button("AC");
		
		Button four = new Button("4");
		
		Button five = new Button("5");
		
		Button six = new Button("6");
		
		Button multiply = new Button("*");
		
		Button divide = new Button("/");
		
		Button one = new Button("1");
		
		Button two = new Button("2");
		
		Button three = new Button("3");
		
		Button add = new Button("+");
		
		Button subtract = new Button("-");
		
		Button zero = new Button("0");
		
		Button dot = new Button(".");
		
		Button equals = new Button("=");
		
		Button[] allButtons = {one, two, three, four, five, six, seven, eight, nine, 
								zero, dot, equals, add, subtract, multiply, divide,
								delete, clear};
		for (Button btn : allButtons) {
			btn.setStyle("-fx-font-size: 20px");
			btn.setPrefSize(60, 60);
		}
		
		// Button Click Event Handlers
		one.setOnAction(e -> handleNumber("1"));
		two.setOnAction(e -> handleNumber("2"));
		three.setOnAction(e -> handleNumber("3"));
		four.setOnAction(e -> handleNumber("4"));
		five.setOnAction(e -> handleNumber("5"));
		six.setOnAction(e -> handleNumber("6"));
		seven.setOnAction(e -> handleNumber("7"));
		eight.setOnAction(e -> handleNumber("8"));
		nine.setOnAction(e -> handleNumber("9"));
		zero.setOnAction(e -> handleNumber("0"));
		
		dot.setOnAction(e -> handleDot());
		
		delete.setOnAction(e -> handleDelete());
		
		clear.setOnAction(e -> handleClear());
		
		add.setOnAction(e -> handleOperator("+"));
		subtract.setOnAction(e -> handleOperator("-"));
		multiply.setOnAction(e -> handleOperator("*"));
		divide.setOnAction(e -> handleOperator("/"));
		
		equals.setOnAction(e -> handleEquals());
		
		
		// Define the layout
		
		// Add buttons in a GripPane (button, column x, row y)
		GridPane gridPane = new GridPane();
		// (button, column x, row 0)
		gridPane.add(seven, 0, 0);
		gridPane.add(eight, 1, 0);
		gridPane.add(nine, 2, 0);
		gridPane.add(delete, 3, 0);
		gridPane.add(clear, 4, 0);
		
		// (button, column x, row 1)
		gridPane.add(four, 0, 1);
		gridPane.add(five, 1, 1);
		gridPane.add(six, 2, 1);
		gridPane.add(multiply, 3, 1);
		gridPane.add(divide, 4, 1);
		
		// (button, column x, row 2)
		gridPane.add(one, 0, 2);
		gridPane.add(two, 1, 2);
		gridPane.add(three, 2, 2);
		gridPane.add(add, 3, 2);
		gridPane.add(subtract, 4, 2);
		
		// (button, column x, row 3)
		gridPane.add(zero, 0, 3);
		gridPane.add(dot, 1, 3);
		gridPane.add(equals, 2, 3);
		
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		
		VBox vbox = new VBox(15);
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-padding: 20px");
		vbox.getChildren().addAll(displayField, gridPane);
		
		
		Scene scene = new Scene(vbox, 350, 450);
		stage.setTitle("JavaFX Calculator App");
		stage.setScene(scene);
		stage.show();
		stage.setResizable(false);
	}
	
	// Logic for appending number to display
	public void handleNumber(String value) {
			displayField.setText(displayField.getText() + value);
	}
	
	// Logic for handling the decimal point safely
	private void handleDot() {
		String currentText = displayField.getText();
		
		// Find the current number being typed (everything after the last space)
		// This ensures a user can't type multiple dots in the *same* number (e.g., "5.2.1" is blocked)
		String currentNumber = currentText;
		int lastSpace = currentText.lastIndexOf(' ');
		if (lastSpace != -1) {
			currentNumber = currentText.substring(lastSpace + 1);
		}
		if (currentText.contains(" ")) {
			String[] parts = currentText.split(" ");
			currentNumber = parts[parts.length - 1]; // Get the last part after the last operator
		}
		
		// Only add the dot if the current number doesn't already have one
		if (!currentNumber.contains(".")) {
			// If the screen is empty or we just typed an operator, format it nicely as "0."
			if (currentText.isEmpty() || currentText.endsWith(" ")) {
				displayField.setText(currentText + "0.");
			
			} else {
				displayField.setText(currentText + ".");
			}
		}
	}
	
	// Logic for capturing the first number and selected operator
	public void handleOperator(String op) {
		String currentText = displayField.getText();
		// Prevent adding an operator if the screen is empty or already ends with an operator
		if (!currentText.isEmpty() && !currentText.endsWith(" ")) {
			displayField.setText(currentText + " " + op + " ");
		}
	}
	
	// Clear everything
	private void handleClear() {
		displayField.clear();
	}
	
	// Handle deleting the last character or operator cleanly
	private void handleDelete() {
		String currentText = displayField.getText();
		
		if (!currentText.isEmpty()) {
			// If the text ends with an operator surrounded by spaces (e.g., " + "), remove the whole operator and spaces
			if (currentText.endsWith(" ")) {
				if (currentText.length() > 3) {
					displayField.setText(currentText.substring(0, currentText.length() - 3));
				
				} else {
					displayField.setText("");
				}
			
			}  else {
				// Otherwise, just remove the very last character
				displayField.setText(currentText.substring(0, currentText.length() - 1));
			}
		}
	}
	
	// Handle evaluating expressions strictly following operator precedence (BODMAS/PEDMAS)
	public void handleEquals() {
		String expression = displayField.getText();
		if (expression == null || expression.trim().isEmpty()) {
			return;
		}
		
		try {
			// Clean up the string: replace multiple spaces with a single space and trim
			expression = expression.trim().replaceAll("\\s+", " ");
			String[] parts = expression.split(" ");
			
			// If it's just a single number, just keep it on the screen
			if (parts.length == 1) {
				return;
			}
			
			if (parts.length < 3) {
				displayField.setText("Error");
				return;
			}
			
			// PASS 1: Handle Multiplication (*) and Division (/) first by building a clean new list
			java.util.List<String> pass1List = new java.util.ArrayList<>();
			pass1List.add(parts[0]); // Add the very first number
			
			for (int i = 1; i < parts.length; i += 2) {
				String op = parts[i];
				if (i + 1 >= parts.length) break;
				
				double nextNum = Double.parseDouble(parts[i + 1]);
				
				if (op.equals("*") || op.equals("/")) {
					// Pull the last number we added, compute it with the next number, and push it back
					double prevNum = Double.parseDouble(pass1List.remove(pass1List.size() - 1));
					double subResult = 0;
					
					if (op.equals("*")) {
						subResult = prevNum * nextNum;
					} 
					else {
						if (nextNum == 0) {
							displayField.setText("Error (Division by zero)");
							return;
						} 
						subResult = prevNum / nextNum;
					}
					pass1List.add(String.valueOf(subResult));
				}
				else {
					// For + and -, we keep the operator and number as they are
					pass1List.add(op);
					pass1List.add(String.valueOf(nextNum));
				}
			}
			
			// PASS 2: Handle Addition(+) and Subtraction (-) from left to right
			double result = Double.parseDouble(pass1List.get(0));
			for (int i = 1; i < pass1List.size(); i += 2) {
				String op = pass1List.get(i);
				if (i + 1 >= pass1List.size()) break;
				
				Double nextNum = Double.parseDouble(pass1List.get(i + 1));
				
				if (op.equals("+")) {
					result += nextNum;
				} 
				else if (op.equals("-")) {
					result -= nextNum;
				}
			}
			
			// Format to remove trailing .0 if it's a whole number for a cleaner look
			if (result == (long) result) {
				displayField.setText(String.valueOf((long) result));
			}
			else {
				displayField.setText(String.valueOf(result));
			}
			
			operator = "";
			startNewNumber = true;
			
		} catch (NumberFormatException e) {
			displayField.setText("Error");
		} catch (Exception e) {
			displayField.setText("Error");
		}
	}
	
	public static void main(String[] args) {
		launch(args); // Launch the Calculator App

	}

}
