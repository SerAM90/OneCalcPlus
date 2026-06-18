package com.cs467.onecalcplus

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.*

enum class CalculatorMode {
    CALCULATOR, UNIT_CONVERSION, KITCHEN_CONVERSION
}

data class ConversionItem(
    val label: String,
    val factor: Double,
    val type: String // e.g., "length", "weight", "volume"
)

class CalculatorViewModel : ViewModel() {

    private val _displayState = mutableStateOf("0")
    val displayState: State<String> = _displayState

    private val _equationState = mutableStateOf("")
    val equationState: State<String> = _equationState

    private val _mode = mutableStateOf(CalculatorMode.CALCULATOR)
    val mode: State<CalculatorMode> = _mode

    private val _isScientificExpanded = mutableStateOf(false)
    val isScientificExpanded: State<Boolean> = _isScientificExpanded

    // State for interactive conversions
    private val _conversionInput = mutableStateOf("")
    val conversionInput: State<String> = _conversionInput

    private val _selectedConversion = mutableStateOf<ConversionItem?>(null)
    val selectedConversion: State<ConversionItem?> = _selectedConversion

    private var lastOperand: BigDecimal? = null
    private var pendingOperation: String? = null
    private var isNewInput = true

    fun onNumberClick(number: String) {
        val currentMode = _mode.value
        if (currentMode == CalculatorMode.CALCULATOR) {
            val currentDisplay = _displayState.value
            if (isNewInput || currentDisplay == "0" || currentDisplay == "undefined") {
                _displayState.value = number
                isNewInput = false
            } else {
                _displayState.value = currentDisplay + number
            }
        } else {
            // Handle number input for conversions
            val currentConvInput = _conversionInput.value
            if (currentConvInput == "1") {
                _conversionInput.value = number
            } else if (currentConvInput == "0" && number != ".") {
                _conversionInput.value = number
            } else {
                _conversionInput.value = currentConvInput + number
            }
        }
    }

    fun onBackspaceClick() {
        val currentMode = _mode.value
        if (currentMode == CalculatorMode.CALCULATOR) {
            val currentDisplay = _displayState.value
            if (currentDisplay.length > 1) {
                _displayState.value = currentDisplay.dropLast(1)
            } else {
                _displayState.value = "0"
                isNewInput = true
            }
        } else {
            val currentConvInput = _conversionInput.value
            if (currentConvInput.length > 1) {
                _conversionInput.value = currentConvInput.dropLast(1)
            } else {
                _conversionInput.value = "0"
            }
        }
    }

    fun onOperationClick(operation: String) {
        val currentValueStr = _displayState.value
        val currentValue = try {
            BigDecimal(currentValueStr)
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
        
        if (lastOperand != null && pendingOperation != null) {
            calculate()
        } else {
            lastOperand = currentValue
        }
        
        pendingOperation = operation
        _equationState.value = "${formatBigDecimal(lastOperand!!)} $operation"
        isNewInput = true
    }

    fun calculate() {
        val currentValueStr = _displayState.value
        val currentValue = try {
            BigDecimal(currentValueStr)
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
        val operand1 = lastOperand ?: return
        val operation = pendingOperation ?: return

        if (operation == "÷" && currentValue.compareTo(BigDecimal.ZERO) == 0) {
            _displayState.value = "undefined"
            _equationState.value = ""
            lastOperand = null
            pendingOperation = null
            isNewInput = true
            return
        }

        val result: BigDecimal = try {
            when (operation) {
                "+" -> operand1.add(currentValue)
                "-" -> operand1.subtract(currentValue)
                "×" -> operand1.multiply(currentValue)
                "÷" -> operand1.divide(currentValue, 10, RoundingMode.HALF_UP)
                "%" -> operand1.remainder(currentValue)
                "^" -> try {
                    operand1.pow(currentValue.toInt())
                } catch (e: Exception) {
                    BigDecimal.valueOf(operand1.toDouble().pow(currentValue.toDouble()))
                }
                else -> currentValue
            }
        } catch (e: Exception) {
            BigDecimal.ZERO
        }

        _displayState.value = formatBigDecimal(result)
        _equationState.value = ""
        lastOperand = null
        pendingOperation = null
        isNewInput = true
    }

    fun onScientificOperation(operation: String) {
        val currentValueStr = _displayState.value
        val currentValue = currentValueStr.toDoubleOrNull() ?: return
        val result = when (operation) {
            "sin" -> sin(Math.toRadians(currentValue))
            "cos" -> cos(Math.toRadians(currentValue))
            "tan" -> tan(Math.toRadians(currentValue))
            "log" -> log10(currentValue)
            "ln" -> ln(currentValue)
            "√" -> sqrt(currentValue)
            "π" -> PI
            "e" -> E
            "1/x" -> if (currentValue != 0.0) 1.0 / currentValue else Double.NaN
            "x²" -> currentValue.pow(2)
            "|x|" -> abs(currentValue)
            "exp" -> exp(currentValue)
            else -> currentValue
        }
        _displayState.value = formatResult(result)
        isNewInput = true
    }

    fun clear() {
        _displayState.value = "0"
        _equationState.value = ""
        _conversionInput.value = "0"
        _selectedConversion.value = null
        lastOperand = null
        pendingOperation = null
        isNewInput = true
    }

    fun setMode(newMode: CalculatorMode) {
        _mode.value = newMode
        clear()
    }

    fun toggleScientific() {
        _isScientificExpanded.value = !_isScientificExpanded.value
    }

    fun selectConversion(item: ConversionItem) {
        _selectedConversion.value = item
        _conversionInput.value = "1"
    }

    fun getConversionResult(): String {
        val inputStr = _conversionInput.value
        val input = try {
            inputStr.toDouble()
        } catch (e: Exception) {
            0.0
        }
        val factor = _selectedConversion.value?.factor ?: 1.0
        return formatResult(input * factor)
    }

    private fun formatBigDecimal(value: BigDecimal): String {
        val stripped = value.stripTrailingZeros()
        return stripped.toPlainString()
    }

    private fun formatResult(result: Double): String {
        return if (result.isNaN() || result.isInfinite()) {
            "undefined"
        } else if (result % 1 == 0.0) {
            result.toLong().toString()
        } else {
            val formatted = "%.8f".format(result).trimEnd('0').trimEnd('.')
            if (formatted == "-0") "0" else formatted
        }
    }
}
