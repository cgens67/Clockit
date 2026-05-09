package com.clockit.cgens67.presentation.screens.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.clockit.cgens67.presentation.components.ClickableIcon
import com.clockit.cgens67.util.extensions.fadingEdge
import com.clockit.cgens67.util.extensions.performHaptic

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
    val view = LocalView.current
    
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("Language") },
                navigationIcon = {
                    ClickableIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack) {
                        onBack()
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { pv ->
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
                .fadingEdge(
                    isVisibleTop = listState.canScrollBackward,
                    isVisibleBottom = listState.canScrollForward,
                    length = 100f
                ),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(supportedLocales) { (displayName, tag) ->
                val isSelected = currentLocale == tag
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer 
                            else MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                        )
                        .clickable {
                            view.performHaptic()
                            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.titleLarge,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer 
                                    else MaterialTheme.colorScheme.onSurface
                        )
                        if (isSelected) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}