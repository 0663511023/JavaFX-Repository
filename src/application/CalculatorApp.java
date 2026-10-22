package application;

// Organize the imports
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.layout.ColumnConstraints;
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
		delete.setId("delete-button");
		
		Button clear = new Button("AC");
		clear.setId("clear-button");
		
		Button four = new Button("4");
		
		Button five = new Button("5");
		
		Button six = new Button("6");
		
		Button one = new Button("1");
		
		Button two = new Button("2");
		
		Button three = new Button("3");
		
		Button add = new Button("+");
		
		Button subtract = new Button("-");
		
		Button multiply = new Button("x");

		Button divide = new Button("/");
		
		Button zero = new Button("0");
		
		Button dot = new Button(".");
		
		Button equals = new Button("=");
		equals.setId("equals-button");
		equals.setMaxWidth(Double.MAX_VALUE);
		
		
		// Style for the number buttons, including the dot button
		Button[] numbers = {zero, one, two, three, four, five, six, seven, eight, nine, dot};
		for (Button numBtn : numbers) {
			numBtn.getStyleClass().add("number-button");
		}
		
		// Style for all operator buttons 
		Button[] operators = {add, subtract, multiply, divide};
		for (Button opBtn : operators) {
			opBtn.getStyleClass().add("operator-button");
		}
		
		Button[] allButtons = {one, two, three, four, five, six, seven, eight, nine, 
								zero, dot, equals, add, subtract, multiply, divide,
								delete, clear};
		for (Button btn : allButtons) {
			btn.setStyle("-fx-font-size: 20px");
			btn.setPrefSize(60, 60);
			btn.setMaxWidth(Double.MAX_VALUE);
			btn.setMaxHeight(Double.MAX_VALUE);
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
		gridPane.add(clear, 0, 0);
		gridPane.add(delete, 1, 0);
		gridPane.add(divide, 3, 0);
		
		// (button, column x, row 1)
		gridPane.add(seven, 0, 1);
		gridPane.add(eight, 1, 1);
		gridPane.add(nine, 2, 1);
		gridPane.add(multiply, 3, 1);
		
		// (button, column x, row 2)
		gridPane.add(four, 0, 2);
		gridPane.add(five, 1, 2);
		gridPane.add(six, 2, 2);
		gridPane.add(subtract, 3, 2);
		
		// (button, column x, row 3)
		gridPane.add(one, 0, 3);
		gridPane.add(two, 1, 3);
		gridPane.add(three, 2, 3);
		gridPane.add(add, 3, 3);
		
		// (button, column x, row 4)
		gridPane.add(zero, 0, 4);
		gridPane.add(dot, 1, 4);
		gridPane.add(equals, 2, 4);
		
		gridPane.setColumnSpan(equals, 2);
		
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		
		VBox vbox = new VBox(15);
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-padding: 20px");
		vbox.getChildren().addAll(displayField, gridPane);
		
		// Make columns grow evenly
		gridPane.getColumnConstraints().clear();
		for (int i = 0; i < 4; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setHgrow(Priority.ALWAYS);
			colConst.setPercentWidth(25); // Every column takes up to 25% of the grid width
			gridPane.getColumnConstraints().add(colConst);
		}
		
		// Make the display screen tall and match modern look
		displayField.setPrefHeight(80);
		displayField.setMaxWidth(Double.MAX_VALUE);
		displayField.setStyle("-fx-font-size: 32px; -fx-alignment: center-right;");
		
		// Make rows grow evenly
		gridPane.getRowConstraints().clear();
		for (int i = 0; i < 5; i++) {
			RowConstraints rowConst = new RowConstraints();
			rowConst.setVgrow(Priority.ALWAYS);
			rowConst.setPercentHeight(20); // Every row takes up to 25% of the grid height
			gridPane.getRowConstraints().add(rowConst);
		}
		
		// Allow the grid pane to expand to fill the bottom section
		vbox.setVgrow(gridPane, Priority.ALWAYS);
		
		// Make the display field stretch wide across the top
		displayField.setMaxWidth(Double.MAX_VALUE);
		
		Image icon = new Image("Calculator_icon (3).png");
		stage.getIcons().add(icon);
		
		Scene scene = new Scene(vbox, 360, 600);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		
		stage.setTitle("Calculator App");
		stage.setScene(scene);
		stage.show();
		stage.setResizable(true);
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
							displayField.setText("Error (Cannot divide by zero)");
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
