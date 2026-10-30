package application;

import com.sun.javafx.scene.paint.GradientUtils.Parser;

public class CalculatorEngine {
	
	/**
	 * Evaluates scientific expressions containing operators, parentheses,
	 * exponents, square roots, trigonometric functions, logarithms, and pi
	 * and square roots anywhere in the string.
	 */
	
	private static boolean inDegrees = true;
	public static void setInDegrees(boolean degrees) {
		inDegrees = degrees;
	}
	
	public static boolean isInDegrees() {
		return inDegrees;
	}
	
	public static double evaluateExpression(String text) {
		// Convert UI symbols to standard operators
		text = text.replaceAll("\u00D7", "*")
				.replaceAll("\u00F7", "/")
				.replaceAll("\u03C0", String.valueOf(Math.PI))
				.replaceAll("e", String.valueOf(Math.E));
		return new Parser (text).parse();
	}
	
	// Parse and computer standard expression with operator precedence
	private static double factorial(double n) {
		if (n < 0 || n != Math.floor(n)) {
			throw new RuntimeException("Factorial is only defined for no-negative integers");
		}
		if (n > 170) {
			return Double.POSITIVE_INFINITY;
		}
		double result = 1;
		for (int i = 1; i <= (int) n; i++) {
			result *= i;
		}
		return result;
	}
	
	private static class Parser {
		private final String str;
		private int pos = -1, ch;
			
		Parser (String str) {
			this.str = str;
		}
			
		void nextChar() {
			ch = (++pos < str.length()) ? str.charAt(pos) : -1;
		}
			
		boolean eat(int charToEat) {
			while (ch == ' ') nextChar();
			if (ch == charToEat) {
				nextChar();
				return true;
			}
			return false;
		}
			
		double parse() {
			nextChar();
			double x = parseExpression();
			if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
			return x;
		}
			
		// Expression = Term (+ / -) Term
		double parseExpression() {
			double x = parseTerm();
			for (;;) {
				if (eat('+')) x += parseTerm();
				else if (eat('-')) x -= parseTerm();
				else return x;
			}
		}
			
		// Term = Power (* / /) Power
		double parseTerm() {
			double x = parseFactor();
			while (true) {
				if (eat('*')) x *= parseFactor();
				else if (eat('/')) x /= parseFactor();
				else return x;		
			}
		}
		
		// Factor = number, parentheses, square roots, functions (sin, cos, tan, log, ln)
		double parseFactor() {
			if (eat('+')) return parseFactor();
			if (eat('-')) return -parseFactor();
			double x;
			int startPos = this.pos;
				
			// Handle Parentheses: ( expression )
			if (eat('(')) {
				x = parseExpression();
				if (!eat(')')) throw new RuntimeException("')' expected");
			}
			// Handle numbers
			else if ((ch >= '0' && ch <= '9') || ch == '.') {
				while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
				x = Double.parseDouble(str.substring(startPos, this.pos));
			}
			// Handle Square Root symbol
			else if (ch == '\u221A') {
				nextChar(); // Consume the sqrt symbol
				if (eat('(')) {
					x = parseExpression();
					if (!eat(')')) throw new RuntimeException("')' expected");
				}
				else {
					x = parseFactor(); // allow typing sqrt(9) directly
				}
				x = Math.sqrt(x);
			}
			// Handle Alphabetic Functions (sin, cos, log, ln, e)
			else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
				while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) nextChar();
				String func = str.substring(startPos, this.pos);
					
				if (func.equals("e")) {
					x = Math.E;
				}
				else {
					if (eat('(')) {
						x = parseExpression();
						if (!eat(')')) throw new RuntimeException("')' expected");
					}
					else {
						throw new RuntimeException("Parentheses expected after function: " + func);
					}
					
					// Check mode for trig functions
					if (func.equals("sin")) {
						double angle = inDegrees ? Math.toRadians(x) : x;
						x = Math.sin(angle);
					}
					else if (func.equals("cos")) {
						double angle = inDegrees ? Math.toRadians(x) : x;
						x = Math.cos(angle);
					}
					else if (func.equals("tan")) {
						double angle = inDegrees ? Math.toRadians(x) : x;
						x = Math.tan(angle);
					}
					else if (func.equals("log")) x = Math.log10(x);
					else if (func.equals("ln")) x = Math.log(x);
					else throw new RuntimeException("Unknown function: " + func);
				}
			}
			else {
				throw new RuntimeException("Unexpected: " + (char) ch);
			}
				
			// Postfix factorial operator (!)
			while (eat('!')) {
				x = factorial(x);
			}
			
			// Exponential (^ operator)
			if (eat('^')) x = Math.pow(x, parseFactor());
				
			return x;
		}
	}
}
