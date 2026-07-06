# OneCalcPlus - The Everything Calculator 

OneCalcPlus is a professional-grade Android calculator application built with a high-end editorial aesthetic known as "Kinetic Brutalism." It features a deep obsidian palette, sophisticated typography, and a suite of powerful conversion tools.

## 🚀 Features

- **Adaptive Unified Layout:** Seamlessly switch between a simple portrait calculator and an expanded scientific mode via a dedicated toggle.
- **Scientific Mode:** Access advanced functions (sin, cos, log, sqrt, etc.) in a side-by-side balanced grid.
- **Comprehensive Conversion Suite:**
    - **Unit Conversions:** Length, Weight, Volume, and Temperature (Celsius to Fahrenheit).
    - **Kitchen Conversions:** Culinary measurements like Cups, Tbsp, and Sticks of Butter.
    - **Currency Conversions:** Real-time interactive conversion between major global currencies (USD, EUR, GBP, JPY, CAD, AUD).
- **Custom Currency Converter:** Mix and match any two supported currencies using dual dropdown menus.
- **Modern Android Architecture:** Built with Jetpack Compose, Kotlin Coroutines (StateFlow), and a strictly decoupled Clean Architecture pattern.
- **High-Precision Arithmetic:** Utilizes `BigDecimal` to ensure absolute mathematical accuracy, avoiding common floating-point errors.

## 🧪 Testing

The project includes a comprehensive suite of unit tests covering:
- Standard arithmetic operations.
- Scientific function accuracy.
- Conversion logic and reciprocal factors.

Run tests using:
```bash
./gradlew test
```
