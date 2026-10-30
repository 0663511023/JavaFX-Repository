package application;

// Organize the imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CalculatorHistory {
	private static final ObservableList<String> historyList = FXCollections.observableArrayList();
	
	public static void addEntry(String calculatorType, String expression, String result) {
		historyList.add("[" + calculatorType + "] " + expression + " = " + result);
	}
	
	public static void removeEntry(int index) {
		if (index >= 0 && index < historyList.size()) {
			historyList.remove(index);
		}
	}
	
	public static void updateEntry(int index, String updateEntry) {
		if (index >= 0 && index < historyList.size()) {
			historyList.set(index, updateEntry);
		}
	}
	
	public static ObservableList<String> getHistory() {
		return historyList;
	}
	
	public static void clearHistory() {
		historyList.clear();
	}
}
