package fr.leboncoin.designsystem

import androidx.compose.runtime.Composable
import com.adevinta.spark.SparkTheme

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    SparkTheme(content = content)
}
