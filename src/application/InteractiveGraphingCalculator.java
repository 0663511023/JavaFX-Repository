package application;


import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class InteractiveGraphingCalculator {

	private static Canvas canvas;
	private static Slider amplitudeSlider;
	private static Slider frequencySlider;
	private static Slider phaseSlider;
	private static ComboBox<String> functionSelector;
	private static TextField customFunctionInput;
	
	private static final double BASE_X_SCALE = 60.0; // Pixels per 180 degrees (PI radians)
	private static final double BASE_Y_SCALE = 50.0; // Pixels per 1.0 unit
	private static double zoomFactor = 1.0;
	
	private static boolean isDarkTheme = false;
	private static final List<Label> dynamicLabels = new ArrayList<>();
	
	// Distinct color for multiple for plotted graphs
	private static final Color[] GRAPH_COLORS = {
			Color.web("#e67e22"), Color.web("#0984e3"), Color.web("#00b894"),
			Color.web("#6c5ce7"), Color.web("#d63031"), Color.web("#fd79a8")
	};
	
	// Data class to store the plot functions
	private static class GraphEntry {
		String type; // "Preset" or "Custom"
		String formula;
		double amp, freq, phase;
		Color color;
		
		GraphEntry(String type, String formula, double amp, double freq, double phase, Color color) {
			this.type = type;
			this.formula = formula;
			this.amp = amp;
			this.freq = freq;
			this.phase = phase;
			this.color = color;
		}
	}
	
	private static final List<GraphEntry> plottedFunctions = new ArrayList<>();
	
	private static Label createDynamicLabel(String text) {
		Label label = new Label(text);
		label.setStyle("-fx-font-size: 14px;");
		dynamicLabels.add(label);
		updateLabelColors(label);
		return label;
	}
	
	private static void updateLabelColors(Label label) {
		label.setTextFill(isDarkTheme ? Color.web("#f1f2f6") : Color.web("#2c3e50"));
	}
	
	
	public static HBox crateInteractiveGraphingLayout() {
		HBox root = new HBox(20);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);
		root.getStyleClass().add("graphing-container");
		
		VBox controlsBox = new VBox(15);
		controlsBox.setAlignment(Pos.TOP_LEFT);
		controlsBox.setMinWidth(320);
		
		functionSelector = new ComboBox<>();
		functionSelector.getItems().addAll("Select a trig function", "Sine", "Cosine", "Tangent");
		functionSelector.setPromptText("Select a trig function");
		functionSelector.setStyle("-fx-font-size: 14px;");
		functionSelector.setMaxWidth(Double.MAX_VALUE);
		
		customFunctionInput = new TextField();
		customFunctionInput.setPromptText("Input a trig function sin(x) + cos(2*x)");
		customFunctionInput.setStyle("-fx-font-size: 13px;");
		
		
		Button addPresetBtn = new Button("Add Preset");
		addPresetBtn.getStyleClass().add("graph-action-btn");
		addPresetBtn.setMaxWidth(Double.MAX_VALUE);
		
		Button addCustomBtn = new Button("Add Custom");
		addCustomBtn.getStyleClass().add("graph-action-btn");
		addCustomBtn.setMaxWidth(Double.MAX_VALUE);
		
		// Clear all graphs, reset reset selector, and clear custom input
		Button clearGraphBtn = new Button("Clear Graphs");
		clearGraphBtn.setOnAction(e -> {
			plottedFunctions.clear();
			functionSelector.getSelectionModel().select("Select a trig function");
			customFunctionInput.clear();
			drawGraph();
		});
		clearGraphBtn.setId("clearHistoryBtn");
		clearGraphBtn.setMaxWidth(Double.MAX_VALUE);
		
		canvas = new Canvas();
		
		// Bind canvas dimensions to fill available space dynamically
		root.widthProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				double newWidth = newVal.doubleValue() - controlsBox.getMinWidth() - 60;
				canvas.setWidth(Math.max(200, newWidth));
				drawGraph();
			}
		});
		root.heightProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				canvas.setHeight(Math.max(200, newVal.doubleValue() - 60));
				drawGraph();
			}
		});
		
		// Attach Scroll Event for Zooming
		canvas.setOnScroll(event -> {
			double zoomIdentity = 0.1;
			if (event.getDeltaY() > 0) {
				zoomFactor += zoomIdentity;
			}
			else {
				zoomFactor = Math.max(0.2, zoomFactor - zoomIdentity); // Prevent inverting
			}
			drawGraph();
		});
		
		// Create sliders with explicit major unit scaling and minor tick
		amplitudeSlider = createSlider(0.1, 5.0, 1.0, 0.1, 1.0, 4);
		frequencySlider = createSlider(0.2, 3.0, 1.0, 0.1, 0.5, 4);
		phaseSlider = createSlider(-Math.PI, Math.PI, 0, 0.1, Math.PI / 2, 2);
	
		
		GridPane controls = new GridPane();
		controls.setHgap(10);
		controls.setVgap(15);
		
		Label presetLabel = createDynamicLabel("Preset Function:");
		Label customLabel = createDynamicLabel("Custom Function:");
		Label ampLabel = createDynamicLabel("Amplitude:");
		Label freqLabel = createDynamicLabel("Frequency:");
		Label phaseLabel = createDynamicLabel("Phase Shift:");
		
		// Preset selector row
		controls.addRow(0, presetLabel, functionSelector);
		// Custom selector row
		controls.addRow(1, customLabel, customFunctionInput);
		controls.addRow(2, ampLabel, amplitudeSlider);
		controls.addRow(3, freqLabel, frequencySlider);
		controls.addRow(4, phaseLabel, phaseSlider);
		
		GridPane.setColumnSpan(functionSelector, 2);
		GridPane.setColumnSpan(customFunctionInput, 2);
		
		VBox actionButtons = new VBox(10, addPresetBtn, addCustomBtn, clearGraphBtn);
		actionButtons.setPadding(new Insets(10, 0, 0, 0));
		
		controlsBox.getChildren().addAll(controls, actionButtons);
		
		// Event Handlers
		amplitudeSlider.valueProperty().addListener(e -> drawGraph());
		frequencySlider.valueProperty().addListener(e -> drawGraph());
		phaseSlider.valueProperty().addListener(e -> drawGraph());
		
		// Add Preset Function
		addPresetBtn.setOnAction(e -> {
			String selected = functionSelector.getValue();
			if (selected != null) {
				Color color = GRAPH_COLORS[plottedFunctions.size() %  GRAPH_COLORS.length];
				plottedFunctions.add(new GraphEntry("Preset", selected, amplitudeSlider.getValue(),
						frequencySlider.getValue(), phaseSlider.getValue(), color	));
				drawGraph();
			}
		});
		
		// Add custom function
		addCustomBtn.setOnAction(e -> {
			String expr = customFunctionInput.getText().trim();
			if (!expr.isEmpty()) {
				Color color = GRAPH_COLORS[plottedFunctions.size() % GRAPH_COLORS.length];
				plottedFunctions.add(new GraphEntry("Custom", expr, amplitudeSlider.getValue(),
						frequencySlider.getValue(), phaseSlider.getValue(), color));
				drawGraph();
			}
		});
		
		// Live updating on custom function input typing
		customFunctionInput.textProperty().addListener((obs, oldText, newText) -> drawGraph());
		
		// Clear all graphs
		clearGraphBtn.setOnAction(e -> {
			plottedFunctions.clear();
			drawGraph();
		});
		
		root.getChildren().addAll(controlsBox, canvas);
		HBox.setHgrow(canvas, Priority.ALWAYS);
		
		drawGraph();
		return root;
	}
	
	private static Slider createSlider(double min, double max, double value, double block, double majorTickUnit, int minorTickUnit) {
		Slider slider = new Slider(min, max, value);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setBlockIncrement(block);
		slider.setMajorTickUnit(majorTickUnit);
		slider.setMinorTickCount(minorTickUnit);
		slider.setPrefWidth(220);
		slider.setStyle("-fx-base: #3498db; -fx-control-inner-background: #2c3e50;");
		return slider;
	}
	
	public static void setDarkTheme(boolean dark) {
		isDarkTheme = dark;
		for (Label label : dynamicLabels) {
			updateLabelColors(label);
		}
		drawGraph();
	}
	
	private static void drawGraph() {
		if (canvas == null) return;
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		double width = canvas.getWidth();
		double height = canvas.getHeight();
		double originX = width / 2;
		double originY = height / 2;
		
		double currentXScale = BASE_X_SCALE * zoomFactor;
		double currentYScale = BASE_Y_SCALE * zoomFactor;
		
		// Clear Previous Graph Frame
		gc.clearRect(0, 0, width, height);
		
		// Background color according to active theme
		Color bgColor = isDarkTheme ? Color.web("#1e272c") : Color.web("#f8f9fa");
		Color gridColor = isDarkTheme ? Color.web("#3a4754") : Color.web("dcdde1");
		Color axisColor = isDarkTheme ? Color.web("#f1f2f6") : Color.web("2f3640");
		Color labelColor = isDarkTheme ? Color.web("#a4b0be") : Color.web("#57606f");
		
		gc.setFill(bgColor);
		gc.fillRect(0, 0, width, height);
		
		// Draw Grid Lines
		gc.setStroke(gridColor);
		gc.setLineWidth(1);
		
		// X-grid lines (every 90 degrees = X_SCALE / 2)
		double xStep = currentXScale / 2.0;
		for (double x = originX % xStep; x < width; x += xStep) {
			gc.strokeLine(x, 0, x, height);
		}
		
		// Y-grid lines
		for (double y = originY % currentYScale; y < height; y += currentYScale) {
			gc.strokeLine(0, y, width, y);
		}
		
		// Draw Main Axes
		gc.setStroke(axisColor);
		gc.setLineWidth(2);
		gc.strokeLine(0, originY, width, originY); // X axis
		gc.strokeLine(originX, 0, originX, height); // Y axis
		
		// Draw Axes Labels (X-angles and Y-units)
		gc.setFill(labelColor);
		gc.setFont(Font.font("Segoe UI", 10));
		
		// X-Axis Angle Ticks (-360, -270, -180, -90, 0, 90, 180, 270, 360)
		for (double deg = -720; deg <= 720; deg += 90) {
			double rad = Math.toRadians(deg);
			double xPx = originX + (rad / Math.PI) * currentXScale;
			
			if (xPx >= 0 && xPx <= width) {
				gc.strokeLine(xPx, originY - 4, xPx, originY + 4);
				if (deg != 0) {
					gc.fillText((int) deg + "\u00b0", xPx - 10, originY + 16);
				}
			}
		}
		
		// Y-Axis Unit Ticks (-10 to +10)
		for (int yVal = -10; yVal <= 10; yVal++) {
			if (yVal == 0) continue;
			double yPy = originY - (yVal * currentYScale);
			if (yPy >= 0 && yPy <= height) {
				gc.strokeLine(originX - 4, yPy, originX + 4, yPy);
				gc.fillText(String.valueOf(yVal), originX + 6, yPy + 4);
			}
		}
		
		// Draw Active/Live Slider Preview Graph
		String activePreset = functionSelector.getValue();
		String activeCustom = customFunctionInput.getText();
		
		// Only plot active preview if a valid preset is selected or custom input exists
		if (activePreset != null && !activePreset.equals("Select a trig function") && !activePreset.isEmpty()) {
			plotSingleFunction(gc, "Preset", activePreset, amplitudeSlider.getValue(),
					frequencySlider.getValue(), phaseSlider.getValue(), Color.web("#e67e22"), originX, originY, width, height, currentXScale, currentYScale);
		}
		else if (!activeCustom.isEmpty()) {
			plotSingleFunction(gc, "Custom", activeCustom, amplitudeSlider.getValue(),
					frequencySlider.getValue(), phaseSlider.getValue(), Color.web("0984e3"), originX, originY, width, height, currentXScale, currentYScale);
		}
		
		// Draw All Saved Multi-Functions
		for (GraphEntry entry : plottedFunctions) {
			plotSingleFunction(gc, entry.type, entry.formula, entry.amp, entry.freq, entry.phase,
					entry.color, originX, originY, width, height, currentXScale, currentYScale);
		}
	}
	
	private static void plotSingleFunction(GraphicsContext gc, String type, String formula,
			double amp, double freq, double phase, Color color,
			double originX, double originY, double width, double height, double currentXScale, double currentYScale) {
		
		gc.setStroke(color);
		gc.setLineWidth(2.5);
		gc.beginPath();
		
		boolean drawing = false;
		
		for (double xPx = 0; xPx < width; xPx += 1.0) {
			// Convert pixel position to math radians x 
			double xRad = ((xPx - originX) / currentXScale) * Math.PI;
			double y = 0;
			
			try {
				if (type.equals("Preset")) {
					switch (formula) {
						case "Sine" :
							y = amp * Math.sin(freq * xRad + phase);
							break;
						case "Cosine" :
							y = amp * Math.cos(freq * xRad + phase);
							break;
						case "Tangent" :
							y = amp * Math.tan(freq * xRad + phase);
							break;
					}
				}
				else {
					// Evaluate custom expression using CalculatorEngine
					String exprToParse = formula.toLowerCase()
							.replaceAll("x", "(" + (freq * xRad + phase) + ")");
					y = amp * CalculatorEngine.evaluateExpression(exprToParse);
				}
			} catch (Exception e) {
				// Ignore parsing errors while user is typing
				continue;
			}
			
			double yPy = originY - (y * currentYScale);
			
			// Handle Tangent/Discontinuity limits
			if (Double.isNaN(y) || Double.isInfinite(y) || Math.abs(y * currentYScale) > height * 2) {
				drawing = false;
				continue;
			}
			
			if (!drawing) {
				gc.moveTo(xPx, yPy);
				drawing = true;
			}
			else {
				gc.lineTo(xPx, yPy);
			}
		}
		gc.stroke();
	}
}
