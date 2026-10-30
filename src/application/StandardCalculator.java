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
import javafx.scene.layout.VBox;

public class StandardCalculator {
	
	private static VBox mainContainer;
	private static Label expressionLabel;
	private static Label resultLabel;
	
	private static String currentInput = "";
	private static boolean isEvaluated = false;
	
	public static VBox createStandardLayout() {
		mainContainer = new VBox(12);
		mainContainer.setPadding(new Insets(15));
		mainContainer.setAlignment(Pos.TOP_CENTER);
		mainContainer.setMaxWidth(550);
		mainContainer.setMaxHeight(Double.MAX_VALUE);
		
		
		// --- Dual-Display Screen Container
		VBox displayBox = new VBox(4);
		displayBox.getStyleClass().add("display-box");
		displayBox.setAlignment(Pos.CENTER_RIGHT);
		displayBox.setPrefHeight(90);
		displayBox.setMinHeight(90);
		displayBox.setMaxWidth(Double.MAX_VALUE);
		
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
		
		displayBox.getChildren().addAll(expressionLabel, resultLabel);
		
		// GridPane for Buttons
		GridPane grid = new GridPane();
		grid.setHgap(8);
		grid.setVgap(8);
		grid.setAlignment(Pos.CENTER);
		VBox.setVgrow(grid, Priority.ALWAYS);
		
		// Responsive Column Constraints 
		for (int i = 0; i < 4; i++) {
			ColumnConstraints colConstr = new ColumnConstraints();
			colConstr.setHgrow(Priority.ALWAYS);
			colConstr.setPercentWidth(100.0 / 4);
			grid.getColumnConstraints().add(colConstr);
		}
		
		// Responsive Row Constraints (6 rows)
		for (int i = 0; i < 6; i++) {
			RowConstraints rowConstr = new RowConstraints();
			rowConstr.setVgrow(Priority.ALWAYS);
			rowConstr.setPercentHeight(100.0 / 6);
			grid.getRowConstraints().add(rowConstr);
		}
		
		// --- Create Memory Buttons ---
		Button memoryClearBtn = new Button("MC");
		Button memoryRecallBtn = new Button("MR");
		Button memoryAddBtn = new Button("M+");
		Button memoryRemoveBtn = new Button("M-");
		
		Button[] memButtons = {memoryClearBtn, memoryRecallBtn, memoryAddBtn, memoryRemoveBtn};
		for (Button btn : memButtons) {
			btn.setMaxWidth(Double.MAX_VALUE);
			btn.setMaxHeight(Double.MAX_VALUE);
			btn.setStyle("-fx-font-size: 32px; -fx-base: #718093; -fx-text-fill: white; -fx-font-weight: bold;");
			btn.getStyleClass().add("memory-buttons");
		}
		
		// --- Memory Buttons Actions Logic ---
		memoryClearBtn.setOnAction(e -> CalculatorMemoryManager.memoryClear());
		
		memoryRecallBtn.setOnAction(e -> {
			if (CalculatorMemoryManager.hasStoreMemory()) {
				String strVal = String.valueOf(CalculatorMemoryManager.memoryRecall()	);
				// Clean up trailing .0 for neat integer display if applicable
				if (strVal.endsWith("0")) {
					strVal = strVal.substring(0, strVal.length() -2);
				}
				appendInput(strVal);
			}
		});
		
		memoryAddBtn.setOnAction(e -> {
			try {
				double currentVal = Double.parseDouble(resultLabel.getText());
				CalculatorMemoryManager.memoryAdd(currentVal);
			
			} catch (NumberFormatException ex) {
				resultLabel.setText("Error");
			}
		});
		
		memoryRemoveBtn.setOnAction(e -> {
			try {
				double currentVal = Double.parseDouble(resultLabel.getText());
				CalculatorMemoryManager.memoryRemove(currentVal);
			
			} catch (NumberFormatException ex) {
				resultLabel.setText("Error");
			}
		});
		
		// --- Create number buttons (0-9) ---
		Button[] numButtons = new Button[10];
		for (int i = 0; i < numButtons.length; i++) {
			numButtons[i] = new Button(String.valueOf(i));
			numButtons[i].setMaxWidth(Double.MAX_VALUE);
			numButtons[i].setMaxHeight(Double.MAX_VALUE);
			numButtons[i].setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");
			numButtons[i].getStyleClass().add("number-buttons");
			final String numStr = String.valueOf(i);
			numButtons[i].setOnAction(e -> appendInput(numStr));
		}
		
		// Action Buttons
		Button clearBtn = new Button("AC");
		Button deleteBtn = new Button("\u232B");
		Button divideBtn = new Button("\u00F7");
		Button multiplyBtn = new Button("\u00D7");
		Button minusBtn = new Button("-");
		Button plusBtn = new Button("+");
		Button equalsBtn = new Button("=");
		Button decimalBtn = new Button(".");
		
		Button[] allButtons = {clearBtn, deleteBtn, divideBtn, multiplyBtn,
				minusBtn, plusBtn, equalsBtn, decimalBtn
			};
		for (Button btn : allButtons) {
			btn.setMaxWidth(Double.MAX_VALUE);
			btn.setPrefHeight(Double.MAX_VALUE);
		}
		
		// Styling
		clearBtn.setStyle("-fx-font-size: 18px; -fx-base: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
		deleteBtn.setStyle("-fx-font-size: 18px; -fx-base: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
		equalsBtn.setStyle("-fx-font-size: 24px; -fx-base: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
		equalsBtn.getStyleClass().add("equalsBtn");
		decimalBtn.getStyleClass().add("number-buttons");
		
		
		
		Button[] operators = {divideBtn, multiplyBtn, minusBtn, plusBtn};
		for (Button btn : operators) {
			btn.setStyle("-fx-font-size: 22px; -fx-base: #5dade2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
		}
		
		clearBtn.setOnAction(e -> triggerClear());
		deleteBtn.setOnAction(e -> triggerDelete());
		decimalBtn.setOnAction(e -> appendInput("."));
		plusBtn.setOnAction(e -> appendInput("+"));
		minusBtn.setOnAction(e -> appendInput("-"));
		multiplyBtn.setOnAction(e -> appendInput("\u00D7"));
		divideBtn.setOnAction(e -> appendInput("\u00F7"));
		equalsBtn.setOnAction(e -> triggerEquals());
		
		// GridPane Layout
		// row 0: MC, MR, M+, M-
		grid.add(memoryClearBtn, 0, 0);
		grid.add(memoryRecallBtn, 1, 0);
		grid.add(memoryAddBtn, 2, 0);
		grid.add(memoryRemoveBtn, 3, 0);
		
		// row 1: AC, delete, divide
		grid.add(clearBtn, 0, 1);
		grid.add(deleteBtn, 1, 1);
		grid.add(divideBtn, 3, 1);
		
		// row 2: 7, 8, 9, multiply 
		grid.add(numButtons[7], 0, 2); 
		grid.add(numButtons[8], 1, 2);
		grid.add(numButtons[9], 2, 2);
		grid.add(multiplyBtn, 3, 2);
		
		// row 3: 4, 5, 6, minus
		grid.add(numButtons[4], 0, 3);
		grid.add(numButtons[5], 1, 3);
		grid.add(numButtons[6], 2, 3);
		grid.add(minusBtn, 3, 3);
		
		// row 4: 1, 2, 3, plus
		grid.add(numButtons[1], 0, 4);
		grid.add(numButtons[2], 1, 4);
		grid.add(numButtons[3], 2, 4);
		grid.add(plusBtn, 3, 4);
		
		// row 5: 0, .
		grid.add(numButtons[0], 0, 5);
		grid.add(decimalBtn, 1, 5, 2, 1);
		grid.add(equalsBtn, 3, 5, 1, 1);
		
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
			if (currentInput.equals("0") || currentInput.isEmpty()) {
				if (val.equals("-")) {
					currentInput = "-"; // Allow negative sign first
				}
				else if (val.equals("+") || val.equals("\u00D7") || val.equals("\u00F7")) {
					return;
				}
				else {
					currentInput = val;
				}
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
		
		try {
			double result = CalculatorEngine.evaluateExpression(currentInput);
			String resStr = (result == (int) result && !Double.isInfinite(result) && !Double.isNaN(result))
						? String.valueOf((int) result)
						: String.valueOf(result);
			
			CalculatorHistory.addEntry("Standard", currentInput, resStr);
			resultLabel.setText(resStr);
			expressionLabel.setText(currentInput);
			isEvaluated = true;
	
		} catch (Exception ex) {
			resultLabel.setText("Error");
			isEvaluated = true;
		}
	}
	
	public static void setInputValue(String val) {
		currentInput = val;
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
