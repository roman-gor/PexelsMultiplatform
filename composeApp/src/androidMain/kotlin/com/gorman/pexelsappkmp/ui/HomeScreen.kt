package com.gorman.pexelsappkmp.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gorman.pexelsappkmp.R
import com.gorman.pexelsappkmp.components.ErrorState
import com.gorman.pexelsappkmp.components.NoResults
import com.gorman.pexelsappkmp.components.PhotoItem
import com.gorman.pexelsappkmp.domain.states.PhotoLoadState
import com.gorman.pexelsappkmp.domain.models.Collection
import com.gorman.pexelsappkmp.domain.models.ErrorID
import com.gorman.pexelsappkmp.domain.models.Photo
import com.gorman.pexelsappkmp.domain.viewmodels.HomeViewModel
import com.gorman.pexelsappkmp.fonts.mulishFont

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onPhotoClick: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(R.color.colorBackground))
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SearchBar(
                text = text,
                onTextChange = { text = it },
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                onSearch = { query ->
                    viewModel.onSearch(query)
                }
            )
            CollectionsRow(
                collections = uiState.collections,
                selected = uiState.selectedCollectionTitle,
                onSelect = {
                    viewModel.onCollectionSelected(it)
                    text = it }
            )
            Box(modifier = Modifier.weight(1f)) {
                PhotosGrid(
                    photos = uiState.photos,
                    onPhotoClick = onPhotoClick,
                    onLoadMore = viewModel::loadMore
                )
                if (uiState.loadState is PhotoLoadState.Loading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = colorResource(R.color.choose),
                        trackColor = colorResource(R.color.colorBackground)
                    )
                }
            }
        }
        if (uiState.noResults) {
            NoResults()
        }
        if (uiState.loadState is PhotoLoadState.Error) {
            val error = uiState.loadState as PhotoLoadState.Error
            val errorMessage = errorsMapping(error.errorId)
            LaunchedEffect(Unit) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
            ErrorState(onRetry = { viewModel.onSearch() })
        }
    }
}

@Composable
fun errorsMapping(error: ErrorID): String {
    return when (error) {
        ErrorID.NETWORK_UNAVAILABLE -> stringResource(R.string.NetworkError)
        ErrorID.REQUEST_TIMEOUT -> stringResource(R.string.TooManyRequests)
        ErrorID.CLIENT_ERROR_404 -> stringResource(R.string.error_404)
        ErrorID.CLIENT_ERROR_401 -> stringResource(R.string.authorize_error)
        ErrorID.SERVER_ERROR_5XX -> stringResource(R.string.ServerError)
        ErrorID.DATA_CORRUPTION -> stringResource(R.string.data_error)
        ErrorID.UNKNOWN_ERROR -> stringResource(R.string.UnknownError)
    }
}

@Composable
fun SearchBar(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: (String?) -> Unit
) {

    OutlinedTextField(
        value = text,
        onValueChange = {
            onSearch(it)
            onTextChange(it) },
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp),
        placeholder = {
            Text(
                text = stringResource(R.string.searchDefaultText),
                fontFamily = mulishFont(),
                color = colorResource(R.color.searchHintColor))
        },
        textStyle = TextStyle(
            fontFamily = mulishFont()
        ),
        singleLine = true,
        trailingIcon = {
            if (text.isNotBlank()) {
                IconButton(onClick = {
                    onTextChange("")
                    onSearch(null)
                }) {
                    Icon(
                        painterResource(R.drawable.close_icon),
                        tint = colorResource(R.color.searchHintColor),
                        contentDescription = null
                    )
                }
            }
        },
        leadingIcon = {
            Icon(
                painterResource(R.drawable.search_icon),
                tint = colorResource(R.color.choose),
                contentDescription = null
            )
        },
        keyboardActions = KeyboardActions(
            onSearch = { onSearch(text) }
        ),
        shape = RoundedCornerShape(36.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorResource(R.color.main),
            unfocusedContainerColor = colorResource(R.color.main),
            focusedTextColor = colorResource(R.color.collectionsText),
            unfocusedTextColor = colorResource(R.color.searchHintColor),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent
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
            containerColor = if (selected) colorResource(R.color.choose)
                                else colorResource(R.color.main),
            contentColor = if (selected) colorResource(R.color.white)
                            else colorResource(R.color.collectionsText)
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
    onPhotoClick: (String, String) -> Unit,
    onLoadMore: () -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 8.dp),
        contentPadding = PaddingValues(bottom = 72.dp)
    ) {
        items(photos.size) { index ->
            PhotoItem(
                photo = photos[index],
                onPhotoClick = onPhotoClick)
            if (index == photos.lastIndex) {
                LaunchedEffect(Unit) {
                    onLoadMore()
                }
            }
        }
    }
}