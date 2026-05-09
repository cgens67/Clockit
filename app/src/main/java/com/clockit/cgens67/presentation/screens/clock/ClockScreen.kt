package com.clockit.cgens67.presentation.screens.clock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.clockit.cgens67.R
import com.clockit.cgens67.domain.model.TimeZoneSortOrder
import com.clockit.cgens67.navigation.TopBarScaffold
import com.clockit.cgens67.presentation.components.BlobIconBox
import com.clockit.cgens67.presentation.components.ClickableIcon
import com.clockit.cgens67.presentation.screens.clock.components.DigitalClockDisplay
import com.clockit.cgens67.presentation.screens.clock.components.TimeZoneSelectDialog
import com.clockit.cgens67.presentation.screens.clock.components.WorldClockItem
import com.clockit.cgens67.presentation.screens.clock.model.ClockModel
import com.clockit.cgens67.util.Preferences
import com.clockit.cgens67.util.extensions.fadingEdge

@Composable
fun ClockScreen(
    onClickSettings: () -> Unit, clockModel: ClockModel
) {
    var showTimeZoneDialog by remember {
        mutableStateOf(false)
    }
    val selectedZones by clockModel.selectedTimeZones.collectAsState()

    TopBarScaffold(title = stringResource(R.string.clock), onClickSettings, actions = {
        if (selectedZones.size >= 2) {
            TopBarActions(clockModel)
        }
    }, fab = {
        FloatingActionButton(onClick = {
            showTimeZoneDialog = true
        }) {
            Icon(Icons.Rounded.Add, null)
        }
    }) { pv ->

        val listState = androidx.compose.foundation.lazy.rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .padding(pv)
                .fadingEdge(
                    isVisibleTop = listState.canScrollBackward,
                    isVisibleBottom = listState.canScrollForward,
                    length = 100f
                )
        ) {
            item {
                DigitalClockDisplay()
            }
            
            if (selectedZones.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize()) {
                        BlobIconBox(
                            icon = R.drawable.ic_language,
                            title = stringResource(R.string.no_world_clocks_yet),
                            subtitle = stringResource(R.string.no_world_clocks_subtitle)
                        )
                    }
                }
            }
            
            items(items = selectedZones, key = { it.key }) { timeZone ->
                WorldClockItem(clockModel, timeZone)
            }
        }
    }

    if (showTimeZoneDialog) {
        TimeZoneSelectDialog(clockModel, onDismissRequest = {
            showTimeZoneDialog = false
        })
    }
}

@Composable
private fun TopBarActions(clockModel: ClockModel) {
    Box {
        var showDropdown by remember {
            mutableStateOf(false)
        }

        ClickableIcon(imageVector = Icons.AutoMirrored.Filled.Sort) {
            showDropdown = true
        }

        DropdownMenu(expanded = showDropdown, onDismissRequest = { showDropdown = false }) {
            TimeZoneSortOrder.entries.forEach {
                DropdownMenuItem(text = {
                    Text(stringResource(it.value))
                }, onClick = {
                    clockModel.updateSortOrder(it)
                    Preferences.edit {
                        putString(Preferences.clockSortOrder, it.name)
                    }
                    showDropdown = false
                })
            }
        }
    }
}