package com.gorman.pexelsappkmp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gorman.pexelsappkmp.R
import com.gorman.pexelsappkmp.domain.PhotoLoadState
import com.gorman.pexelsappkmp.domain.models.Collection
import com.gorman.pexelsappkmp.domain.models.Photo
import com.gorman.pexelsappkmp.domain.viewmodels.HomeViewModel
import com.gorman.pexelsappkmp.fonts.mulishFont

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            SearchBar(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 45.dp),
                onSearch = { query ->
                    viewModel.onSearch(query)
                }
            )

            CollectionsRow(
                collections = uiState.collections,
                selected = uiState.selectedCollectionTitle,
                onSelect = viewModel::onCollectionSelected
            )

            Box(modifier = Modifier.weight(1f)) {

                PhotosGrid(
                    photos = uiState.photos,
                    onLoadMore = viewModel::loadMore
                )

                if (uiState.loadState is PhotoLoadState.Loading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        if (uiState.noResults) {
            NoResults()
        }

        if (uiState.loadState is PhotoLoadState.Error) {
            ErrorState(onRetry = { viewModel.onSearch() })
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String?) -> Unit
) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp),
        placeholder = {
            Text(stringResource(R.string.searchDefaultText))
        },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = {
                text = ""
                onSearch(null)
            }) {
                Icon(
                    painterResource(R.drawable.close_icon),
                    contentDescription = null
                )
            }
        },
        leadingIcon = {
            Icon(
                painterResource(R.drawable.search_icon),
                contentDescription = null
            )
        },
        keyboardActions = KeyboardActions(
            onSearch = { onSearch(text) }
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
fun CollectionsRow(
    collections: List<Collection>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .padding(top = 8.dp)
            .height(47.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(collections) { collection ->
            CollectionChip(
                title = collection.title,
                selected = collection.title == selected,
                onClick = { onSelect(collection.title) }
            )
        }
    }
}

@Composable
fun CollectionChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.main),
            contentColor = colorResource(R.color.collectionsText)
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontFamily = mulishFont()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotosGrid(
    photos: List<Photo>,
    onLoadMore: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 72.dp)
    ) {
        items(photos.size) { index ->
            PhotoItem(photo = photos[index])

            if (index == photos.lastIndex) {
                LaunchedEffect(Unit) {
                    onLoadMore()
                }
            }
        }
    }
}

@Composable
fun PhotoItem(
    photo: Photo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.75f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        AsyncImage(
            model = photo.src.medium,
            contentDescription = photo.url,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}


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
