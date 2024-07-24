package com.example.foodtruck.ui.screens.add

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.foodtruck.model.TruckRequest
import com.example.foodtruck.ui.theme.Orange
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
import java.math.RoundingMode


@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun AddScreen(viewmodel: TruckViewModel,navigate: () -> Unit) {

    val location = viewmodel.location.value
    val address by remember {
        viewmodel.address
    }
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
            viewmodel.newTruckPosition.value = position.target
            viewmodel.getAddress(
                position.target.longitude.toBigDecimal().setScale(7,RoundingMode.DOWN).toDouble(),
                position.target.latitude.toBigDecimal().setScale(7,RoundingMode.DOWN).toDouble()
            )
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
        AddDetailScreenBtn(modifier = Modifier.align(Alignment.BottomCenter),address,navigate)
    }
}
@Composable
fun AddDetailScreenBtn(modifier: Modifier,text: String,navigate: () -> Unit){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(14.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray),
        verticalArrangement = Arrangement.spacedBy(5.dp))
    {
        Text(text = text,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp),
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = {
                navigate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(text = "여기가 맞나요??")
        }
    }
}

/**
 * AddDetailScreen
 */
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun AddDetailScreen(viewmodel: TruckViewModel?,onVisibilityChange: () -> Unit){

    var name by remember { mutableStateOf("")}
    var menu by remember { mutableStateOf("")}
    val item = listOf("일","월","화","수","목","금","토")
    val selectedItem = remember { mutableStateOf(setOf<String>()) }
    val isEnabled = name.isNotEmpty() && menu.isNotEmpty() && selectedItem.value.isNotEmpty()
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 10.dp, end = 10.dp)
        //키보드 포커스 해제
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { focusManager.clearFocus() },
        verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { onVisibilityChange() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                }
                Text(text = "가게 추가", modifier = Modifier.align(Alignment.Center), fontWeight = FontWeight.Bold)
            }

            NaverMap(
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition(LatLng(viewmodel?.newTruckPosition?.value!!.latitude, viewmodel.newTruckPosition.value!!.longitude), 18.0)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(vertical = 10.dp, horizontal = 20.dp)
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
                    .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
            ){
                Marker(
                    state = MarkerState(position = LatLng(viewmodel?.newTruckPosition?.value!!.latitude, viewmodel.newTruckPosition.value!!.longitude)),
                    captionText = "center",
                    width = 25.dp,
                    height = 25.dp
                )
            }

            Text(text = "이름", color = Color.Black, modifier = Modifier.padding(start = 10.dp, bottom = 15.dp), fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(text = "이름")
                }
            )
            Text(text = "메뉴", color = Color.Black, modifier = Modifier.padding(start = 10.dp, bottom = 15.dp,top = 10.dp), fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = menu,
                onValueChange = { menu = it },
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(text = "메뉴")
                }
            )
            Text(text = "요일", color = Color.Black, modifier = Modifier.padding(start = 10.dp,top = 10.dp), fontWeight = FontWeight.Bold)
            Row(modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                item.forEach {
                    Button(
                        shape = CircleShape,
                        modifier = Modifier
                            .size(45.dp)
                            .padding(5.dp)
                            .clip(CircleShape),
                        onClick = {
                            Log.d("test", "AddDetailScreen: ${it} ")
                            selectedItem.value = if (it in selectedItem.value) {
                                selectedItem.value - it
                            } else {
                                selectedItem.value + it
                            }
                        },
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (it in selectedItem.value) Orange else Color.LightGray,
                            contentColor = if (it in selectedItem.value) Color.Black else Color.White
                        )
                    ) {
                        Text(it, modifier = Modifier.align(Alignment.CenterVertically), textAlign = TextAlign.Center)
                    }
                }

            }
            Button(
                onClick = {
                    //등장 요일 비트 변환
                    var day = 0
                    for (i in 0..6) {
                        if(item[i] in selectedItem.value){
                            day = day or (1 shl (6-i))
                        }
                    }
                    val data = TruckRequest(
                        name = name,
                        latitude = viewmodel!!.newTruckPosition.value!!.latitude,
                        longitude = viewmodel.newTruckPosition.value!!.longitude,
                        openDay = day.toShort(),
                        address = viewmodel.address.value
                    )
                    Log.d("test", "AddDetailScreen: $data")
                    //등록 요청
                    viewmodel.postTruck(
                        data
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp),
                colors =
                    if(!isEnabled) ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Gray
                    )
                    else ButtonDefaults.buttonColors(
                        containerColor = Orange,
                        contentColor = Color.Black
                    )
                ,
                enabled = isEnabled
            ) {
                Text(text = "등록 요청하기")
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun Preview() {
    AddDetailScreen(viewmodel = null) {

    }
}