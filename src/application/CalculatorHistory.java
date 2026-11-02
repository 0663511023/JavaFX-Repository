package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.stream.Collectors;

// Organize the imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CalculatorHistory {
	private static final ObservableList<String> historyList = FXCollections.observableArrayList();
	private static final String HISTORY_FILE = "calculator_tape_history.json";
	
	static {
		loadHistoryFromFile();
	}
	
	public static void addEntry(String calculatorType, String expression, String result) {
		String entry = "[" + calculatorType + "] " + expression + " = " + result;
		historyList.add(0, entry); // Add newest item on top
		saveHistoryToFile();
	}
	
	public static void removeEntry(int index) {
		if (index >= 0 && index < historyList.size()) {
			historyList.remove(index);
			saveHistoryToFile();
		}
	}
	
	public static void updateEntry(int index, String updateEntry) {
		if (index >= 0 && index < historyList.size()) {
			historyList.set(index, updateEntry);
			saveHistoryToFile();
		}
	}
	
	public static ObservableList<String> getHistory() {
		return historyList;
	}
	
	public static void clearHistory() {
		historyList.clear();
		saveHistoryToFile();
	}
	
	private static void saveHistoryToFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(HISTORY_FILE)))) {
			writer.write("\n");
			for (int i = 0; i < historyList.size(); i++) {
				String sanitized = historyList.get(i).replace("\\", "\\\\").replace("\"", "\\\"");
				writer.write("    \"" + sanitized + "\"");
				if (i < historyList.size() - 1) {
					writer.write(",");
				}
				writer.write("\n");
			}
			writer.write("]");
		} catch (Exception e){
			System.err.println("Could not save history tape to JSON: " + e.getMessage());
		}
	}
	
	public static void loadHistoryFromFile() {
		File file = new File(HISTORY_FILE);
		if (!file.exists()) return;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			historyList.clear();
			String line;
			while ((line = reader.readLine()) != null) {
				String trimmed = line.trim();
				// Ignore the JSON array brackets and commas
				if (trimmed.equals("[") || trimmed.equals("]") || trimmed.equals(",") || trimmed.endsWith(",")) {
					if (trimmed.endsWith(",")) {
						trimmed = trimmed.substring(0, trimmed.length() - 1).trim();
					}
					else {
						continue;
					}
				}
				// Remove surrounding quotes and unescape
				if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
					String content = trimmed.substring(1, trimmed.length() - 1);
					String unescaped = content.replace("\\\"", "\"").replace("\\\\", "\\");
					historyList.add(unescaped);
				}
			}
			
		} catch (Exception e) {
			System.err.println("Could notload history tape: " + e.getMessage());
;		}
	}
}
