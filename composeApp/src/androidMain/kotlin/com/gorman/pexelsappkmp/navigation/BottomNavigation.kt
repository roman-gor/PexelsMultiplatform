package com.gorman.pexelsappkmp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gorman.pexelsappkmp.R
import com.gorman.pexelsappkmp.components.BottomNavigationBar
import com.gorman.pexelsappkmp.domain.viewmodels.BookmarksViewModel
import com.gorman.pexelsappkmp.domain.viewmodels.HomeViewModel
import com.gorman.pexelsappkmp.ui.BookmarksScreen
import com.gorman.pexelsappkmp.ui.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BottomNavigation(
    nestedNavController: NavController
) {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = koinViewModel()
    val bookmarksViewModel: BookmarksViewModel = koinViewModel()
    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(colorResource(R.color.colorBackground)),
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = Screen.bList
            )
        }
    ) { innerPaddings->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.background(colorResource(R.color.colorBackground))
                .padding(innerPaddings)
        ) {
            Screen.bList.forEach { item->
                composable(item.route) {
                    when(item.route) {
                        Screen.Home.route -> HomeScreen(
                            viewModel = homeViewModel,
                            onPhotoClick = { url, name->
                                nestedNavController.navigate("${Screen.PhotoDetail.route}?url=$url&name=$name")
                            }
                        )
                        Screen.Bookmarks.route -> BookmarksScreen(
                            viewModel = bookmarksViewModel,
                            onNavigateToDetails = { id ->
                                nestedNavController.navigate("${Screen.PhotoDetail.route}?id=$id")
                            },
                            onNavigateToHome = {
                                navController.navigate(Screen.Home.route)
                            }
                        )
                    }
                }
            }
        }
    }
}