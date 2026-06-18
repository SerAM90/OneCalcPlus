package com.cs467.onecalcplus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs467.onecalcplus.ui.theme.Primary
import com.cs467.onecalcplus.ui.theme.PrimaryContainer
import com.cs467.onecalcplus.ui.theme.SurfaceBright

@Composable
fun CalcButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    isAction: Boolean = false,
    shape: Shape = RoundedCornerShape(24.dp),
    fontSize: Int = 24
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundModifier = if (isAction) {
        Modifier.background(
            brush = Brush.linearGradient(
                colors = listOf(Primary, PrimaryContainer),
                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
            )
        )
    } else {
        Modifier.background(if (isPressed) SurfaceBright else backgroundColor)
    }

    Box(
        modifier = modifier
            .padding(4.dp)
            .clip(shape)
            .then(backgroundModifier)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isAction) MaterialTheme.colorScheme.onPrimaryContainer else contentColor,
            style = MaterialTheme.typography.titleMedium,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
