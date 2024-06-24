package com.example.foodtruck

import android.util.Log
import com.example.foodtruck.network.TruckService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.math.log

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Mock
    private lateinit var truckService: TruckService

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // 연결 Timeout 10초
            .readTimeout(30, TimeUnit.SECONDS)    // 읽기 Timeout 30초
            .writeTimeout(15, TimeUnit.SECONDS)   // 쓰기 Timeout 15초
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.6:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        truckService = retrofit.create(TruckService::class.java)
    }
    @Test
    fun testTruckService() = runBlocking {


        val response = truckService.getTruckData()
        System.out.println(response.body())
        assertEquals(200,response.code())
    }
}