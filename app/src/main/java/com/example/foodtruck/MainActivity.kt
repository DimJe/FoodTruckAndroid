package com.example.foodtruck

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodtruck.ui.theme.FoodTruckTheme
import com.example.foodtruck.navigation.Screen
import com.example.foodtruck.ui.screens.add.AddDetailScreen
import com.example.foodtruck.ui.screens.add.AddScreen
import com.example.foodtruck.ui.screens.home.HomeScreen
import com.example.foodtruck.ui.screens.setting.SettingsScreen
import com.example.foodtruck.ui.theme.Orange
import com.example.foodtruck.viewmodel.TruckViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            FoodTruckTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute(navController) != "AddDetail") {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) {
                    var hasLocationPermission by remember { mutableStateOf(false) }
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission()
                    ) { isGranted: Boolean ->
                        if (isGranted) {
                            hasLocationPermission = true
                        } else {
                            // 권한이 거부되었을 때 처리
                        }
                    }
                    // 권한이 없는 경우 권한 요청
                    if (!hasLocationPermission) {
                        SideEffect {
                            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    }
                    // 권한 상태에 따라 화면 표시
                    if (hasLocationPermission) {
                        // 위치 정보 사용하여 지도 표시 등
                        Navigation(navController,it)
                    } else {
                        // 권한 요청 중 또는 권한 거부 시 화면 표시
                    }
                }
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController, innerPadding: PaddingValues) {
    val viewModel: TruckViewModel = hiltViewModel()
    NavHost(navController, startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding)) {
        composable(Screen.Home.route) { HomeScreen(viewModel) }
        composable(Screen.Add.route) {
            AddScreen(viewModel){
                navController.navigate("AddDetail") {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
        composable(Screen.Settings.route) { SettingsScreen(viewModel) }
        composable("AddDetail"){
            AddDetailScreen(viewmodel = viewModel){
                navController.navigate(Screen.Add.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }
}
@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
@Composable
fun BottomNavigationBar(navController: NavController){
    val items = listOf(
        Screen.Home,
        Screen.Add,
        Screen.Settings
    )

    NavigationBar(
        modifier = Modifier
            .height(60.dp),
        containerColor = Color.White,
    ) {
        val currentRoute = currentRoute(navController)
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        items.forEach { screen ->
            NavigationBarItem(
                colors = NavigationBarItemColors(
                    selectedIconColor = Orange, Color.White,Color.White,Color.Gray,Color.White,Color.White,Color.White
                ),
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                //label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}
