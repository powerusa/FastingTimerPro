package com.fastingtimerpro.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.res.stringResource
import com.fastingtimerpro.app.R
import com.fastingtimerpro.app.ui.theme.AppColors
import java.time.Duration

@Composable
fun CustomDurationPickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Duration) -> Unit
) {
    var days by remember { mutableIntStateOf(0) }
    var hours by remember { mutableIntStateOf(16) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(AppColors.baseBackground)
                .border(
                    width = 1.5.dp,
                    color = AppColors.cardBorder,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.custom_duration_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.primaryText
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.custom_duration_subtitle),
                    fontSize = 14.sp,
                    color = AppColors.secondaryText
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NumberPicker(
                        label = stringResource(R.string.custom_duration_days),
                        value = days,
                        range = 0..30,
                        onValueChange = { days = it }
                    )

                    NumberPicker(
                        label = stringResource(R.string.custom_duration_hours),
                        value = hours,
                        range = 0..23,
                        onValueChange = { hours = it }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val totalHours = days * 24 + hours
                val totalText = if (days > 0 && hours > 0) "${days}d ${hours}h"
                           else if (days > 0) "${days}d"
                           else "${hours}h"
                Text(
                    text = stringResource(R.string.custom_duration_total_format, totalText),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.primaryAccent
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.secondaryButtonBackground
                        ),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.common_cancel),
                            color = AppColors.primaryText,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = {
                            if (totalHours >= 1) {
                                onConfirm(Duration.ofHours(totalHours.toLong()))
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = totalHours >= 1,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.primaryButtonBackground,
                            disabledContainerColor = AppColors.primaryButtonBackground.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.common_start_fast),
                            color = AppColors.primaryText,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NumberPicker(
    label: String,
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = AppColors.secondaryText
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            PickerButton(
                text = "−",
                enabled = value > range.first,
                onClick = { if (value > range.first) onValueChange(value - 1) }
            )

            Text(
                text = value.toString().padStart(2, '0'),
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.brightRed,
                modifier = Modifier.width(48.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 1
            )

            PickerButton(
                text = "+",
                enabled = value < range.last,
                onClick = { if (value < range.last) onValueChange(value + 1) }
            )
        }
    }
}

@Composable
private fun PickerButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(38.dp)
            .height(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(AppColors.primaryAccent.copy(alpha = if (enabled) 0.15f else 0.05f))
            .border(
                width = 1.5.dp,
                color = AppColors.primaryAccent.copy(alpha = if (enabled) 1f else 0.3f),
                shape = RoundedCornerShape(10.dp)
            )
            .then(if (enabled) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.primaryAccent.copy(alpha = if (enabled) 1f else 0.3f)
        )
    }
}
