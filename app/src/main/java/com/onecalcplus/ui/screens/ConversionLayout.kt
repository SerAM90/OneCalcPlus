package com.onecalcplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onecalcplus.CalculatorViewModel
import com.onecalcplus.R
import com.onecalcplus.model.CalculatorMode
import com.onecalcplus.model.CalculatorUiEvent
import com.onecalcplus.model.ConversionItem
import com.onecalcplus.ui.components.CalcButton
import com.onecalcplus.ui.theme.OneCalcPlusTheme

@Composable
fun ConversionLayout(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    val title = when (uiState.mode) {
        CalculatorMode.UNIT_CONVERSION -> stringResource(R.string.unit_conversions)
        CalculatorMode.KITCHEN_CONVERSION -> stringResource(R.string.kitchen_conversions)
        CalculatorMode.CURRENCY_CONVERSION -> stringResource(R.string.currency_conversions)
        else -> ""
    }

    val selectedConversion = uiState.selectedConversion
    val conversionInput = uiState.conversionInput

    // Optimization: Calculate result only when inputs change
    val conversionResult = remember(conversionInput, selectedConversion, uiState.customFromCurrency, uiState.customToCurrency) {
        viewModel.getConversionResult(
            conversionInput, 
            selectedConversion,
            uiState.customFromCurrency,
            uiState.customToCurrency
        )
    }

    val unitConversions = remember {
        listOf(
            // Length
            ConversionItem("cm to inch", 0.393701, "length"),
            ConversionItem("m to ft", 3.28084, "length"),
            ConversionItem("km to mile", 0.621371, "length"),
            ConversionItem("inch to cm", 2.54, "length"),
            ConversionItem("ft to m", 0.3048, "length"),
            
            // Weight/Mass
            ConversionItem("kg to lb", 2.20462, "weight"),
            ConversionItem("lb to kg", 0.453592, "weight"),
            ConversionItem("g to oz", 0.035274, "weight"),
            ConversionItem("oz to g", 28.3495, "weight"),
            
            // Volume
            ConversionItem("L to gal", 0.264172, "volume"),
            ConversionItem("gal to L", 3.78541, "volume"),
            ConversionItem("ml to fl oz", 0.033814, "volume"),
            
            ConversionItem("Celsius to F", 1.8, "temperature")
        )
    }

    val kitchenConversions = remember {
        listOf(
            // Volume
            ConversionItem("Cup to ml", 236.588, "volume"),
            ConversionItem("ml to Cup", 0.00422675, "volume"),
            ConversionItem("Tbsp to ml", 14.7868, "volume"),
            ConversionItem("tsp to ml", 4.92892, "volume"),
            ConversionItem("fl oz to ml", 29.5735, "volume"),
            ConversionItem("Pint to ml", 473.176, "volume"),
            ConversionItem("Quart to ml", 946.353, "volume"),
            
            // Weight
            ConversionItem("oz to g", 28.3495, "weight"),
            ConversionItem("lb to g", 453.592, "weight"),
            ConversionItem("Stick of Butter to g", 113.0, "weight"),
            ConversionItem("Stick of Butter to Cup", 0.5, "volume")
        )
    }

    val currencyConversions = remember {
        listOf(
            // USD as base
            ConversionItem("USD to EUR", 0.95, "currency", "$", "€"),
            ConversionItem("EUR to USD", 1.05, "currency", "€", "$"),
            ConversionItem("USD to GBP", 0.79, "currency", "$", "£"),
            ConversionItem("GBP to USD", 1.27, "currency", "£", "$"),
            ConversionItem("USD to JPY", 150.0, "currency", "$", "¥"),
            ConversionItem("JPY to USD", 0.0067, "currency", "¥", "$"),
            ConversionItem("USD to CAD", 1.40, "currency", "$", "C$"),
            ConversionItem("CAD to USD", 0.71, "currency", "C$", "$"),
            ConversionItem("USD to AUD", 1.55, "currency", "$", "A$"),
            ConversionItem("AUD to USD", 0.65, "currency", "A$", "$")
        )
    }

    val conversions = when (uiState.mode) {
        CalculatorMode.UNIT_CONVERSION -> unitConversions
        CalculatorMode.KITCHEN_CONVERSION -> kitchenConversions
        CalculatorMode.CURRENCY_CONVERSION -> currencyConversions
        else -> emptyList()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (selectedConversion != null || (uiState.mode == CalculatorMode.CURRENCY_CONVERSION && conversionInput != "0")) {
            // Interactive Conversion UI
            val fromText = if (selectedConversion != null) {
                if (selectedConversion.fromSymbol.isNotEmpty()) {
                    "$conversionInput ${selectedConversion.fromSymbol}"
                } else conversionInput
            } else {
                "$conversionInput ${getCurrencySymbol(uiState.customFromCurrency)}"
            }
            
            val toText = if (selectedConversion != null) {
                if (selectedConversion.toSymbol.isNotEmpty()) {
                    "$conversionResult ${selectedConversion.toSymbol}"
                } else conversionResult
            } else {
                "$conversionResult ${getCurrencySymbol(uiState.customToCurrency)}"
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    val label = selectedConversion?.label ?: "${uiState.customFromCurrency} to ${uiState.customToCurrency}"
                    Text(text = label, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.tertiary)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = fromText, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "=", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = toText, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            if (selectedConversion == null && uiState.mode == CalculatorMode.CURRENCY_CONVERSION) {
                // Dropdown selections for Custom Currency
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CurrencyDropdown(
                        selected = uiState.customFromCurrency,
                        onSelect = { viewModel.onEvent(CalculatorUiEvent.UpdateCustomCurrency(from = it)) },
                        modifier = Modifier.weight(1f)
                    )
                    CurrencyDropdown(
                        selected = uiState.customToCurrency,
                        onSelect = { viewModel.onEvent(CalculatorUiEvent.UpdateCustomCurrency(to = it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Number Pad for Conversion Input
            val numPad = remember {
                listOf(
                    listOf("7", "8", "9"),
                    listOf("4", "5", "6"),
                    listOf("1", "2", "3"),
                    listOf(".", "0", "⌫", "C")
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                numPad.forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { char ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 4.dp)
                                    .height(60.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                    .clickable { 
                                        when (char) {
                                            "⌫" -> viewModel.onEvent(CalculatorUiEvent.Backspace)
                                            "C" -> viewModel.onEvent(CalculatorUiEvent.SelectConversion(selectedConversion))
                                            else -> viewModel.onEvent(CalculatorUiEvent.NumberClick(char))
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = char, 
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (char == "C") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Selection List
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(conversions) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clickable { viewModel.onEvent(CalculatorUiEvent.SelectConversion(item)) }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                
                if (uiState.mode == CalculatorMode.CURRENCY_CONVERSION) {
                    item(span = { GridItemSpan(2) }) {
                        Button(
                            onClick = { viewModel.onEvent(CalculatorUiEvent.NumberClick("1")) }, // Trigger interactive mode with initial "1"
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Text(
                                stringResource(R.string.custom_currency_conversion),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
        
        CalcButton(
            text = if (selectedConversion != null || (uiState.mode == CalculatorMode.CURRENCY_CONVERSION && conversionInput != "0")) stringResource(R.string.back_to_list) else stringResource(R.string.back_to_calculator),
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            onClick = { 
                if (selectedConversion != null || (uiState.mode == CalculatorMode.CURRENCY_CONVERSION && conversionInput != "0")) {
                    viewModel.onEvent(CalculatorUiEvent.BackToConversionList)
                } else {
                    viewModel.onEvent(CalculatorUiEvent.SetMode(CalculatorMode.CALCULATOR))
                }
            },
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Composable
fun CurrencyDropdown(
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val currencies = listOf("USD", "EUR", "GBP", "JPY", "CAD", "AUD")

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
        ) {
            Text(text = selected)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(currency) },
                    onClick = {
                        onSelect(currency)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun getCurrencySymbol(code: String): String {
    return when (code) {
        "USD" -> "$"
        "EUR" -> "€"
        "GBP" -> "£"
        "JPY" -> "¥"
        "CAD" -> "C$"
        "AUD" -> "A$"
        else -> code
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun CurrencyConversionPreview() {
    val viewModel = CalculatorViewModel()
    viewModel.onEvent(CalculatorUiEvent.SetMode(CalculatorMode.CURRENCY_CONVERSION))
    val yenItem = ConversionItem("USD to JPY", 150.0, "currency", "$", "¥")
    viewModel.onEvent(CalculatorUiEvent.SelectConversion(yenItem))
    OneCalcPlusTheme {
        ConversionLayout(viewModel = viewModel)
    }
}
