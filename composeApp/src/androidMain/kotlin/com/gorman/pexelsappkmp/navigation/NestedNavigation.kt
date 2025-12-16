package com.gorman.pexelsappkmp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gorman.pexelsappkmp.R

@Composable
fun NestedNavigation() {
    val navController = rememberNavController()
    Box(modifier = Modifier.fillMaxSize()
        .background(colorResource(R.color.colorBackground))) {
        NavHost(navController = navController, startDestination = "setup") {
            composable("setup") {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(colorResource(R.color.colorBackground))
                )
                navController.navigate("main") {
                    popUpTo(0) { inclusive = true }
                }
            }
            composable("main") {
                BottomNavigation(navController)
            }
            composable(Screen.PhotoDetail.route) {

            }
        }
    }
}