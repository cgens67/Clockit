package com.clockit.cgens67.presentation.screens.alarm.components

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.clockit.cgens67.R
import com.clockit.cgens67.domain.model.Alarm
import com.clockit.cgens67.util.AlarmHelper

@Composable
fun AlarmCard(
    alarm: Alarm,
    onClick: () -> Unit,
    isAlarmEnabled: Boolean,
    onEnable: (Boolean) -> Unit
) {
    val context = LocalContext.current
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                val relativeTimeString = DateUtils.getRelativeTimeSpanString(
                    AlarmHelper.getAlarmTime(alarm),
                )
                alarm.label?.let {
                    Row(
                        modifier = Modifier
                            .padding(start = 5.dp, end = 10.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Label, null)
                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = it,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = alarm.formattedTime,
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = "$relativeTimeString.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Center) {
                Switch(
                    checked = isAlarmEnabled,
                    onCheckedChange = onEnable
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(Modifier.padding(horizontal = 8.dp)) {
                    when {
                        !alarm.repeat -> {
                            Text(text = stringResource(R.string.one_time), style = MaterialTheme.typography.labelLarge)
                        }

                        alarm.isRepeatEveryday -> {
                            Text(text = stringResource(R.string.repeating), style = MaterialTheme.typography.labelLarge)
                        }

                        alarm.isWeekends -> {
                            Text(text = stringResource(R.string.weekends), style = MaterialTheme.typography.labelLarge)
                        }

                        alarm.isWeekdays -> {
                            Text(text = stringResource(R.string.weekdays), style = MaterialTheme.typography.labelLarge)
                        }

                        else -> {
                            val daysOfWeek = remember {
                                AlarmHelper.getDaysOfWeekByLocale(context)
                            }
                            daysOfWeek.forEach { (day, index) ->
                                val enabled = alarm.days.contains(index)
                                Text(
                                    text = day,
                                    color = if (enabled) {
                                        MaterialTheme.colorScheme.onSurface
                                    } else {
                                        MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.5f
                                        )
                                    },
                                    fontWeight = if (enabled) FontWeight.Bold else FontWeight.Normal,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}