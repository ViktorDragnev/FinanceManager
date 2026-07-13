package com.example.csoftproject.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.csoftproject.ui.theme.MediumStroke

@Composable
fun LoadingBar() {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val boxSize = screenWidth * 0.3f
    val bar = screenWidth * 0.2f

    Box(
        modifier = Modifier
            .size(boxSize)
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(bar),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = MediumStroke
        )
    }
}