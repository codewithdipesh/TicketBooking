package com.codewithdipesh.ticketbooking.booking.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.codewithdipesh.ticketbooking.R
import com.codewithdipesh.ticketbooking.home.elements.customClickable
import com.codewithdipesh.ticketbooking.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutBottomSheet(
    movie : Movie,
    dateTime:String,
    seats : List<Pair<Int,Int>>,
    onDismiss : () -> Unit,
    onContinue : () -> Unit,
    sheetState : SheetState,
    modifier: Modifier = Modifier
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier.fillMaxWidth(),
        containerColor = Color(0xFF1E1E1E)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text= "Checkout",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Back",
                        tint = Color.White.copy(0.5f)
                    )
                }
            }

            Spacer(Modifier.height(36.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = rememberAsyncImagePainter(movie.posterUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = 110.dp, height = 80.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))

                // Movie Details
                Column(verticalArrangement = Arrangement.Center){
                    Image(
                        painter = rememberAsyncImagePainter(movie.titleUrl),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        alpha = 0.7f
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${movie.releaseYear} · ${movie.genres.first()} · ${movie.durationMinutes} min",
                        color = Color.White.copy(0.7f),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))
                }
            }

            Spacer(Modifier.height(40.dp))


            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(R.drawable.ticket),
                    contentDescription = null,
                    tint = Color.White.copy(0.7f),
                    modifier = Modifier.size(30.dp)
                )
                Column {
                    Text(
                        text= dateTime,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text= "Screen 2 • Row ${seats.first().first + 1} • ${seats.size} Seats",
                        color = Color.White.copy(0.7f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            HorizontalDivider(
                thickness = 0.4.dp,
                color = Color.White.copy(0.4f),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(30.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text= "Pay With",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text= "•••• 4558",
                        color = Color.White.copy(0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(Modifier.width(6.dp))
                    Image(
                        painter = painterResource(R.drawable.ma_symbol),
                        contentDescription = null,
                        modifier= Modifier.height(24.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.White.copy(0.7f)
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            //total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text= "Total",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text= "$${movie.ticketPrice * seats.size}",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(36.dp))


            Box(
                modifier = Modifier
                    .customClickable(onClick = onContinue)
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFFF5C800)),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Pay",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(Modifier.height(12.dp))
        }
    }

}