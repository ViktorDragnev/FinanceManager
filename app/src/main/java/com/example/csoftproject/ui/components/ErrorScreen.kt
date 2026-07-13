package com.example.csoftproject.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.room.util.TableInfo
import com.example.csoftproject.R

@Composable
fun ErrorScreen(onRetry: () -> Unit) {
    
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.error_screen_status),
            color = MaterialTheme.colorScheme.error
        )
        
        Button(
            onClick = { onRetry() },
        ){
            Text(text = "Retry")
        }
    }
}