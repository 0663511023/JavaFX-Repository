package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ScientificCalculator {

	private static VBox mainContainer;
	private static Label expressionLabel;
	private static Label resultLabel;
	
	private static boolean isEvaluated = false;
	private static String currentInput = "";
	private static String lastExpression = "";
	
	public static VBox createScientificLayout() {
		mainContainer = new VBox(10);
		mainContainer.setPadding(new Insets(15));
		mainContainer.setAlignment(Pos.TOP_CENTER);
		mainContainer.setMaxWidth(600);
		
		
		// --- Compact DEG and RAD Buttons ---
		Button degBtn = new Button("DEG");
		Button radBtn = new Button("RAD");
		
		// Styles for active/inactive states
		String activeStyle = "-fx-font-size: 10px; -fx-base: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 3; -fx-padding: 2 6 2 6;";
		String inactiveStyle = "-fx-font-size: 10px; -fx-base: #dcdde1; -fx-text-fill: #2f3640; -fx-background-radius: 3; -fx-padding: 2 6 2 6;";
		
		// Set initial button states based on CalculatorEngine default
		boolean isDeg = CalculatorEngine.isInDegrees();
		degBtn.setStyle(isDeg ? activeStyle : inactiveStyle);
		radBtn.setStyle(!isDeg ? activeStyle : inactiveStyle);
		
		// Button Action Handlers
		degBtn.setOnAction(e -> {
			CalculatorEngine.setInDegrees(true);
			degBtn.setStyle(activeStyle);
			radBtn.setStyle(inactiveStyle);
			
			// Instant recalculation for DEG mode without retyping
			recalculateLastExpression();
		});
		
		radBtn.setOnAction(e -> {
			CalculatorEngine.setInDegrees(false);
			radBtn.setStyle(activeStyle);
			degBtn.setStyle(inactiveStyle);
			
			// Instant recalculation for  RAD mode without retyping
			recalculateLastExpression();
		});
		
		// Layout for the mode buttons
		HBox modeBar = new HBox(6, degBtn, radBtn);
		modeBar.setAlignment(Pos.TOP_CENTER);
		modeBar.setPadding(new Insets(4, 0, 0, 0));
		
		
		// --- Dual-Display Screen Container
		VBox displayBox = new VBox(4);
		displayBox.getStyleClass().add("display-box");
		displayBox.setPadding(new Insets(8, 12, 8, 12));
		displayBox.setAlignment(Pos.CENTER_RIGHT);
		displayBox.setPrefHeight(100);
		displayBox.setMinHeight(100);
		displayBox.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(displayBox, Priority.ALWAYS);
		
		
		// Top-Left: Expression as you type
		expressionLabel = new Label("");
		expressionLabel.getStyleClass().add("expression-label");
		expressionLabel.setAlignment(Pos.TOP_LEFT);
		expressionLabel.setMaxWidth(Double.MAX_VALUE);
		
		
		// Bottom-Right: Result output
		resultLabel = new Label("0");
		resultLabel.getStyleClass().add("result-label");
		resultLabel.setAlignment(Pos.BOTTOM_RIGHT);
		resultLabel.setMaxWidth(Double.MAX_VALUE);
		

		displayBox.getChildren().addAll(modeBar, expressionLabel, resultLabel);
		
		
		// --- GridPane for Buttons ---
		GridPane grid = new GridPane();
		grid.setHgap(8);
		grid.setVgap(8);
		grid.setAlignment(Pos.CENTER);
		VBox.setVgrow(grid, Priority.ALWAYS);
		
		// Responsive Column Constraints (5 columns)
		for (int i = 0; i < 5; i++) {
			ColumnConstraints colConstr = new ColumnConstraints();
			colConstr.setHgrow(Priority.ALWAYS);
			colConstr.setPercentWidth(100.0 / 5);
			grid.getColumnConstraints().add(colConstr);
		}
		
		// Responsive Row Constraints (6 rows)
		for (int i = 0; i < 6; i++) {
			RowConstraints rowConstr = new RowConstraints();
			rowConstr.setVgrow(Priority.ALWAYS);
			rowConstr.setPercentHeight(100.0 / 6);
			grid.getRowConstraints().add(rowConstr);
		}

		
		// --- Create number buttons (0-9) ---
		Button[] numButtons = new Button[10];
		for (int i = 0; i < numButtons.length; i++) {
			numButtons[i] = new Button(String.valueOf(i));
			numButtons[i].setMaxWidth(Double.MAX_VALUE);
			numButtons[i].setMaxHeight(Double.MAX_VALUE);
			numButtons[i].setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
			numButtons[i].getStyleClass().add("number-buttons");
			final String numStr = String.valueOf(i);
			numButtons[i].setOnAction(e -> appendInput(numStr));
		}
		
		// --- Create Functional, Scientific Buttons & Operator Buttons ---
		Button clearBtn = new Button("AC");
		Button deleteBtn = new Button("\u232B");
		Button eBtn = new Button("e");
		Button factorialBtn = new Button("x!");
		
		Button openParenBtn = new Button("(");
		Button closeParenBtn = new Button(")");
		
		Button sinBtn = new Button("sin");
		Button cosBtn = new Button("cos");
		Button tanBtn = new Button("tan");
		Button logBtn = new Button("log");
		Button lnBtn = new Button("ln");
		Button sqrtBtn = new Button("\u221A");
		Button powerBtn = new Button("x^y");
		Button piBtn = new Button("\u03C0");
		
		Button divideBtn = new Button("\u00F7");
		Button multiplyBtn = new Button("\u00D7");
		Button minusBtn = new Button("-");
		Button plusBtn = new Button("+");
		Button equalsBtn = new Button("=");
		Button decimalBtn = new Button(".");
		
		// Set button sizes
		Button[] allActionButtons = {clearBtn, deleteBtn, eBtn, factorialBtn, 
									openParenBtn, closeParenBtn,
									sinBtn, cosBtn, tanBtn, logBtn, lnBtn, sqrtBtn, powerBtn, piBtn,
									divideBtn, multiplyBtn, minusBtn, plusBtn, equalsBtn, decimalBtn
							};
		for (Button btn : allActionButtons) {
			btn.setMaxWidth(Double.MAX_VALUE);
			btn.setMaxHeight(Double.MAX_VALUE);
		}
		
		// Styling
		clearBtn.setStyle("-fx-font-size: 20px; -fx-base: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
		deleteBtn.setStyle("-fx-font-size: 20px; -fx-base: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
		equalsBtn.setStyle("-fx-font-size: 18px; -fx-base: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
		decimalBtn.getStyleClass().add("number-buttons");
		
		Button[] sciButtons = { eBtn, factorialBtn, openParenBtn, closeParenBtn,
								sinBtn, cosBtn, tanBtn, 
								logBtn, lnBtn, sqrtBtn, powerBtn, piBtn
						};
		for (Button btn : sciButtons) {
			btn.setStyle("-fx-font-size: 18px; -fx-base: #5dade2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
		}
		
		Button[] operators = { divideBtn, multiplyBtn, minusBtn, plusBtn };
		for (Button btn : operators) {
			btn.setStyle("-fx-font-size: 18px; -fx-base: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
		}
		
		// --- Button Actions ---
		
		clearBtn.setOnAction(e -> triggerClear());
		deleteBtn.setOnAction(e -> triggerDelete());
		decimalBtn.setOnAction(e -> appendInput("."));
		openParenBtn.setOnAction(e -> appendInput("("));
		closeParenBtn.setOnAction(e -> appendInput(")"));
		
		eBtn.setOnAction(e -> appendInput("e"));
		factorialBtn.setOnAction(e -> appendInput("!"));
		
		
		sinBtn.setOnAction(e -> appendInput("sin("));
		cosBtn.setOnAction(e -> appendInput("cos("));
		tanBtn.setOnAction(e -> appendInput("tan("));
		logBtn.setOnAction(e -> appendInput("log("));
		lnBtn.setOnAction(e -> appendInput("ln("));
		
		sqrtBtn.setOnAction(e -> appendInput("\u221A"));
		powerBtn.setOnAction(e -> appendInput("^"));
		piBtn.setOnAction(e -> appendInput("\u03C0"));
		
		// Operator Handlers
		plusBtn.setOnAction(e -> appendInput("+"));
		
		minusBtn.setOnAction(e -> appendInput("-"));
		multiplyBtn.setOnAction(e -> appendInput("\u00D7"));
		divideBtn.setOnAction(e -> appendInput("\u00F7"));
		
		// --- Equals Logic Using CalculatorEngine ---
		equalsBtn.setOnAction(e -> triggerEquals());
		
		// --- Arrange Buttons on the GridPane layout ---
		// row 0: Parentheses and basic trig functions
		grid.add(openParenBtn, 0, 0);
		grid.add(closeParenBtn, 1, 0);
		grid.add(sinBtn, 2, 0);
		grid.add(cosBtn, 3, 0);
		grid.add(tanBtn, 4, 0);
		
		// Row 1: Advanced math functions
		grid.add(logBtn, 0, 1);
		grid.add(lnBtn, 1, 1);
		grid.add(sqrtBtn, 2, 1);
		grid.add(powerBtn, 3, 1);
		grid.add(eBtn, 4, 1);
		
		// Row 2: Constants and Utilities
		grid.add(piBtn, 0, 2);
		grid.add(factorialBtn, 1, 2);
		grid.add(clearBtn, 2, 2);
		grid.add(deleteBtn, 3, 2);
		grid.add(divideBtn, 4, 2);
		
		// Row 3: Numbers 7-9 and Operators
		grid.add(numButtons[7], 0, 3);
		grid.add(numButtons[8], 1, 3);
		grid.add(numButtons[9], 2, 3);
		grid.add(multiplyBtn, 3, 3);
		grid.add(minusBtn, 4, 3);
		
		// Row 4: Numbers 4-6 and Operators
		grid.add(numButtons[4], 0, 4);
		grid.add(numButtons[5], 1, 4);
		grid.add(numButtons[6], 2, 4);
		grid.add(plusBtn, 3, 4);
		grid.add(decimalBtn, 4, 4);
		
		// row 5: Numbers 1-3, 0 and Equals
		grid.add(numButtons[1], 0, 5);
		grid.add(numButtons[2], 1, 5);
		grid.add(numButtons[3], 2, 5);
		grid.add(numButtons[0], 3, 5);
		grid.add(equalsBtn, 4, 5);
		
		mainContainer.getChildren().addAll(displayBox, grid);
		
		return mainContainer;
	}
		
	public static void appendInput(String val) {
		if (isEvaluated) {
			if (val.equals("+") || val.equals("-") || val.equals("\u00D7") || val.equals("\u00F7")) {
				// Chain calculation from the previous result
				currentInput = resultLabel.getText() + val;
			}
			else {
				currentInput = val;
			}
			isEvaluated = false;
		}
		else {
			if (currentInput.equals("0") && !val.equals(".")) {
				currentInput = val;
			}
			else {
				currentInput += val;
			}
		}
		expressionLabel.setText(currentInput);
		resultLabel.setText("0");
	}
	
		
	public static void triggerClear() {
		currentInput = "";
		lastExpression = "";
		expressionLabel.setText("");
		resultLabel.setText("0");
		isEvaluated = false;
	}
		
	public static void triggerDelete() {
		if (isEvaluated) {
			triggerClear();
			return;
		}
		if (!currentInput.isEmpty()) {
			currentInput = currentInput.substring(0, currentInput.length() - 1);
			expressionLabel.setText(currentInput);
		}
		if (currentInput.isEmpty()) {
			expressionLabel.setText("");
			resultLabel.setText("0");
		}
	}
		
	public static void triggerEquals() {
		if (currentInput.isEmpty()) return;
		
		lastExpression = currentInput;
		try {
			double result = CalculatorEngine.evaluateExpression(lastExpression);
			String resStr = formatResult(result);
			
			CalculatorHistory.addEntry("Scientific", lastExpression, resStr);
			resultLabel.setText(resStr);
			expressionLabel.setText(lastExpression); // Keep expression visible at top-left
			isEvaluated = true;
	
		} catch (Exception ex) {
			resultLabel.setText("Error");
			isEvaluated = true;
		}
	}
	
	private static void recalculateLastExpression() {
		if (!lastExpression.isEmpty()) {
			try {
				double result = CalculatorEngine.evaluateExpression(lastExpression);
				resultLabel.setText(formatResult(result));
			
			} catch (Exception ex) {
				resultLabel.setText("Error");
			}
		}
	}
	
	private static String formatResult(double result) {
		if (Double.isInfinite(result)) {
			return "Error (division by zero)";
		}
		else if (Double.isNaN(result)) {
			return "Error";
		}
		else {
			return (result == (int) result) ? String.valueOf((int) result) : String.valueOf(result);
		}
	}
	
	public static void setInputValue(String val) {
		currentInput = val;
		lastExpression = val;
		expressionLabel.setText(currentInput);
		resultLabel.setText("0");
		isEvaluated = false;
	}
	
	// Compatibility helper for CalculatorApp
	public static TextField getDisplayField() {
		TextField dummy = new TextField(resultLabel.getText());
		return dummy;
	}
}
