package com.gorman.pexelsappkmp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gorman.pexelsappkmp.R
import com.gorman.pexelsappkmp.fonts.mulishFont

@Composable
fun NoResults() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.ResultError),
            color = colorResource(R.color.warningText),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = mulishFont()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = {}) {
            Text(
                text = "Explore",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = mulishFont(),
                color = colorResource(R.color.choose)
            )
        }
    }
}