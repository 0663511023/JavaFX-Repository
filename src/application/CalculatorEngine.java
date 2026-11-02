package application;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;


public class CalculatorEngine {
	
	/**
	 * Evaluates a mathematical expression string using exp4j.
	 * Supports basic operators, polynomials, logarithms (log, log10), and exponentials (exp, ^).'
	 * 
	 * @paaram expressionStr The mathematical expression from the UI
	 * @return The calculated double result
	 */
	
	private static boolean inDegrees = true;
	public static void setInDegrees(boolean degrees) {
		inDegrees = degrees;
	}
	
	public static boolean isInDegrees() {
		return inDegrees;
	}
	
	public static double evaluateExpression(String text) {
		if (text == null || text.trim().isEmpty()) {
			throw new IllegalArgumentException("Expression cannot be empty");
		}
		// Convert UI symbols to standard operators
		text = text.replaceAll("\u00D7", "*")
				.replaceAll("\u00F7", "/")
				.replaceAll("\u221A", "sqrt")
				.replaceAll("\u03C0", "pi");
		
		// Automatically close any unclosed parentheses
		long openCount = text.chars().filter(ch -> ch == '(').count();
		long closeCount = text.chars().filter(ch -> ch == ')').count();
		while (openCount > closeCount) {
			text += ")";
			closeCount++;
		}
		
		// Custom trigonometric functions honoring Degree/Radian mode
		Function sinFunc = new Function("sin", 1) {
			@Override
			public double apply(double... args) {
				double angle = inDegrees ? Math.toRadians(args[0]) : args[0];
				return Math.sin(angle);
			}
		};
		
		Function cosFunc = new Function("cos", 1) {
			@Override
			public double apply(double... args) {
				double angle = inDegrees ? Math.toRadians(args[0]) : args[0];
				return Math.cos(angle);
			}
		};
		
		Function tanFunc = new Function("tan", 1) {
			@Override
			public double apply(double... args) {
				double angle = inDegrees ? Math.toRadians(args[0]) : args[0];
				return Math.tan(angle);
			}
		};
		
		// Custom square root function
		Function sqrtFunc = new Function("sqrt", 1) {
			@Override
			public double apply(double... args) {
				return Math.sqrt(args[0]);
			}
		};
		
		// Custom factorial (!) operator implementation
		Operator factorialOp = new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {
			@Override
			public double apply(double[] values) {
				double n = values[0];
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
		};
		
		try {
			Expression expression = new ExpressionBuilder(text)
					.function(sinFunc)
					.function(cosFunc)
					.function(tanFunc)
					.function(sqrtFunc)
					.operator(factorialOp)
					.build();
			
			return expression.evaluate();
		} catch (Exception e) {
			throw new RuntimeException("Evaluate Error: " + e.getMessage());
		}
	}
}