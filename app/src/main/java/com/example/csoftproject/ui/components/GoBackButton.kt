package com.example.csoftproject.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.csoftproject.R

@Composable
fun BackButton(navController : NavController, modifier: Modifier) {
    IconButton(
        modifier = Modifier
            .size(24.dp),
        onClick = { navController.popBackStack() },

        ) {
        Icon(
            painter = painterResource(R.drawable.back),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "back_button"
        )
    }
}