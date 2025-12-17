package com.gorman.pexelsappkmp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gorman.pexelsappkmp.R
import com.gorman.pexelsappkmp.fonts.mulishFont

@Composable
fun ErrorState(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.no_network_icon),
            contentDescription = null,
            modifier = Modifier.size(156.dp, 121.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onRetry) {
            Text(
                text = "Try again",
                fontSize = 18.sp,
                fontFamily = mulishFont(),
                color = colorResource(R.color.choose)
            )
        }
    }
}