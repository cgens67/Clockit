package com.clockit.cgens67.presentation.screens.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.clockit.cgens67.presentation.components.ClickableIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagePickerScreen(onBack: () -> Unit) {
    val supportedLocales = listOf(
        "System Default" to "",
        "English" to "en",
        "Spanish (Español)" to "es",
        "French (Français)" to "fr",
        "German (Deutsch)" to "de",
        "Malay (Bahasa Melayu)" to "ms",
        "Chinese (中文)" to "zh"
    )

    val currentLocale = AppCompatDelegate.getApplicationLocales().toLanguageTags().ifEmpty { "" }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Language") },
                navigationIcon = {
                    ClickableIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack) {
                        onBack()
                    }
                }
            )
        }
    ) { pv ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
        ) {
            items(supportedLocales) { (displayName, tag) ->
                val isSelected = currentLocale == tag
                ListItem(
                    headlineContent = { Text(displayName, style = MaterialTheme.typography.titleLarge) },
                    trailingContent = {
                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = "Selected", tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    modifier = Modifier.clickable {
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}