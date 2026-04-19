package com.codewithdipesh.ticketbooking.booking.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.ticketbooking.home.elements.customClickable


data class DayItem(val number: Int, val label: String)
data class TimeSlot(val display: String)


@Composable
fun WhenToWatchScreen(
    modifier: Modifier = Modifier
) {
    val days = remember {
        listOf(
            DayItem(11, "T"),
            DayItem(12, "W"),
            DayItem(13, "T"),
            DayItem(14, "F"),
            DayItem(15, "S"),
            DayItem(16, "S"),
            DayItem(17, "M"),
        )
    }
    val timeSlots = remember {
        listOf(
            TimeSlot("10:45 AM"),
            TimeSlot("02:45 PM"),
            TimeSlot("08:00 PM"),
        )
    }

    var selectedDay by rememberSaveable { mutableStateOf(1) }
    var selectedTime by rememberSaveable { mutableStateOf(1) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val visibleDays = if (expanded) days else days.take(4)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "When to Watch?",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Spacer(Modifier.height(2.dp))

        Text("Select date and time",
            color = Color.White.copy(0.5f), fontSize = 12.sp)
        Spacer(Modifier.height(20.dp))


        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            visibleDays.forEachIndexed { index, day ->
                DayButton(
                    day = day,
                    selected = selectedDay == index,
                    onClick = { selectedDay = index }
                )
            }
            val chevronRotation by animateFloatAsState(
                targetValue = if (expanded) 180f else 0f,
                label = "chevron"
            )
            Box(
                modifier = Modifier
                    .customClickable { expanded = !expanded }
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2A2A2A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color.White.copy(0.7f),
                    modifier = Modifier
                        .size(22.dp)
                        .graphicsLayer { rotationZ = chevronRotation }
                )
            }
        }

        Spacer(Modifier.height(24.dp))


        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            timeSlots.forEachIndexed { index, slot ->
                TimeSlotRow(
                    slot = slot,
                    selected = selectedTime == index,
                    onClick = { selectedTime = index }
                )
            }
        }
    }
}

@Composable
fun DayButton(
    day: DayItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (selected) Color(0xFFF5C800) else Color(0xFF2A2A2A),
        label = "dayBg"
    )
    val textColor = if (selected) Color.Black else Color.White

    Column(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(bgColor)
            .customClickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day.number.toString(),
            color = textColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 16.sp
        )
        Text(
            text = day.label,
            color = textColor.copy(alpha = if (selected) 0.7f else 0.5f),
            fontSize = 11.sp,
            lineHeight = 13.sp
        )
    }
}


@Composable
fun TimeSlotRow(
    slot: TimeSlot,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color(0xFF222222),
        label = "timeBg"
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) Color.Black else Color.White.copy(0.6f),
        label = "timeText"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .customClickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = slot.display,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}