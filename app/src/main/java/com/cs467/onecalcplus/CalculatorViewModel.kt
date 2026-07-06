package com.cs467.onecalcplus

import androidx.lifecycle.ViewModel
import com.cs467.onecalcplus.model.CalculatorMode
import com.cs467.onecalcplus.model.CalculatorUiEvent
import com.cs467.onecalcplus.model.CalculatorUiState
import com.cs467.onecalcplus.model.ConversionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.*

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private var lastOperand: BigDecimal? = null
    private var pendingOperation: String? = null
    private var isNewInput = true
    private var isNewConversionInput = true

    /**
     * Primary entry point for UI events.
     */
    fun onEvent(event: CalculatorUiEvent) {
        when (event) {
            is CalculatorUiEvent.NumberClick -> handleNumberClick(event.number)
            is CalculatorUiEvent.OperationClick -> handleOperationClick(event.operation)
            is CalculatorUiEvent.ScientificOperationClick -> handleScientificOperation(event.operation)
            CalculatorUiEvent.Calculate -> calculate()
            CalculatorUiEvent.Backspace -> handleBackspace()
            CalculatorUiEvent.Clear -> reset()
            is CalculatorUiEvent.SetMode -> setMode(event.mode)
            CalculatorUiEvent.ToggleScientific -> toggleScientific()
            is CalculatorUiEvent.SelectConversion -> selectConversion(event.item)
            CalculatorUiEvent.BackToConversionList -> backToConversionList()
        }
    }

    private fun handleNumberClick(number: String) {
        if (_uiState.value.mode == CalculatorMode.CALCULATOR) {
            _uiState.update { state ->
                val currentDisplay = state.display
                val newDisplay = if (isNewInput || currentDisplay == "0" || currentDisplay == "undefined") {
                    number
                } else {
                    currentDisplay + number
                }
                isNewInput = false
                state.copy(display = newDisplay)
            }
        } else {
            _uiState.update { state ->
                val currentConvInput = state.conversionInput
                val newConvInput = if (isNewConversionInput || currentConvInput == "0" && number != ".") {
                    isNewConversionInput = false
                    number
                } else {
                    currentConvInput + number
                }
                state.copy(conversionInput = newConvInput)
            }
        }
    }

    private fun handleBackspace() {
        if (_uiState.value.mode == CalculatorMode.CALCULATOR) {
            _uiState.update { state ->
                val currentDisplay = state.display
                val newDisplay = if (currentDisplay.length > 1 && currentDisplay != "undefined") {
                    currentDisplay.dropLast(1)
                } else {
                    isNewInput = true
                    "0"
                }
                state.copy(display = newDisplay)
            }
        } else {
            _uiState.update { state ->
                val currentConvInput = state.conversionInput
                val newConvInput = if (currentConvInput.length > 1) {
                    currentConvInput.dropLast(1)
                } else {
                    isNewConversionInput = true
                    "0"
                }
                state.copy(conversionInput = newConvInput)
            }
        }
    }

    private fun handleOperationClick(operation: String) {
        val currentValueStr = _uiState.value.display
        val currentValue = try {
            BigDecimal(currentValueStr)
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
        
        if (lastOperand != null && pendingOperation != null) {
            performCalculation()
        } else {
            lastOperand = currentValue
        }
        
        pendingOperation = operation
        val eq = "${formatBigDecimal(lastOperand!!)} $operation"
        _uiState.update { it.copy(equation = eq) }
        isNewInput = true
    }

    private fun calculate() {
        performCalculation()
    }

    private fun performCalculation() {
        val currentValueStr = _uiState.value.display
        val currentValue = try {
            BigDecimal(currentValueStr)
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
        val operand1 = lastOperand ?: return
        val operation = pendingOperation ?: return

        if (operation == "÷" && currentValue.compareTo(BigDecimal.ZERO) == 0) {
            _uiState.update { it.copy(display = "undefined", equation = "") }
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

        _uiState.update { it.copy(display = formatBigDecimal(result), equation = "") }
        lastOperand = null
        pendingOperation = null
        isNewInput = true
    }

    private fun handleScientificOperation(operation: String) {
        val currentValueStr = _uiState.value.display
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
        _uiState.update { it.copy(display = formatResult(result)) }
        isNewInput = true
    }

    private fun reset() {
        _uiState.update { 
            it.copy(
                display = "0", 
                equation = "", 
                conversionInput = "0", 
                selectedConversion = null
            )
        }
        lastOperand = null
        pendingOperation = null
        isNewInput = true
        isNewConversionInput = true
    }

    private fun setMode(newMode: CalculatorMode) {
        _uiState.update { it.copy(mode = newMode) }
        reset()
    }

    private fun toggleScientific() {
        _uiState.update { it.copy(isScientificExpanded = !it.isScientificExpanded) }
    }

    private fun selectConversion(item: ConversionItem) {
        _uiState.update { it.copy(selectedConversion = item, conversionInput = "1") }
        isNewConversionInput = true
    }

    private fun backToConversionList() {
        _uiState.update { it.copy(selectedConversion = null, conversionInput = "0") }
        isNewConversionInput = true
    }

    fun getConversionResult(input: String, item: ConversionItem): String {
        val valDouble = try {
            input.toDouble()
        } catch (e: Exception) {
            0.0
        }
        
        return if (item.label == "Celsius to F") {
            formatResult((valDouble * 1.8) + 32)
        } else {
            formatResult(valDouble * item.factor)
        }
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
