package com.gorman.pexelsappkmp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gorman.pexelsappkmp.R
import com.gorman.pexelsappkmp.ui.DetailsScreen

@Composable
fun NestedNavigation() {
    val navController = rememberNavController()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.colorBackground))
    ) {
        NavHost(navController = navController, startDestination = "setup") {
            composable("setup") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(R.color.colorBackground))
                )
                navController.navigate("main") {
                    popUpTo(0) { inclusive = true }
                }
            }
            composable("main") {
                BottomNavigation(navController)
            }
            composable(
                route = "${Screen.PhotoDetail.route}?id={id}&url={url}&name={name}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType; defaultValue = -1 },
                    navArgument("url") {
                        type = NavType.StringType; nullable = true; defaultValue = null
                    },
                    navArgument("name") {
                        type = NavType.StringType; nullable = true; defaultValue = null
                    }
                )) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id").takeIf { it != -1 }
                val url = backStackEntry.arguments?.getString("url")
                val name = backStackEntry.arguments?.getString("name")

                DetailsScreen(
                    passedId = id,
                    passedUrl = url,
                    passedName = name,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}