/*
Copyright 2022 Savvas Dalkitsis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.savvasdalkitsis.uhuruphotos.home.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.savvasdalkitsis.uhuruphotos.feed.view.state.FeedDisplay
import com.savvasdalkitsis.uhuruphotos.home.navigation.NavigationStyle.BOTTOM_BAR
import com.savvasdalkitsis.uhuruphotos.home.navigation.NavigationStyle.NAVIGATION_RAIL
import com.savvasdalkitsis.uhuruphotos.strings.R
import com.savvasdalkitsis.uhuruphotos.ui.window.LocalWindowSize

@Composable
fun homeNavigationStyle() = when (LocalWindowSize.current.widthSizeClass) {
    Compact -> BOTTOM_BAR
    else -> NAVIGATION_RAIL
}

@Composable
fun HomeNavigationBar(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    feedDisplay: FeedDisplay,
    navController: NavHostController,
    feedNavigationName: String,
    searchNavigationName: String,
    onReselected: () -> Unit = {},
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    when (homeNavigationStyle()) {
        BOTTOM_BAR -> {
            BottomNavigation(
                elevation = 0.dp,
                backgroundColor = Color.Transparent
            ) {
                Items(currentDestination, navController, feedDisplay, feedNavigationName, searchNavigationName, onReselected, rowScope = this)
            }
        }
        NAVIGATION_RAIL -> NavigationRail(
            modifier = Modifier.padding(top = contentPadding.calculateTopPadding()),
            elevation = 0.dp,
            backgroundColor = Color.Transparent,
        ) {
            Items(currentDestination, navController, feedDisplay, feedNavigationName, searchNavigationName, onReselected)
        }
    }
}

@Composable
private fun Items(
    currentDestination: NavDestination?,
    navController: NavHostController,
    feedDisplay: FeedDisplay,
    feedNavigationName: String,
    searchNavigationName: String,
    onReselected: () -> Unit,
    rowScope: RowScope? = null,
) {
    NavItem(
        currentDestination, navController,
        label = R.string.feed,
        routeName = feedNavigationName,
        painterResource(id = feedDisplay.iconResource),
        onReselected,
        rowScope,
    )
    NavItem(
        currentDestination, navController,
        label = R.string.search,
        routeName = searchNavigationName,
        icon = rememberVectorPainter(Icons.Filled.Search),
        rowScope = rowScope,
        onReselected = onReselected,
    )
}

@Composable
private fun NavItem(
    currentDestination: NavDestination?,
    navController: NavHostController,
    @StringRes label: Int,
    routeName: String,
    icon: Painter,
    onReselected: () -> Unit,
    rowScope: RowScope? = null,
) {
    when (homeNavigationStyle()) {
        BOTTOM_BAR -> BottomNavItem(
            rowScope = rowScope!!,
            currentDestination ,
            navController,
            label,
            routeName,
            icon,
            onReselected,
        )
        NAVIGATION_RAIL -> NavRailNavItem(
            currentDestination,
            navController,
            label,
            routeName,
            icon,
            onReselected,
        )
    }

}

@Composable
private fun BottomNavItem(
    rowScope: RowScope,
    currentDestination: NavDestination?,
    navController: NavHostController,
    @StringRes label: Int,
    routeName: String,
    icon: Painter,
    onReselected: () -> Unit,
) {
    with(rowScope) {
        BottomNavigationItem(
            icon = { Icon(icon, contentDescription = null) },
            label = { Text(stringResource(label)) },
            selected = isSelected(currentDestination, routeName),
            onClick = selectNavigationItem(currentDestination, routeName, navController, onReselected)
        )
    }
}

@Composable
private fun NavRailNavItem(
    currentDestination: NavDestination?,
    navController: NavHostController,
    @StringRes label: Int,
    routeName: String,
    icon: Painter,
    onReselected: () -> Unit,
) {
    NavigationRailItem(
        selectedContentColor = LocalContentColor.current,
        icon = { Icon(icon, contentDescription = null) },
        label = { Text(stringResource(label)) },
        selected = isSelected(currentDestination, routeName),
        onClick = selectNavigationItem(currentDestination, routeName, navController, onReselected)
    )
}

@Composable
private fun isSelected(
    currentDestination: NavDestination?,
    routeName: String
) = currentDestination?.hierarchy?.any { it.route == routeName } == true

@Composable
private fun selectNavigationItem(
    currentDestination: NavDestination?,
    routeName: String,
    navController: NavHostController,
    onReselected: () -> Unit,
): () -> Unit = {
    if (currentDestination?.route != routeName) {
        navController.navigate(routeName) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    } else {
        onReselected()
    }
}