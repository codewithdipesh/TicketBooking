package com.codewithdipesh.ticketbooking.booking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.ticketbooking.R
import com.codewithdipesh.ticketbooking.booking.seatBooking.SeatStatus
import com.codewithdipesh.ticketbooking.booking.seatBooking.seatArrangements
import com.codewithdipesh.ticketbooking.home.elements.customClickable
import kotlin.math.abs

fun findContinuousSeats(
    rowSeats: List<SeatStatus>,
    clickedIndex: Int,
    count: Int
): List<Int>? {
    if (count <= 0) return null
    if (rowSeats.getOrNull(clickedIndex) != SeatStatus.A) return null

    // Try every block that contains clickedIndex, preferring the clicked seat in the middle.
    // For count=3, leftOffset order is [1, 0, 2] → centered, then left-end, then right-end.
    val offsets = (0 until count).sortedBy { abs(it - count / 2) }
    for (leftOffset in offsets) {
        val start = clickedIndex - leftOffset
        val end = start + count - 1
        if (start < 0 || end >= rowSeats.size) continue
        if ((start..end).all { rowSeats[it] == SeatStatus.A }) {
            return (start..end).toList()
        }
    }
    return null
}

@Composable
fun SeatHall(
    seatCount: Int,
    modifier: Modifier = Modifier,
    onSelectionChange: (List<Pair<Int, Int>>) -> Unit = {}
) {
    val rows = remember {
        seatArrangements.map { it.toMutableStateList() }.toMutableStateList()
    }
    val positions = remember { mutableStateMapOf<Pair<Int, Int>, Offset>() }
    val sizes = remember { mutableStateMapOf<Pair<Int, Int>, IntSize>() }
    var selected by remember { mutableStateOf<List<Pair<Int, Int>>>(emptyList()) }
    var hallRoot by remember { mutableStateOf(Offset.Zero) }
    val tooltipGapPx = with(LocalDensity.current) { 40.dp.toPx() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { hallRoot = it.positionInRoot() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            rows.forEachIndexed { rowIdx, row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEachIndexed { colIdx, seat ->
                        val color = when (seat) {
                            SeatStatus.B -> Color.Gray.copy(0.2f)
                            SeatStatus.A -> Color.Gray
                            SeatStatus.S -> Color(0xFFF5C800)
                            SeatStatus.NA -> Color.Transparent
                        }
                        if (seat != SeatStatus.NA) {
                            Icon(
                                painter = painterResource(R.drawable.sofa),
                                contentDescription = "seat",
                                tint = color,
                                modifier = Modifier
                                    .size(26.dp)
                                    .onGloballyPositioned { c ->
                                        positions[rowIdx to colIdx] = c.positionInRoot()
                                        sizes[rowIdx to colIdx] = c.size
                                    }
                                    .customClickable {
                                        if (seat != SeatStatus.A) return@customClickable
                                        val block = findContinuousSeats(
                                            rowSeats = row.toList(),
                                            clickedIndex = colIdx,
                                            count = seatCount
                                        ) ?: return@customClickable

                                        // Clear previous selection back to available.
                                        selected.forEach { (r, c) ->
                                            if (rows[r][c] == SeatStatus.S) rows[r][c] = SeatStatus.A
                                        }
                                        // Apply new selection.
                                        block.forEach { c -> rows[rowIdx][c] = SeatStatus.S }
                                        selected = block.map { rowIdx to it }
                                        onSelectionChange(selected)
                                    }
                            )
                        } else {
                            Spacer(Modifier.width(26.dp))
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }

        // --- Tooltip over the selected block ---
        if (selected.isNotEmpty()) {
            val selPositions = selected.mapNotNull { positions[it] }
            val seatSize = selected.firstNotNullOfOrNull { sizes[it] }
            if (selPositions.isNotEmpty() && seatSize != null) {
                // Average x-center of the selected block, top-most y.
                val avgCenterX = selPositions.map { it.x }.average().toFloat() + seatSize.width / 2f
                val minY = selPositions.minOf { it.y }

                var tooltipSize by remember { mutableStateOf(IntSize.Zero) }
                Box(
                    modifier = Modifier
                        .onSizeChanged { tooltipSize = it }
                        .offset {
                            IntOffset(
                                x = (avgCenterX - hallRoot.x).toInt() - tooltipSize.width / 2,
                                y = (minY - hallRoot.y - tooltipGapPx).toInt()
                            )
                        }
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xFF6E6E6E))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Row ${selected.first().first + 1} • ${selected.size} seats",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
