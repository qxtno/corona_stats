package com.github.qxtno.coronastats.retrofitHelpers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        const val BASE_URL = "https://disease.sh/v3/covid-19/"
    }

    fun getService(): ApiService =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
}