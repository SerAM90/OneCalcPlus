package com.onecalcplus.model

/**
 * UI Events to handle user interactions via Unidirectional Data Flow.
 */
sealed class CalculatorUiEvent {
    data class NumberClick(val number: String) : CalculatorUiEvent()
    data class OperationClick(val operation: String) : CalculatorUiEvent()
    data class ScientificOperationClick(val operation: String) : CalculatorUiEvent()
    object Calculate : CalculatorUiEvent()
    object Backspace : CalculatorUiEvent()
    object Clear : CalculatorUiEvent()
    data class SetMode(val mode: CalculatorMode) : CalculatorUiEvent()
    object ToggleScientific : CalculatorUiEvent()
    data class SelectConversion(val item: ConversionItem?) : CalculatorUiEvent()
    object BackToConversionList : CalculatorUiEvent()
    data class UpdateCustomCurrency(val from: String? = null, val to: String? = null) : CalculatorUiEvent()
}
