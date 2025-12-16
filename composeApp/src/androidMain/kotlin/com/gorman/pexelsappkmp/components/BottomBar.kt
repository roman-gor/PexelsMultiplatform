package com.gorman.pexelsappkmp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gorman.pexelsappkmp.R
import com.gorman.pexelsappkmp.navigation.Screen

@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<Screen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationItem(
        items = items,
        currentRoute = currentRoute,
        navController = navController
    )
}

@Composable
fun NavigationItem(
    items: List<Screen>,
    currentRoute: String?,
    navController: NavController
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = colorResource(R.color.colorBackground)
    ) {
        items.forEach { item->
            NavigationBarItem(
                icon = {
                    if (currentRoute == item.route)
                        Icon(painter = painterResource(item.iconActive),
                            modifier = Modifier.scale(1.2f),
                            contentDescription = stringResource(item.title))
                    else
                        Icon(painter = painterResource(item.iconPassive),
                            modifier = Modifier.scale(1.2f),
                            contentDescription = stringResource(item.title)) },
                selected = currentRoute == item.route,
                colors = NavigationBarItemColors(
                    selectedIconColor = colorResource(R.color.choose),
                    selectedTextColor = colorResource(R.color.choose),
                    selectedIndicatorColor = Color.Transparent,
                    unselectedIconColor = colorResource(R.color.searchHintColor),
                    unselectedTextColor = colorResource(R.color.searchHintColor),
                    disabledIconColor = colorResource(R.color.searchHintColor),
                    disabledTextColor = colorResource(R.color.searchHintColor)
                ),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}