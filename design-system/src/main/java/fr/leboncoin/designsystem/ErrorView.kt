package fr.leboncoin.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme

@Composable
fun ErrorView(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = SparkTheme.typography.body1,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorViewPreview() {
    AppTheme {
        ErrorView(message = "Check your internet connection.")
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorViewLongMessagePreview() {
    AppTheme {
        ErrorView(
            message = "Something went wrong while loading this content. Please try again in a few moments.",
        )
    }
}
