package com.gdproduct.rendana.ui

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

import com.gdproduct.rendana.ui.icons.Home as icHome
import com.gdproduct.rendana.ui.icons.ReceiptLong
import com.gdproduct.rendana.ui.icons.Add as icAdd
import com.gdproduct.rendana.ui.icons.Monitoring
import com.gdproduct.rendana.ui.icons.Person
import com.gdproduct.rendana.ui.screens.home.HomeScreen
import com.gdproduct.rendana.ui.screens.add.AddScreen
import com.gdproduct.rendana.ui.screens.history.HistoryScreen
import com.gdproduct.rendana.ui.screens.report.ReportScreen
import com.gdproduct.rendana.ui.screens.profile.ProfileScreen
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Screen("Home", "Beranda", icHome)
    data object History : Screen("History", "Riwayat", ReceiptLong)
	data object Add : Screen("Add", "Tambah", icAdd)
	data object Report : Screen("Report", "Laporan", Monitoring)
    data object Profile : Screen("Profile", "Profil", Person)
}

@Composable
fun Placeholder(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name)
    }
}

@Composable
fun RendanaNavigator(navController: NavHostController) {
    val colors = RendanaThemeColors
    val items = listOf(Screen.Home, Screen.History, Screen.Add, Screen.Report, Screen.Profile)
	
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(
                containerColor = colors.navigationBackground
            ) {
                items.forEach { item ->
                    if (item == Screen.Add) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    if (currentRoute != Screen.Add.route) {
                                        navController.navigate(Screen.Add.route) {
                                            popUpTo(
                                                navController.graph.findStartDestination().id
                                            ) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                modifier = Modifier.size(58.dp),
                                shape = CircleShape,
                                containerColor = colors.primary,
                                contentColor = colors.iconOnPrimary,
                                elevation = FloatingActionButtonDefaults.elevation(
                                    defaultElevation = 6.dp
                                )
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    } else {
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(
                                            navController.graph.findStartDestination().id
                                        ) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = colors.navigationSelected,
                                selectedTextColor = colors.navigationSelected,
                                unselectedIconColor = colors.navigationUnselected,
                                unselectedTextColor = colors.navigationUnselected,
                                indicatorColor = colors.navigationIndicator
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
	        
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(
                        durationMillis = 200,
                        delayMillis = 80
                    ),
                    initialOffsetX = { it }
                )
            },
            
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(
                        durationMillis = 200,
                        delayMillis = 80
                    ),
                    targetOffsetX = { -it / 4 }
                )
            },
	        
            popEnterTransition = {
                slideInHorizontally(
                    animationSpec = tween(200),
                    initialOffsetX = { -it / 4 }
                )
            },
	        
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(200),
                    targetOffsetX = { it }
                )
            }
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.History.route) { HistoryScreen() }
		    composable(Screen.Add.route) { AddScreen() }
	        composable(Screen.Report.route) { ReportScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}