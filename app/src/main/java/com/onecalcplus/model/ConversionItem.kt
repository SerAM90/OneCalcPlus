package com.onecalcplus.model

data class ConversionItem(
    val label: String,
    val factor: Double,
    val type: String,
    val fromSymbol: String = "", // e.g., "$"
    val toSymbol: String = ""   // e.g., "¥"
)
