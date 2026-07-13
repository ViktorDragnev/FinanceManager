package com.example.csoftproject.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.csoftproject.ui.theme.Blue500
import com.example.csoftproject.ui.theme.BlueGrey500
import com.example.csoftproject.ui.theme.BorderLarge
import com.example.csoftproject.ui.theme.BorderMedium
import com.example.csoftproject.ui.theme.Brown500
import com.example.csoftproject.ui.theme.ColorPaletteBoxSize
import com.example.csoftproject.ui.theme.Cyan500
import com.example.csoftproject.ui.theme.DeepOrange500
import com.example.csoftproject.ui.theme.DeepPurple500
import com.example.csoftproject.ui.theme.Green500
import com.example.csoftproject.ui.theme.LightBlue500
import com.example.csoftproject.ui.theme.LightGreen500
import com.example.csoftproject.ui.theme.Orange500
import com.example.csoftproject.ui.theme.Pink500
import com.example.csoftproject.ui.theme.Purple500
import com.example.csoftproject.ui.theme.Red500
import com.example.csoftproject.ui.theme.SpaceSmall
import com.example.csoftproject.ui.theme.Teal500
import com.example.csoftproject.ui.theme.Yellow500

@Composable
fun ColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Red500,
        Pink500,
        Purple500,
        DeepPurple500,
        Blue500,
        LightBlue500,
        Cyan500,
        Teal500,
        Green500,
        LightGreen500,
        Yellow500,
        Orange500,
        DeepOrange500,
        Brown500,
        BlueGrey500,
    )

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SpaceSmall),
        verticalArrangement = Arrangement.spacedBy(SpaceSmall)
    ) {
        colors.forEach { color ->
            ColorOption(
                color = color,
                isSelected = color == selectedColor,
                onClick = { onColorSelected(color) }
            )
        }
    }
}

@Composable
fun ColorOption(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(ColorPaletteBoxSize)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (isSelected) BorderLarge else BorderMedium,
                color = if (isSelected) Color.Black else Color.Gray,
                shape = CircleShape
            )
            .clickable { onClick() }
    )
}