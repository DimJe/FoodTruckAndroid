package com.example.foodtruck

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodtruck.navigation.Screen
import com.example.foodtruck.ui.theme.FoodTruckTheme
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            FoodTruckTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController = navController) }) {
                    Navigation(navController = navController,it)
                }
            }
        }
    }
}
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapScreen(){
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoom = 30.0, minZoom = 18.0)
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isLocationButtonEnabled = true)
        )
    }
    val seoul = LatLng(37.532600, 127.024612)
    var cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        position = CameraPosition(seoul, 11.0)
    }
    val position by remember {
        derivedStateOf {
            cameraPositionState.position
        }
    }
    var isMovingByAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(cameraPositionState.isMoving) {
        if(!cameraPositionState.isMoving){
            Log.e("test","${position.target}")
        }
    }
    NaverMap(
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = mapUiSettings,
        modifier = Modifier.wrapContentSize(),
    ){
        //Marker
        Marker(
            state = MarkerState(position = cameraPositionState.position.target),
            captionText = "center",
            width = 25.dp,
            height = 25.dp
            )
    }
}
@Composable
fun BottomNavigationBar(navController: NavController){
    val items = listOf(
        Screen.Home,
        Screen.Add,
        Screen.Settings
    )

    NavigationBar {
        val currentRoute = currentRoute(navController)
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
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
@Composable
fun Navigation(navController: NavHostController,innerPadding: PaddingValues) {
    NavHost(navController, startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding)) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Add.route) { AddScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}
@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
@Composable
fun HomeScreen() {
    MapScreen()
}
@Composable
fun AddScreen() {
    Text(text = "Add")
}
@Composable
fun SettingsScreen() {
    Text(text = "Settings")
}