package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CalculatorMemoryManager {
	private static double memoryValue = 0.0;
	private static boolean hasMemory = false;
	
	// Observable list to track saved memory items for the Memory Tab
	private static final ObservableList<String> memoryList = FXCollections.observableArrayList();
	
	public static void memoryClear() {
		memoryValue = 0.0;
		hasMemory = false;
		memoryList.clear();
	}
	
	public static void memoryStore(double value) {
		memoryValue = value;
		hasMemory = true;
		memoryList.add(formatVal(value));
	}
	
	public static void memoryAdd(double value) {
		memoryValue += value;
		hasMemory = true;
		memoryList.add(formatVal(memoryValue));
	}
	
	public static void memoryRemove(double value) {
		memoryValue -= value;
		hasMemory = true;
		memoryList.add(formatVal(memoryValue));
	}
	
	public static double memoryRecall() {
		return memoryValue;
	}
	
	public static boolean hasStoreMemory() {
		return hasMemory;
	}
	
	public static ObservableList<String> getMemoryList() {
		return memoryList;
	}
	
	public static void remorveMemoryEntry(int index) {
		if (index >= 0 && index < memoryList.size()) {
			memoryList.remove(index);
			if (memoryList.isEmpty()) {
				memoryClear();
			}
			else {
				try {
					memoryValue = Double.parseDouble(memoryList.get(memoryList.size() - 1));
				
				} catch (Exception e) {
					memoryValue = 0.0;
				}
			}
		}
	}
	
	public static String formatVal(double val) {
		return (val == (int) val) ? String.valueOf((int) val) : String.valueOf(val);
	}
}
