package com.example.foodtruck.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.foodtruck.viewmodel.TruckViewModel
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun HomeScreen(viewmodel: TruckViewModel){
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoom = 30.0, minZoom = 18.0, locationTrackingMode = LocationTrackingMode.Follow )
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
        onLocationChange = {
            viewmodel.location.value = it
        }
    ){
        //Marker
    }
}