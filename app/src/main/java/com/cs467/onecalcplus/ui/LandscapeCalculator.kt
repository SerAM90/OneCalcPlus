package com.cs467.onecalcplus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cs467.onecalcplus.CalculatorViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.cs467.onecalcplus.ui.theme.OneCalcPlusTheme

@Composable
fun LandscapeCalculator(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Scientific Panel (Left)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val scientificButtons = listOf(
                listOf("sin", "cos", "tan"),
                listOf("log", "ln", "√"),
                listOf("π", "e", "^"),
                listOf("x²", "1/x", "|x|")
            )
            
            scientificButtons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    row.forEach { text ->
                        CalcButton(
                            text = text,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.onScientificOperation(text) },
                            backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            contentColor = MaterialTheme.colorScheme.tertiary,
                            isScientific = true
                        )
                    }
                }
            }
        }

        // Standard Panel (Right)
        Column(
            modifier = Modifier.weight(1.5f)
        ) {
            CalculatorDisplay(
                equation = viewModel.equationState.value,
                result = viewModel.displayState.value,
                modifier = Modifier.weight(0.4f)
            )

            val buttons = listOf(
                listOf("C", "÷", "%", "⌫"),
                listOf("7", "8", "9", "×"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf("0", ".", "=")
            )

            buttons.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    row.forEach { text ->
                        val isAction = text == "="
                        val isOperator = text in listOf("÷", "×", "-", "+", "%", "⌫", "C")
                        
                        CalcButton(
                            text = text,
                            modifier = Modifier.weight(if (text == "=") 2f else 1f),
                            onClick = {
                                when (text) {
                                    "C" -> viewModel.clear()
                                    "⌫" -> viewModel.onBackspaceClick()
                                    "=" -> viewModel.calculate()
                                    in listOf("÷", "×", "-", "+", "%") -> viewModel.onOperationClick(text)
                                    else -> viewModel.onNumberClick(text)
                                }
                            },
                            backgroundColor = if (isOperator) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isOperator) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurface,
                            isAction = isAction
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun LandscapeCalculatorPreview() {
    OneCalcPlusTheme {
        LandscapeCalculator(viewModel = CalculatorViewModel())
    }
}
