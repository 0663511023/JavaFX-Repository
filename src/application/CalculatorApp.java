package application;

//Organize the imports
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CalculatorApp extends Application {
	
	private TabPane tabPane;
	private BorderPane root;
	
	@Override // Override the start method in the Application class
	public void start(Stage stage) {
		
		// Create a TabPane container
		tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		
				
		// Load Modular Tabs
		//Tab standardTab = new Tab("Standard", StandardCalculator.createStandardLayout());
		//Tab scientificTab = new Tab("Scientific", ScientificCalculator.createScientificLayout());
		
		VBox standardLayout = StandardCalculator.createStandardLayout();
		StackPane standardPane = new StackPane(standardLayout);
		Tab standardTab = new Tab("Standard", standardPane);
		
		VBox scientificLayout = ScientificCalculator.createScientificLayout();
		StackPane scientificPane = new StackPane(scientificLayout);
		Tab scientificTab = new Tab("Scientific", scientificPane);
				
		// History Tab Setup
		ListView<String> historyView = new ListView<>(CalculatorHistory.getHistory());
		Label emptyPlaceholder = new Label("No entries yet");
		emptyPlaceholder.setStyle("-fx-font-size: 15px; -fx-text-fill: #718093; -fx-font-style: italic;");
		historyView.setPlaceholder(emptyPlaceholder);
		
		
		// --- UI Components for History Tab ---
		// Instructions Banner at the top54
		Label instructionsLabel = new  Label(
			"Guide: Rigth-click on an expression to re-use, edit, or delete it."
		);
		instructionsLabel.setWrapText(true);
		instructionsLabel.setStyle(
			"-fx-font-size: 14px; -fx-text-fill: #2c3e50; -fx-background-color: #f1f2f6; " +
			"-fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #dcdde1; -fx-border-radius: 5;"
		);
		instructionsLabel.setMaxWidth(Double.MAX_VALUE);
		
		// Status / Error Feedback Label (displays in-tab instead of console)
		Label historyStatusLabel = new Label("");
		historyStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #eb4d4b; -fx-font-weight: bold;");
		
		
		// Create context menu for history actions
		ContextMenu historyContextMenu = new ContextMenu();
		MenuItem useMenuItem = new MenuItem("Use Result in Calculator");
		MenuItem editMenuItem = new MenuItem("Edit Entry");
		historyView.setContextMenu(historyContextMenu);
		
		// --- Tag-Based "Use Result" Logic  ---
		useMenuItem.setOnAction(e -> {
			String selected = historyView.getSelectionModel().getSelectedItem();
			if (selected != null && selected.contains(" = ")) {
				String result = selected.split(" = ")[1];
				
				// Intelligently route based on the entry's tag
				if (selected.startsWith("[Scientific]")) {
					ScientificCalculator.setInputValue(result);
					tabPane.getSelectionModel().select(scientificTab);
				}
				else {
					// Default fallback if on History tab: update both
					StandardCalculator.setInputValue(result);
					tabPane.getSelectionModel().select(standardTab);
				}
			}
		});
		
		// --- Action: Edit Logic with Tag Preservation & UI Error Feedback ---
		editMenuItem.setOnAction(e -> {
			int selectedIndex = historyView.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0) {
				String currentItem = CalculatorHistory.getHistory().get(selectedIndex);
				
				// Preserve the source tag (e.g., "[Standard]" or "[Scientific]")
				String tag = currentItem.startsWith("[") ? currentItem.substring(0, currentItem.indexOf("]") + 1) : "[Standard]";
				
				// Extract raw expression for the dialog box
				String rawExpression = currentItem.contains("]") ? currentItem.substring(currentItem.indexOf("]") + 1).trim() : currentItem;
				String oldExpression = rawExpression.contains(" = ") ? rawExpression.split(" = ")[0].trim() : rawExpression;
				
				
				TextInputDialog dialog = new TextInputDialog(oldExpression);
				dialog.setTitle("Edit Expression");
				dialog.setHeaderText("Modify the calculation expression:");
				dialog.setContentText("Expression: ");
				
				dialog.showAndWait().ifPresent(editedExpression -> {
					String cleanedExpression = editedExpression.trim();
					
					if (!cleanedExpression.trim().isEmpty()) {
						try {
							if (cleanedExpression.contains(" = ")) {
								cleanedExpression = cleanedExpression.split(" = ")[0].trim();
							}
							
							// Automatically re-evaluate the math expression cleanly using the engine
							double result = CalculatorEngine.evaluateExpression(cleanedExpression);
							String resStr = (result == (int) result && !Double.isInfinite(result) && !Double.isNaN(result))
											? String.valueOf((int) result)
											: String.valueOf(result);
							
							// Reassemble with the original tag preserved and update history
							String updatedEntry = tag + " " + cleanedExpression + " = " + resStr;
							CalculatorHistory.updateEntry(selectedIndex, updatedEntry);
							
							// Clear label on success
							historyStatusLabel.setText("");
						 
						} catch (Exception ex) {
							// If invalid syntax is entered, display message
							historyStatusLabel.setText("\u26A0 Error: Invalid expression syntax. Entry not updated");
						}
					}
				});
			}
		});
		
		// --- Action: Delete the selected history item ---
		MenuItem deleteMenuItem = new MenuItem("Delete Entry");
		
		// Action handler for deleting the selected entry
		deleteMenuItem.setOnAction(e -> {
			int selectedIndex = historyView.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0) {
				// Removes the entry by using its backend method
				CalculatorHistory.removeEntry(selectedIndex);
				
				// Clear any status messages
				historyStatusLabel.setText("");
			}
		});
		
		// Add items to the context menu and link it to the ListView
		historyContextMenu.getItems().addAll(useMenuItem, editMenuItem, deleteMenuItem);
		historyView.setContextMenu(historyContextMenu); // It shows up on right-click!
		
		// Setup Clear History Button and Layout
		Button clearHistoryBtn = new Button("Clear History");
		clearHistoryBtn.setOnAction(e -> {
			CalculatorHistory.clearHistory();
			historyStatusLabel.setText("");
		});
		clearHistoryBtn.setId("clearHistoryBtn");
		
		
		VBox historyLayout = new VBox(10, instructionsLabel, historyStatusLabel, historyView, clearHistoryBtn);
		historyLayout.setPadding(new Insets(15));
		historyLayout.setAlignment(Pos.CENTER);
		historyLayout.setMaxWidth(450);
		historyLayout.setMaxHeight(Double.MAX_VALUE);
		historyLayout.getStyleClass().add("history-container");
		VBox.setVgrow(historyLayout, Priority.ALWAYS);
		VBox.setVgrow(historyView, Priority.ALWAYS);
		historyView.setMaxWidth(Double.MAX_VALUE);
		
		// Wrap history tab in a StackPane
		StackPane historyPane = new StackPane(historyLayout);
		Tab historyTab = new Tab("History", historyPane);

		// Create CSS selectors
		Tab[] allTabs = {standardTab, scientificTab, historyTab};
		for (Tab tab : allTabs) {
			tab.getStyleClass().add("tab");
		}
		
		tabPane.getTabs().addAll(standardTab, scientificTab, historyTab);
		tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) ->{
			if (newTab != null && newTab.getContent() != null) {
				FadeTransition fade = new FadeTransition(Duration.millis(1000), newTab.getContent());
				fade.setFromValue(0.4);
				fade.setToValue(1.0);
				fade.play();
			}
		});
		tabPane.getStyleClass().add("tab-pane");
		
		
		// Top Toolbar for Dark Mode Toggle
		Button darkModeBtn = new Button("Dark Mode");
		Button lightModeBtn = new Button("Light Mode");
		darkModeBtn.setId("dark-mode-btn");
		lightModeBtn.setId("light-mode-btn");
		
		HBox topBar = new HBox(10, darkModeBtn, lightModeBtn);
		topBar.setPadding(new Insets(8, 15, 0, 15));
		topBar.setAlignment(Pos.CENTER_RIGHT);
		
		// Main Layout Container
		root = new BorderPane();
		root.setTop(topBar);
		root.setCenter(tabPane);
		root.getStyleClass().add("root");
		
		Scene scene = new Scene(root, 450, 660);
		// Call style.css
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		
		// --- Dynamic Font Scaling for Instructions Label ---
		scene.widthProperty().addListener((obs, oldVal, newVal) -> {
			double scale = 12 + (newVal.doubleValue() / 100.0);
			instructionsLabel.setFont(Font.font("Consolas", Math.min(Math.max(scale, 12), 16)));
		});
		
		// Theme Toggle Action (With Transition)
		darkModeBtn.setOnAction(e -> applyThemeWithAnimation(true));
		lightModeBtn.setOnAction(e -> applyThemeWithAnimation(false));
		
		// --- Physical Keyboard Support ---
		scene.setOnKeyPressed(event -> {
			KeyCode code = event.getCode();
			// Determine active tab
			boolean isSciTab = tabPane.getSelectionModel().getSelectedItem() == scientificTab;
			
			if (code.isDigitKey()) {
				String digit = code.getName();
				if (isSciTab) ScientificCalculator.appendInput(digit);
				else StandardCalculator.appendInput(digit);
			}
			else if (code == KeyCode.ENTER || code == KeyCode.EQUALS) {
				if (isSciTab) ScientificCalculator.triggerEquals();
				else StandardCalculator.triggerEquals();
			}
			else if (code == KeyCode.BACK_SPACE) {
				if (isSciTab) ScientificCalculator.triggerDelete();
				else StandardCalculator.triggerDelete();
			}
			else if (code == KeyCode.ESCAPE) {
				if (isSciTab) ScientificCalculator.triggerClear();
				else StandardCalculator.triggerClear();
			}
			else if (code == KeyCode.PERIOD || code == KeyCode.DECIMAL) {
				if (isSciTab) ScientificCalculator.appendInput(".");
				else StandardCalculator.appendInput(".");
			}
			else if (code == KeyCode.PLUS || code == KeyCode.ADD) {
				if (isSciTab) ScientificCalculator.appendInput("+");
				else StandardCalculator.appendInput("+");
			}
			else if (code == KeyCode.MINUS || code == KeyCode.SUBTRACT) {
				if (isSciTab) ScientificCalculator.appendInput("-");
				else StandardCalculator.appendInput("-");
			}
			else if (code == KeyCode.MULTIPLY) {
				if (isSciTab) ScientificCalculator.appendInput("\u00D7");
				else StandardCalculator.appendInput("\u00D7");
			}
			else if (code == KeyCode.DIVIDE) {
				if (isSciTab) ScientificCalculator.appendInput("\u00F7");
				else StandardCalculator.appendInput("\u00F7");
			}
		});
		
		Image icon = new Image("Calculator_icon (3).png");
		try {
			stage.getIcons().add(icon);
		
		} catch (Exception ex) {
			System.out.println("Icon not found!");
		}
		
		stage.setTitle("Calculator App");
		stage.setScene(scene);
		stage.show();
	}
	
	// Add Fade Transition
	private void applyThemeWithAnimation(boolean isDark) {
		// Quick fade-out transition
		FadeTransition fadeOut = new FadeTransition(Duration.millis(200), tabPane);
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.4);
		
		// Switch theme while faded
		fadeOut.setOnFinished(e -> {
			if (isDark) {
				if (!root.getStyleClass().contains("dark-theme")) {
					root.getStyleClass().add("dark-theme");
				}
			}
			else {
				root.getStyleClass().remove("dark-theme");
			}
		});
				
		// Fade-in transition back to full opacity
		FadeTransition fadeIn = new FadeTransition(Duration.millis(200), tabPane);
		fadeIn.setFromValue(0.4);
		fadeIn.setToValue(1.0);
					
		// Play fade-out then fade-in sequentially
		SequentialTransition seq = new SequentialTransition(fadeOut, fadeIn);
		seq.play();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
