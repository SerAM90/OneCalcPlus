package com.cs467.onecalcplus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs467.onecalcplus.CalculatorMode
import com.cs467.onecalcplus.CalculatorViewModel
import com.cs467.onecalcplus.ui.theme.OneCalcPlusTheme

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val mode = viewModel.mode.value
    val isScientificExpanded = viewModel.isScientificExpanded.value

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingsWidget(viewModel = viewModel, modifier = Modifier.weight(1f))
                
                if (mode == CalculatorMode.CALCULATOR) {
                    IconButton(
                        onClick = { viewModel.toggleScientific() },
                        modifier = Modifier
                            .background(
                                if (isScientificExpanded) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                else Color.Transparent,
                                shape = MaterialTheme.shapes.small
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = "Scientific Mode",
                            tint = if (isScientificExpanded) MaterialTheme.colorScheme.primary 
                                   else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (mode) {
                CalculatorMode.CALCULATOR -> {
                    if (isScientificExpanded) {
                        ScientificCalculatorView(viewModel)
                    } else {
                        StandardCalculatorView(viewModel)
                    }
                }
                CalculatorMode.UNIT_CONVERSION, CalculatorMode.KITCHEN_CONVERSION -> {
                    ConversionLayout(viewModel)
                }
            }
        }
    }
}

@Composable
fun StandardCalculatorView(viewModel: CalculatorViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        CalculatorDisplay(
            equation = viewModel.equationState.value,
            result = viewModel.displayState.value,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Standard Layout - completely untouched by Scientific Mode
        Column(
            modifier = Modifier.weight(2f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val standardButtons = listOf(
                listOf("C", "÷", "%", "⌫"),
                listOf("7", "8", "9", "×"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+")
            )

            standardButtons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { text ->
                        val isOperator = text in listOf("÷", "×", "-", "+", "%", "⌫", "C")
                        CalcButton(
                            text = text,
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            onClick = {
                                when (text) {
                                    "C" -> viewModel.clear()
                                    "⌫" -> viewModel.onBackspaceClick()
                                    in listOf("÷", "×", "-", "+", "%") -> viewModel.onOperationClick(text)
                                    else -> viewModel.onNumberClick(text)
                                }
                            },
                            backgroundColor = if (isOperator) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isOperator) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(24.dp), // CIRCLE
                            fontSize = 24
                        )
                    }
                }
            }

            // Bottom row for 0, ., and = (Original pill shape for =)
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "0",
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    onClick = { viewModel.onNumberClick("0") },
                    shape = RoundedCornerShape(24.dp),
                    fontSize = 24
                )
                CalcButton(
                    text = ".",
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    onClick = { viewModel.onNumberClick(".") },
                    shape = RoundedCornerShape(24.dp),
                    fontSize = 24
                )
                CalcButton(
                    text = "=",
                    modifier = Modifier.weight(2f).fillMaxHeight(),
                    onClick = { viewModel.calculate() },
                    isAction = true,
                    shape = RoundedCornerShape(48.dp), // PILL
                    fontSize = 24
                )
            }
        }
    }
}

@Composable
fun ScientificCalculatorView(viewModel: CalculatorViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        CalculatorDisplay(
            equation = viewModel.equationState.value,
            result = viewModel.displayState.value,
            modifier = Modifier.weight(0.8f) // Reduced display weight to give keys more room
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Main Grid Area
        Row(
            modifier = Modifier.fillMaxWidth().weight(3f), // Significantly increased weight for buttons
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Scientific Column (SMALLER)
            Column(
                modifier = Modifier.weight(0.6f).fillMaxHeight(), // Further reduced weight
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val scientificButtons = listOf(
                    listOf("sin", "cos"),
                    listOf("tan", "log"),
                    listOf("ln", "√"),
                    listOf("π", "e"),
                    listOf("^", "x²"),
                    listOf("|x|", "exp")
                )
                scientificButtons.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        row.forEach { text ->
                            CalcButton(
                                text = text,
                                modifier = Modifier.weight(1f).fillMaxHeight(),
                                onClick = { viewModel.onScientificOperation(text) },
                                backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                contentColor = MaterialTheme.colorScheme.tertiary,
                                shape = RoundedCornerShape(8.dp),
                                fontSize = 13 // Slightly smaller font
                            )
                        }
                    }
                }
            }

            // Standard Column (LARGER)
            Column(
                modifier = Modifier.weight(1.4f).fillMaxHeight(), // Further increased weight
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val standardRows = listOf(
                    listOf("C", "÷", "%", "⌫"),
                    listOf("7", "8", "9", "×"),
                    listOf("4", "5", "6", "-"),
                    listOf("1", "2", "3", "+"),
                    listOf("0", ".")
                )
                
                standardRows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        row.forEach { text ->
                            val isOperator = text in listOf("÷", "×", "-", "+", "%", "⌫", "C")
                            CalcButton(
                                text = text,
                                modifier = Modifier.weight(if (text == "0") 2f else 1f).fillMaxHeight(),
                                onClick = {
                                    when (text) {
                                        "C" -> viewModel.clear()
                                        "⌫" -> viewModel.onBackspaceClick()
                                        in listOf("÷", "×", "-", "+", "%") -> viewModel.onOperationClick(text)
                                        else -> viewModel.onNumberClick(text)
                                    }
                                },
                                backgroundColor = if (isOperator) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isOperator) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurface,
                                shape = RoundedCornerShape(8.dp),
                                fontSize = 22 // Even larger font for standard
                            )
                        }
                    }
                }
            }
        }

        // Full-width Equals Footer - Sleeker Height
        Spacer(modifier = Modifier.height(8.dp))
        CalcButton(
            text = "=",
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp), // Further reduced height for sleek action bar
            onClick = { viewModel.calculate() },
            isAction = true,
            shape = RoundedCornerShape(8.dp),
            fontSize = 24
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun CalculatorScreenPreview() {
    OneCalcPlusTheme {
        CalculatorScreen(viewModel = CalculatorViewModel())
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ScientificExpandedPreview() {
    val viewModel = CalculatorViewModel()
    viewModel.toggleScientific()
    OneCalcPlusTheme {
        CalculatorScreen(viewModel = viewModel)
    }
}
