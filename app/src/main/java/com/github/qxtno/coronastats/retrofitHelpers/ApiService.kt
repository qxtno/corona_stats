package com.github.qxtno.coronastats.retrofitHelpers

import com.github.qxtno.coronastats.model.ContinentResponse
import com.github.qxtno.coronastats.model.HistoricalResponse
import com.github.qxtno.coronastats.model.SummaryResponse
import com.github.qxtno.coronastats.model.TimeLine
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET("countries/{country}")
    fun getSummary(@Path("country") country: String): Call<SummaryResponse>

    @GET("all")
    fun getGlobalAll(): Call<SummaryResponse>

    @GET("countries")
    fun getCountries(): Call<List<SummaryResponse>>

    @GET("historical/{country}")
    fun getHistorical(@Path("country") country: String): Call<HistoricalResponse>

    @GET("historical/all")
    fun getHistoricalAll(): Call<TimeLine>

    @GET("continents")
    fun getContinents(): Call<List<ContinentResponse>>

    @GET("continents/{continent}")
    fun getContinent(@Path("continent") continent: String): Call<ContinentResponse>
}