package com.example.foodtruck

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationSource
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.CameraUpdateReason
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapEffect
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            FoodTruckTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
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
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapScreen(){
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoom = 30.0, minZoom = 18.0, locationTrackingMode = LocationTrackingMode.None )
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isLocationButtonEnabled = true)
        )
    }
    var cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        //position = CameraPosition(seoul, 11.0)
    }
    var locationSource = rememberFusedLocationSource()

    locationSource.apply {
        activate {
            Log.e("test","camera move")
            cameraPositionState.position = CameraPosition(LatLng(it!!), 18.0)
            deactivate()
        }
    }
    val position by remember {
        derivedStateOf {
            cameraPositionState.position
        }
    }
    LaunchedEffect(cameraPositionState.isMoving) {
        if(!cameraPositionState.isMoving){
            Log.e("test","${position.target}")
        }
    }
    NaverMap(
        locationSource = locationSource,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = mapUiSettings,
        modifier = Modifier.wrapContentSize(),
//        onLocationChange = {
//            cameraPositionState.move(CameraUpdate.scrollTo(LatLng(it)))
//
//        }
    ){
        //Marker
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
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun AddScreen() {
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
    var cameraPositionState: CameraPositionState = rememberCameraPositionState {
        // 카메라 초기 위치를 설정합니다.
        //position = CameraPosition(seoul, 11.0)
    }

    val position by remember {
        derivedStateOf {
            cameraPositionState.position
        }
    }
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
fun SettingsScreen() {
    Text(text = "Settings")
}
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            // 권한이 거부되었을 때 처리
        }
    }

    SideEffect {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
//@Composable
//fun CheckLocationPermission(): Boolean {
//    val context = LocalContext.current
//    return ContextCompat.checkSelfPermission(
//        context,
//        Manifest.permission.ACCESS_FINE_LOCATION
//    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
//}