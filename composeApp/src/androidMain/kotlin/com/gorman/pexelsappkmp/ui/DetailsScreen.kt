package com.gorman.pexelsappkmp.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.gorman.pexelsappkmp.R
import com.gorman.pexelsappkmp.domain.states.DetailsUiState
import com.gorman.pexelsappkmp.domain.viewmodels.DetailsViewModel
import com.gorman.pexelsappkmp.fonts.mulishFont
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailsScreen(
    passedUrl: String? = null,
    passedName: String? = null,
    passedId: Int? = null,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: DetailsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val bookmarkFromDb by viewModel.bookmark.collectAsState()
    var currentUrl by remember { mutableStateOf(passedUrl) }
    var currentName by remember { mutableStateOf(passedName) }

    LaunchedEffect(Unit) {
        println("DEBUG: Details opened with id=$passedId, url=$passedUrl, name=$passedName")
    }

    LaunchedEffect(Unit) {
        if (passedId != null) {
            viewModel.findBookmarkById(passedId)
        } else if (passedUrl != null) {
            viewModel.searchInDBOnce(passedUrl)
        }
    }

    LaunchedEffect(bookmarkFromDb) {
        bookmarkFromDb?.let { bookmark ->
            currentUrl = bookmark.imageUrl
            currentName = bookmark.phName
            bookmark.imageUrl?.let { viewModel.searchInDBOnce(it) }
        }
    }

    val isBookmarked = (uiState as? DetailsUiState.Success)?.isBookmark == true

    Box(modifier = Modifier
        .background(colorResource(R.color.colorBackground))
        .fillMaxSize()
        .systemBarsPadding()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onBackClick,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .size(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.colorBackground)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                Text(
                    text = currentName ?: "Loading...",
                    fontSize = 18.sp,
                    fontFamily = mulishFont(),
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.collectionsText),
                    modifier = Modifier.wrapContentWidth(),
                    maxLines = 1
                )
                Button(
                    onClick = {},
                    modifier = Modifier
                        .size(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent
                    )
                ) {}
            }
            Spacer(modifier = Modifier.weight(1f))
            if (currentUrl != null) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Detail Image",
                    modifier = Modifier
                        .padding(horizontal = 25.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Fit,
                    loading = {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(colorResource(R.color.main)),
                            contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = colorResource(R.color.choose))
                        }
                    },
                    error = {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(colorResource(R.color.main)),
                            contentAlignment = Alignment.Center) {
                            Text("Error loading image",
                                color = Color.Red,
                                fontFamily = mulishFont(),
                                fontSize = 14.sp)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, end = 25.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        currentUrl?.let { downloadImage(context, it) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.main)
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .height(56.dp)
                        .background(Color(0xFFF3F5F9), CircleShape),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(R.drawable.ic_download),
                            contentDescription = null,
                            modifier = Modifier.clip(CircleShape).size(56.dp)
                        )
                        Spacer(modifier = Modifier.width(32.dp))
                        Text(
                            text = stringResource(R.string.download),
                            fontFamily = mulishFont(),
                            fontSize = 14.sp,
                            lineHeight = 15.sp,
                            maxLines = 1,
                            color = colorResource(R.color.collectionsText)
                        )
                        Spacer(modifier = Modifier.width(32.dp))
                    }
                }
                Button(
                    onClick = {
                        if (currentUrl != null && currentName != null) {
                            if (isBookmarked) {
                                viewModel.deleteByUrl(currentUrl!!)
                                Toast.makeText(context, R.string.BookmarkDelete, Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.addBookmark(currentUrl!!, currentName!!)
                                Toast.makeText(context, R.string.BookmarkAdd, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .size(56.dp)
                ) {
                    Image(
                        painter = if (isBookmarked)
                            painterResource(R.drawable.bookmark_button_active_detail)
                        else painterResource(R.drawable.bookmark_button_inactive_detail),
                        contentDescription = "Like",
                        modifier = Modifier.size(56.dp)
                    )
                }
            }
        }
    }
}

fun downloadImage(context: android.content.Context, url: String) {
    val uri = url.toUri()
    val request = android.app.DownloadManager.Request(uri)
        .setTitle(context.getString(R.string.DownloadStart))
        .setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(
            android.os.Environment.DIRECTORY_PICTURES,
            "image_${System.currentTimeMillis()}.jpg"
        )
    val manager = context.getSystemService(android.content.Context.DOWNLOAD_SERVICE) as android.app.DownloadManager
    manager.enqueue(request)
    Toast.makeText(context, R.string.DownloadStart, Toast.LENGTH_SHORT).show()
}
