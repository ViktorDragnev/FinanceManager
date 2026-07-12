package com.example.csoftproject.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.csoftproject.R

@Composable
fun BoxScope.ButtonToNavigateToAddForms(navController: NavController, navigationRoute: String) {
    FloatingActionButton(
        onClick = { navController.navigate(navigationRoute) },
        modifier = Modifier
            .size(70.dp)
            .padding(16.dp)
            .align(Alignment.BottomEnd),
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.surfaceBright
    ) {
        Icon(
            painter = painterResource(R.drawable.plus),
            contentDescription = null
        )
    }
}