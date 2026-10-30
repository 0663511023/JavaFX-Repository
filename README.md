# JavaFX Modern Calculator App

A feature-rich, modern desktop calculator application built using Java and JavaFX, featuring custom styling, a responsive UI layout, and a robust expression parsing.

## Features

- **Multi-Step Arithmetic & BODMAS/PEMDAS:** Evaluates complex expressions using a reliable two-pass parser honoring ordering of operations.
- **Modern Dark-Mode UI:** Custom-styled layout using JavaFX CSS ('style.css') for a polished desktop experience.
- **Safe Decimal & Error Handling:** Gracefully handles edge cases like division by zero and invalid input sequences.
- **Window Branding:** Custom application window icon ('Calculator_icon.png (3)') for an integrated desktop feel.
- **Responsive Layout:** Built using 'VBox' and 'GridPane' components with dynamic constraints.

## Tech Stack

- **Language:** Java.
- **UI Framework:** JavaFX
- **Styling:** CSS
- **IDE:** Eclipse IDE
- **Version Control:** Git / GitHub

## Project Structure
src/

|___ CalculatorApp.java         # Main entry point and UI controller

|___ style.css                  # Custom dark-mode theme stylesheet

|___ Calculator_icon (3).png    # Window icon asset

## Getting Started
1. Clone the repository on bash

    git clone [https://github.com/0663511023/JavaFX-Repository.git]
   
3. Open in Eclipse:
  - Import the java as existing Java project or Maven/Gradle project Cdepending on your setup)
  - Ensure your JavaFX SDK is properly configured in your project's build path and run configurations
3. Run in Application:
  - Open CalculatorApp.java and run it as a Java Application

## Future Enhancements
- Scientific calculation mode (trigonometry and logarithmic functions)
- History drawer to review past calculations
- Native desktop executable packaging via jpackage.

*Built with passion as part of desktop application development exploration*
