package com.cs467.onecalcplus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cs467.onecalcplus.CalculatorMode
import com.cs467.onecalcplus.CalculatorViewModel

@Composable
fun SettingsWidget(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopStart // Original position
    ) {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            DropdownMenuItem(
                text = { Text("Standard Calculator", style = MaterialTheme.typography.titleMedium) },
                onClick = {
                    viewModel.setMode(CalculatorMode.CALCULATOR)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Unit Conversions", style = MaterialTheme.typography.titleMedium) },
                onClick = {
                    viewModel.setMode(CalculatorMode.UNIT_CONVERSION)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Kitchen Conversions", style = MaterialTheme.typography.titleMedium) },
                onClick = {
                    viewModel.setMode(CalculatorMode.KITCHEN_CONVERSION)
                    expanded = false
                }
            )
        }
    }
}
