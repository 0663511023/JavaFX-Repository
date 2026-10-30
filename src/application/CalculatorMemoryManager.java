package application;

public class CalculatorMemoryManager {
	private static double memoryValue = 0.0;
	private static boolean hasMemory = false;
	
	public static void memoryClear() {
		memoryValue = 0.0;
		hasMemory = false;
	}
	
	public static void memoryStore(double value) {
		memoryValue = value;
		hasMemory = true;
	}
	
	public static void memoryAdd(double value) {
		memoryValue += value;
		hasMemory = true;
	}
	
	public static void memoryRemove(double value) {
		memoryValue -= value;
		hasMemory = true;
	}
	
	public static boolean memoryRecall() {
		return hasMemory;
	}
	
	public static boolean hasStoreMemory() {
		return hasMemory;
	}
}
