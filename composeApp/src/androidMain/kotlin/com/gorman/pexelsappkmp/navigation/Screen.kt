package com.gorman.pexelsappkmp.navigation

import com.gorman.pexelsappkmp.R

sealed class Screen(val route: String, val iconActive: Int, val iconPassive: Int, val title: Int) {
    object Home: Screen(
        "home_screen",
        R.drawable.home_button_active,
        R.drawable.home_button_inactive,
        R.string.title_home)
    object Bookmarks: Screen(
        "bookmarks_screen",
        R.drawable.bookmark_button_active,
        R.drawable.bookmark_button_inactive,
        R.string.title_bookmarks)
    object PhotoDetail: Screen(
        "photo_detail_screen",
        iconActive = 0,
        iconPassive = 0,
        title = 0)
    companion object {
        val bList = listOf(Home, Bookmarks)
    }
}