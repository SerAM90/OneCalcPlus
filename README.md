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

## 📷 Screenshots

- **Standard Calculator**
  
  <img width="381" height="778" alt="Screenshot 2026-07-07 at 4 56 11 PM" src="https://github.com/user-attachments/assets/e49920ca-97a5-4c17-b33d-7f37589ec48d" />
  
- **Scientific Calculator**
  
 <img width="332" height="740" alt="Screenshot 2026-07-07 at 4 57 56 PM" src="https://github.com/user-attachments/assets/0ebb4dd2-fd4f-4902-96bd-6f167301b048" />
 
- **Multiple Conversion Selections**
  
 <img width="337" height="398" alt="Screenshot 2026-07-07 at 4 56 40 PM" src="https://github.com/user-attachments/assets/9c601367-655e-44cb-b0fc-4fcb9c31d658" />


## Run tests using:
```bash
./gradlew test
```
