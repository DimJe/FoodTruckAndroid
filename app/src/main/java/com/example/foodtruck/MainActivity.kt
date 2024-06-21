package com.example.foodtruck

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
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

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodTruckTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    MapScreen()
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
        modifier = Modifier.fillMaxSize(),
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FoodTruckTheme {
        Greeting("Android")
    }
}