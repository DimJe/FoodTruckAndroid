package com.example.foodtruck.di

import com.example.foodtruck.network.AddressService
import com.example.foodtruck.network.TruckService
import com.example.foodtruck.repository.AddressRepository
import com.example.foodtruck.repository.TruckRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object module {

    @Singleton
    @Provides
    fun provideTruckApi(): TruckService {
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
        return retrofit.create(TruckService::class.java)
    }
    @Singleton
    @Provides
    fun provideKaKaoApi(): AddressService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(AddressService::class.java)
    }

    @Singleton
    @Provides
    fun provideKaKaoRepository(kakaoService: AddressService): AddressRepository {
        return AddressRepository(kakaoService)
    }

    @Singleton
    @Provides
    fun provideTruckRepository(truckService: TruckService): TruckRepository {
        return TruckRepository(truckService)
    }


}