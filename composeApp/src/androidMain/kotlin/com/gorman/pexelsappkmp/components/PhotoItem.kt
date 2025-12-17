package com.gorman.pexelsappkmp.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gorman.pexelsappkmp.domain.models.Photo

@Composable
fun PhotoItem(
    photo: Photo,
    onPhotoClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        AsyncImage(
            model = photo.src.medium,
            contentDescription = photo.url,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = {
                    val encodedUrl = Uri.encode(photo.src.large2x)
                    onPhotoClick(encodedUrl, photo.photographer)
                })
        )
    }
}