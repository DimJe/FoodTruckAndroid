package com.example.foodtruck.ui.screens.add

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodtruck.viewmodel.TruckViewModel
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

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun AddScreen(viewmodel: TruckViewModel) {

    val location = viewmodel.location.value
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
        position = CameraPosition(LatLng(location!!), 18.0)
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
    Box(modifier = Modifier.wrapContentSize()) {
        NaverMap(
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings
        ){
            //Marker
            Marker(
                state = MarkerState(position = cameraPositionState.position.target),
                captionText = "center",
                width = 25.dp,
                height = 25.dp
            )
        }
        AddDetailScreenBtn(modifier = Modifier.align(Alignment.BottomCenter))

    }
}
@Composable
fun AddDetailScreenBtn(modifier: Modifier){
    Column(
        modifier = modifier.fillMaxWidth()
            .padding(14.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray),
        verticalArrangement = Arrangement.spacedBy(5.dp))
    {
        Text(text = "서울특별시 관악구 봉천동 1688-48",
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(top = 10.dp),
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(text = "여기가 맞나요??")
        }
    }
}