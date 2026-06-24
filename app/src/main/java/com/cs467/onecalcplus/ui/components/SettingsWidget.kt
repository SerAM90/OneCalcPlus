package com.cs467.onecalcplus.ui.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cs467.onecalcplus.CalculatorViewModel
import com.cs467.onecalcplus.R
import com.cs467.onecalcplus.model.CalculatorMode
import com.cs467.onecalcplus.model.CalculatorUiEvent

@Composable
fun SettingsWidget(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings_description),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.standard_calculator), style = MaterialTheme.typography.titleMedium) },
                onClick = {
                    viewModel.onEvent(CalculatorUiEvent.SetMode(CalculatorMode.CALCULATOR))
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.unit_conversions), style = MaterialTheme.typography.titleMedium) },
                onClick = {
                    viewModel.onEvent(CalculatorUiEvent.SetMode(CalculatorMode.UNIT_CONVERSION))
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.kitchen_conversions), style = MaterialTheme.typography.titleMedium) },
                onClick = {
                    viewModel.onEvent(CalculatorUiEvent.SetMode(CalculatorMode.KITCHEN_CONVERSION))
                    expanded = false
                }
            )
        }
    }
}
