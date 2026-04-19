package com.codewithdipesh.ticketbooking.booking.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.codewithdipesh.ticketbooking.R

@Composable
fun AmountChooser(modifier: Modifier = Modifier) {

    var amount by rememberSaveable { mutableStateOf(1) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ){
        Text(
            text = "Who's going?",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = "Select tickets amount",
            color = Color.White.copy(0.7f),
            fontSize = 12.sp
        )
        Spacer(Modifier.height(70.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            AnimatedVisibility(
                visible = amount >= 1,
                enter = scaleIn(),
                exit = scaleOut(),
                modifier = Modifier
                    .zIndex(2f)
                    .offset(x = 10.dp) //fixing the smily
                    .offset(x = if (amount > 1) (12).dp else 0.dp)
            ) {
                SmileLottie(
                    resId = R.raw.smile_1,
                    size = 120
                )
            }
            AnimatedVisibility(
                visible = amount >= 2,
                enter = scaleIn(),
                exit = scaleOut(),
                modifier = Modifier.zIndex(3f)
            ) {
                SmileLottie(
                    resId = R.raw.smile_2
                )
            }
            AnimatedVisibility(
                visible = amount >= 3,
                enter = scaleIn(),
                exit = scaleOut(),
                modifier = Modifier
                    .offset(x = -14.dp )
                    .zIndex(1f)
            ) {
                SmileLottie(
                    resId = R.raw.smile_3
                )
            }
        }

        Spacer(Modifier.height(70.dp))
        TicketCounter(
            amount = amount,
            onAmountChange = { amount = it },
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
private fun SmileLottie(resId: Int, modifier: Modifier = Modifier,size :Int = 100) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = 1.2f
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier.size(size.dp)
    )
}
