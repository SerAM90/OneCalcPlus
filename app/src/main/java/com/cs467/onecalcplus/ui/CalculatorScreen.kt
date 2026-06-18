package com.cs467.onecalcplus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
                    CalculatorLayout(viewModel)
                }
                CalculatorMode.UNIT_CONVERSION, CalculatorMode.KITCHEN_CONVERSION -> {
                    ConversionLayout(viewModel)
                }
            }
        }
    }
}

@Composable
fun CalculatorLayout(viewModel: CalculatorViewModel) {
    val isScientificExpanded = viewModel.isScientificExpanded.value
    
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

        Row(
            modifier = Modifier.fillMaxWidth().weight(2f), // Increased weight for button area
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (isScientificExpanded) {
                ScientificColumn(
                    viewModel = viewModel,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }

            StandardColumn(
                viewModel = viewModel,
                isScientificExpanded = isScientificExpanded,
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
        }

        if (isScientificExpanded) {
            // Full-width Equals button for scientific mode - further reduced height
            Spacer(modifier = Modifier.height(8.dp))
            CalcButton(
                text = "=",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { viewModel.calculate() },
                isAction = true
            )
        }
    }
}

@Composable
fun ScientificColumn(viewModel: CalculatorViewModel, modifier: Modifier = Modifier) {
    val scientificButtons = listOf(
        listOf("sin", "cos"),
        listOf("tan", "log"),
        listOf("ln", "√"),
        listOf("π", "e"),
        listOf("^", "x²"),
        listOf("|x|", "exp")
    )
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
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
                        isScientific = true
                    )
                }
            }
        }
    }
}

@Composable
fun StandardColumn(
    viewModel: CalculatorViewModel, 
    isScientificExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    val standardButtons = listOf(
        listOf("C", "÷", "%", "⌫"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+")
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(if (isScientificExpanded) 4.dp else 8.dp)
    ) {
        standardButtons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(if (isScientificExpanded) 4.dp else 8.dp)
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
                        isScientific = isScientificExpanded
                    )
                }
            }
        }

        // Bottom row for 0 and .
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(if (isScientificExpanded) 4.dp else 8.dp)
        ) {
            CalcButton(
                text = "0",
                modifier = Modifier.weight(if (isScientificExpanded) 1f else 1f).fillMaxHeight(),
                onClick = { viewModel.onNumberClick("0") },
                isScientific = isScientificExpanded
            )
            CalcButton(
                text = ".",
                modifier = Modifier.weight(1f).fillMaxHeight(),
                onClick = { viewModel.onNumberClick(".") },
                isScientific = isScientificExpanded
            )
            
            if (!isScientificExpanded) {
                CalcButton(
                    text = "=",
                    modifier = Modifier.weight(2f).fillMaxHeight(),
                    onClick = { viewModel.calculate() },
                    isAction = true
                )
            } else {
                Spacer(modifier = Modifier.weight(2f))
            }
        }
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
