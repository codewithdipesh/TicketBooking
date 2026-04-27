package com.codewithdipesh.ticketbooking.booking.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.ticketbooking.home.elements.customClickable
import kotlinx.serialization.Serializable

@Serializable
data class DayItem(val number: Int, val label: String, val fullLabel: String = "") : java.io.Serializable
data class TimeSlot(val display: String , val availability : Float )


@Composable
fun WhenToWatchScreen(
    modifier: Modifier = Modifier,
    onContinue : (day: DayItem, time: String) -> Unit
) {
    val days = remember {
        listOf(
            DayItem(11, "T" , "Tue, May 11"),
            DayItem(12, "W", "Wed, May 12"),
            DayItem(13, "T", "Thur, May 13"),
            DayItem(14, "F", "Fri, May 14"),
            DayItem(15, "S", "Sat, May 15"),
            DayItem(16, "S", "Sun, May 16"),
            DayItem(17, "M", "Mon, May 17"),
        )
    }
    val timeSlots = remember {
        listOf(
            TimeSlot("10:45 AM" , 0.5f),
            TimeSlot("02:45 PM" , 0.7f),
            TimeSlot("08:00 PM" , 0.3f),
        )
    }

    var selectedDay by rememberSaveable { mutableStateOf<DayItem?>(null) }
    var selectedTime by rememberSaveable { mutableStateOf(-1) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val visibleDays = if (expanded) days else days.take(4)

    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "chevron"
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ){
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

            //date Buttons 
            LazyRow (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                items(visibleDays){
                    DayButton(
                        day = it,
                        selected = selectedDay?.number == it.number,
                        onClick = { selectedDay = it }
                    )
                }
                item {
                    Box(
                        modifier = Modifier
                            .customClickable { expanded = !expanded }
                            .size(62.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2A2A2A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = if (expanded) "Collapse" else "Expand",
                            tint = Color.White,
                            modifier = Modifier
                                .size(22.dp)
                                .graphicsLayer { rotationZ = chevronRotation }
                        )
                    }
                }
            }

            Spacer(Modifier.height(40.dp))
            //show the time slots only after the date selection
            AnimatedVisibility (
                visible = selectedDay != null,
                enter = fadeIn(tween(200))
                    .plus(scaleIn(tween(200))),
                exit = fadeOut(tween(300))
            ){
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    timeSlots.forEachIndexed { index, slot ->
                        TimeSlotBar(
                            slot = slot,
                            selected = selectedTime == index,
                            onClick = { selectedTime = index }
                        )
                    }
                }
            }
            //show text for not sleecting date
            if(selectedDay == null){
                Spacer(Modifier.height(96.dp))
                Text(
                    text = "Select a day to see the \n available showtimes",
                    color = Color.White.copy(0.5f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth(),
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        //continue button
        Box(
            modifier = Modifier
                .customClickable(
                    onClick = {
                        val day = selectedDay
                        if(selectedTime != -1 && day != null){
                            onContinue(day, timeSlots[selectedTime].display)
                        }
                    }
                )
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(if(selectedTime != -1) Color(0xFFF5C800) else Color(0xFFF5C800).copy(0.5f)),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Continue",
                color = Color.Black.copy(if(selectedTime != -1) 1f else 0.5f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .customClickable { onClick() }
                .size(62.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = day.number.toString(),
                color = textColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            text = day.label,
            color = Color.White.copy(alpha = if (selected) 1f else 0.7f),
            fontSize = 11.sp,
            fontWeight = if(selected) FontWeight.Bold else FontWeight.Normal,
        )
    }

}


@Composable
fun TimeSlotBar(
    slot: TimeSlot,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gap: Dp = 6.dp
) {

    val bgColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color(0xFF222222),
        label = "timeBg"
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) Color.Black else Color.White,
        label = "timeText"
    )


    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(gap)
    ) {
        Box(
            modifier = Modifier
                .weight(slot.availability)
                .customClickable { onClick() }
                .fillMaxHeight()
                .clip(
                    RoundedCornerShape(
                        topStart = 50.dp,
                        bottomStart = 50.dp,
                        topEnd = 4.dp,
                        bottomEnd = 4.dp
                    )
                )
                .background(bgColor),
            contentAlignment = Alignment.CenterStart
        ){
            Text(
                text = slot.display,
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 20.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f - slot.availability)
                .fillMaxHeight()
                .clip(
                    RoundedCornerShape(
                        topStart = 4.dp,
                        bottomStart = 4.dp,
                        topEnd = 50.dp,
                        bottomEnd = 50.dp
                    )
                )
                .background(Color(0xB91C1C1C))
        )
    }
}