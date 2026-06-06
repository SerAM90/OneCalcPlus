package com.cs467.onecalcplus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs467.onecalcplus.CalculatorMode
import com.cs467.onecalcplus.CalculatorViewModel
import com.cs467.onecalcplus.ConversionItem
import androidx.compose.ui.tooling.preview.Preview
import com.cs467.onecalcplus.ui.theme.OneCalcPlusTheme

@Composable
fun ConversionLayout(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val mode = viewModel.mode.value
    val title = if (mode == CalculatorMode.UNIT_CONVERSION) "Unit Conversions" else "Kitchen Conversions"
    val selectedConversion = viewModel.selectedConversion.value
    val conversionInput = viewModel.conversionInput.value

    val unitConversions = listOf(
        ConversionItem("cm to inch", 0.393701, "length"),
        ConversionItem("m to ft", 3.28084, "length"),
        ConversionItem("kg to lb", 2.20462, "weight"),
        ConversionItem("km to mile", 0.621371, "length"),
        ConversionItem("Celsius to F", 1.8, "temperature"),
        ConversionItem("L to gal", 0.264172, "volume")
    )

    val kitchenConversions = listOf(
        ConversionItem("Cup to ml", 236.588, "volume"),
        ConversionItem("Tbsp to ml", 14.7868, "volume"),
        ConversionItem("tsp to ml", 4.92892, "volume"),
        ConversionItem("oz to g", 28.3495, "weight"),
        ConversionItem("lb to g", 453.592, "weight"),
        ConversionItem("Stick of Butter to g", 113.0, "weight")
    )

    val conversions = if (mode == CalculatorMode.UNIT_CONVERSION) unitConversions else kitchenConversions

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

        if (selectedConversion != null) {
            // Interactive Conversion UI
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = selectedConversion.label, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.tertiary)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = conversionInput, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "=", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = viewModel.getConversionResult(), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // Number Pad for Conversion Input
            val numPad = listOf(
                listOf("7", "8", "9"),
                listOf("4", "5", "6"),
                listOf("1", "2", "3"),
                listOf(".", "0", "⌫")
            )

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
                                        if (char == "⌫") viewModel.onBackspaceClick()
                                        else viewModel.onNumberClick(char)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = char, style = MaterialTheme.typography.titleMedium)
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
                            .clickable { viewModel.selectConversion(item) }
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
            }
        }
        
        CalcButton(
            text = if (selectedConversion != null) "Back to List" else "Back to Calculator",
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            onClick = { 
                if (selectedConversion != null) {
                    viewModel.clear()
                } else {
                    viewModel.setMode(CalculatorMode.CALCULATOR)
                }
            },
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ConversionLayoutPreview() {
    val viewModel = CalculatorViewModel()
    viewModel.setMode(CalculatorMode.UNIT_CONVERSION)
    OneCalcPlusTheme {
        ConversionLayout(viewModel = viewModel)
    }
}
