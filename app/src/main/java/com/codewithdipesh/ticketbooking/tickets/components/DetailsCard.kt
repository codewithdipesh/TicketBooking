package com.codewithdipesh.ticketbooking.tickets.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.ticketbooking.R

@Composable
fun DetailCard(
    modifier: Modifier = Modifier,
    name : String,
    dateTime : String,
    row : Int,
    seatString : String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(440.dp)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        Color(0xFF101010),
                        Color(0xFF101010),
                        Color(0xFF101010),
                        Color(0xFF101010),
                        Color(0xFF101010),
                        Color(0xFF101010),
                        Color(0xFF101010),
                        Color(0xFF101010),
                    )
                )
            )
            .padding(16.dp)
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = name,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = dateTime,
            color = Color.White.copy(0.5f),
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(50.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 46.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            InfoItem(title = "Screen", value = "2")

            VerticalDivider(
                modifier = Modifier.height(16.dp),
                color = Color.White.copy(0.2f),
                thickness = 0.5.dp
            )

            InfoItem(title = "Row", value = row.toString())

            VerticalDivider(
                modifier = Modifier.height(16.dp),
                color = Color.White.copy(0.2f),
                thickness = 0.5.dp
            )

            InfoItem(title = "Seats", value = seatString)
        }
        Spacer(Modifier.height(25.dp))
        Box(
            modifier.fillMaxWidth()
                .padding(horizontal = 70.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Manage RSVP",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier
                .offset(y=(-10).dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(110.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White.copy(0.1f))
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Scan code at the counter to get a physical ticket",
                color = Color.White.copy(0.7f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.width(180.dp)
            )
            Icon(
                painter = painterResource(R.drawable.qr_code),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(70.dp)
            )

        }

    }
}


@Composable
fun InfoItem(
    title: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            color = Color.White.copy(0.5f),
            fontSize = 13.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}