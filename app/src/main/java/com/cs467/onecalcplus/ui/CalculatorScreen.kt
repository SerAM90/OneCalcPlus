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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cs467.onecalcplus.CalculatorMode
import com.cs467.onecalcplus.CalculatorUiEvent
import com.cs467.onecalcplus.CalculatorViewModel
import com.cs467.onecalcplus.R
import com.cs467.onecalcplus.ui.theme.OneCalcPlusTheme

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val mode = uiState.mode
    val isScientificExpanded = uiState.isScientificExpanded

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
                        onClick = { viewModel.onEvent(CalculatorUiEvent.ToggleScientific) },
                        modifier = Modifier
                            .background(
                                if (isScientificExpanded) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                else Color.Transparent,
                                shape = MaterialTheme.shapes.small
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = stringResource(R.string.scientific_mode_description),
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        CalculatorDisplay(
            equation = uiState.equation,
            result = uiState.display,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                                    "C" -> viewModel.onEvent(CalculatorUiEvent.Clear)
                                    "⌫" -> viewModel.onEvent(CalculatorUiEvent.Backspace)
                                    in listOf("÷", "×", "-", "+", "%") -> viewModel.onEvent(CalculatorUiEvent.OperationClick(text))
                                    else -> viewModel.onEvent(CalculatorUiEvent.NumberClick(text))
                                }
                            },
                            backgroundColor = if (isOperator) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isOperator) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(24.dp),
                            fontSize = 24
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalcButton(
                    text = "0",
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    onClick = { viewModel.onEvent(CalculatorUiEvent.NumberClick("0")) },
                    shape = RoundedCornerShape(24.dp),
                    fontSize = 24
                )
                CalcButton(
                    text = ".",
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    onClick = { viewModel.onEvent(CalculatorUiEvent.NumberClick(".")) },
                    shape = RoundedCornerShape(24.dp),
                    fontSize = 24
                )
                CalcButton(
                    text = "=",
                    modifier = Modifier.weight(2f).fillMaxHeight(),
                    onClick = { viewModel.onEvent(CalculatorUiEvent.Calculate) },
                    isAction = true,
                    shape = RoundedCornerShape(48.dp),
                    fontSize = 24
                )
            }
        }
    }
}

@Composable
fun ScientificCalculatorView(viewModel: CalculatorViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        CalculatorDisplay(
            equation = uiState.equation,
            result = uiState.display,
            modifier = Modifier.weight(0.8f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth().weight(3f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Scientific Column
            Column(
                modifier = Modifier.weight(0.6f).fillMaxHeight(),
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
                                onClick = { viewModel.onEvent(CalculatorUiEvent.ScientificOperationClick(text)) },
                                backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                contentColor = MaterialTheme.colorScheme.tertiary,
                                shape = RoundedCornerShape(8.dp),
                                fontSize = 13
                            )
                        }
                    }
                }
            }

            // Standard Column
            Column(
                modifier = Modifier.weight(1.4f).fillMaxHeight(),
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
                                        "C" -> viewModel.onEvent(CalculatorUiEvent.Clear)
                                        "⌫" -> viewModel.onEvent(CalculatorUiEvent.Backspace)
                                        in listOf("÷", "×", "-", "+", "%") -> viewModel.onEvent(CalculatorUiEvent.OperationClick(text))
                                        else -> viewModel.onEvent(CalculatorUiEvent.NumberClick(text))
                                    }
                                },
                                backgroundColor = if (isOperator) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isOperator) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurface,
                                shape = RoundedCornerShape(8.dp),
                                fontSize = 22
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        CalcButton(
            text = "=",
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            onClick = { viewModel.onEvent(CalculatorUiEvent.Calculate) },
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
    viewModel.onEvent(CalculatorUiEvent.ToggleScientific)
    OneCalcPlusTheme {
        CalculatorScreen(viewModel = viewModel)
    }
}
