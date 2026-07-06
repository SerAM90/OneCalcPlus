package com.onecalcplus.model

/**
 * UI State for the Calculator, following MAD standards by grouping related fields.
 */
data class CalculatorUiState(
    val display: String = "0",
    val equation: String = "",
    val mode: CalculatorMode = CalculatorMode.CALCULATOR,
    val isScientificExpanded: Boolean = false,
    val conversionInput: String = "0",
    val selectedConversion: ConversionItem? = null,
    val customFromCurrency: String = "USD",
    val customToCurrency: String = "EUR"
)
