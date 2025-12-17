package com.gorman.pexelsappkmp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gorman.pexelsappkmp.R
import com.gorman.pexelsappkmp.domain.models.Bookmark
import com.gorman.pexelsappkmp.domain.viewmodels.BookmarksViewModel
import com.gorman.pexelsappkmp.fonts.mulishFont

@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel,
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToHome: () -> Unit
) {
    val bookmarks by viewModel.bookmarks.collectAsState()
    val progress by viewModel.bookmarksProgress.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadBookmarks()
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.title_bookmarks),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 8.dp),
            textAlign = TextAlign.Center,
            fontFamily = mulishFont(),
            fontSize = 18.sp,
            color = colorResource(id = R.color.collectionsText)
        )
        if (progress in 1..99) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(R.color.choose),
                trackColor = colorResource(R.color.colorBackground)
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            if (bookmarks.isEmpty() && progress == 100) {
                EmptyBookmarksView(onExploreClick = onNavigateToHome)
            } else {
                BookmarksGrid(
                    bookmarks = bookmarks,
                    onItemClick = onNavigateToDetails
                )
            }
        }
    }
}

@Composable
fun BookmarksGrid(
    bookmarks: List<Bookmark>,
    onItemClick: (Int) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 8.dp),
        contentPadding = PaddingValues(bottom = 72.dp)
    ) {
        items(bookmarks) { bookmark ->
            BookmarkItem(
                bookmark = bookmark,
                onClick = { bookmark.id?.let { onItemClick(it) } }
            )
        }
    }
}

@Composable
fun EmptyBookmarksView(onExploreClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.NoSavedImages),
            fontFamily = mulishFont(),
            fontSize = 18.sp,
            color = colorResource(id = R.color.warningText)
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onExploreClick) {
            Text(
                text = "Explore",
                fontFamily = mulishFont(),
                fontSize = 18.sp,
                color = colorResource(id = R.color.choose)
            )
        }
    }
}

@Composable
fun BookmarkItem(bookmark: Bookmark, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        AsyncImage(
            model = bookmark.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }
}