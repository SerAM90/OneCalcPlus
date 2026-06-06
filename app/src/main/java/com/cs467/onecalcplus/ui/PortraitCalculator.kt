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
fun PortraitCalculator(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CalculatorDisplay(
            equation = viewModel.equationState.value,
            result = viewModel.displayState.value,
            modifier = Modifier.weight(1f)
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
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PortraitCalculatorPreview() {
    OneCalcPlusTheme {
        PortraitCalculator(viewModel = CalculatorViewModel())
    }
}
