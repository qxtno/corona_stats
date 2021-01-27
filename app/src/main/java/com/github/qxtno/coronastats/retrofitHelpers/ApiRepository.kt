package com.github.qxtno.coronastats.retrofitHelpers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.qxtno.coronastats.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ApiRepository {
    private val client: RetrofitClient = RetrofitClient()

    fun getApiSummary(country: String): MutableLiveData<SummaryResponse> {
        val responseSummary = MutableLiveData<SummaryResponse>()
        client.getService().getSummary(country).enqueue(object : Callback<SummaryResponse> {
            override fun onResponse(
                call: Call<SummaryResponse>?,
                response: Response<SummaryResponse>?
            ) {
                if (response != null) {
                    responseSummary.value = response.body()
                    Log.i("getApiSummary()", "Response OK")
                }
            }

            override fun onFailure(call: Call<SummaryResponse>?, t: Throwable?) {
                Log.i("getApiSummary()", t.toString())
            }
        })
        return responseSummary
    }

    fun getApiGlobalAll(): MutableLiveData<SummaryResponse> {
        val responseGlobalAll = MutableLiveData<SummaryResponse>()
        client.getService().getGlobalAll().enqueue(object : Callback<SummaryResponse> {
            override fun onResponse(
                call: Call<SummaryResponse>?,
                response: Response<SummaryResponse>?
            ) {
                if (response != null) {
                    responseGlobalAll.value = response.body()
                    Log.i("getApiGlobalAll()", "Response OK")
                }
            }

            override fun onFailure(call: Call<SummaryResponse>?, t: Throwable?) {
                Log.i("getApiGlobalAll()", t.toString())
            }
        })
        return responseGlobalAll
    }

    fun getApiCountries(): MutableLiveData<List<SummaryResponse>> {
        val responseCountries = MutableLiveData<List<SummaryResponse>>()
        client.getService().getCountries().enqueue(object : Callback<List<SummaryResponse>> {
            override fun onResponse(
                call: Call<List<SummaryResponse>>?,
                response: Response<List<SummaryResponse>>?
            ) {
                if (response != null) {
                    responseCountries.value = response.body()
                }
                Log.i("getApiCountries()", "Response OK")
            }

            override fun onFailure(call: Call<List<SummaryResponse>>?, t: Throwable?) {
                Log.i("getApiCountries()", t.toString())
            }
        })
        return responseCountries
    }

    fun getApiHistorical(country: String): MutableLiveData<List<Historical>> {
        val responseHistorical = MutableLiveData<List<Historical>>()
        client.getService().getHistorical(country)
            .enqueue(object : Callback<HistoricalResponse> {
                override fun onResponse(
                    call: Call<HistoricalResponse>?,
                    response: Response<HistoricalResponse>?
                ) {
                    if (response != null) {
                        val responseBody = response.body()
                        var list: List<Historical> = emptyList()
                        if (responseBody != null) {
                            list = responseBody.timeline.casesMap.keys.map {
                                val originalFormat: DateFormat =
                                    SimpleDateFormat("MM/dd/yy", Locale.ENGLISH)
                                val targetFormat: DateFormat =
                                    SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
                                val date: Date = originalFormat.parse(it) ?: Date()
                                val formattedDate: String = targetFormat.format(date)

                                val historical = Historical(
                                    formattedDate,
                                    responseBody.timeline.casesMap[it] ?: 0,
                                    responseBody.timeline.deathsMap[it] ?: 0,
                                    responseBody.timeline.recoveredMap[it] ?: 0,
                                )

                                historical
                            }
                        }
                        responseHistorical.value = list
                        Log.i("getApiHistorical()", "Response OK")
                    }
                }

                override fun onFailure(call: Call<HistoricalResponse>?, t: Throwable?) {
                    Log.i("getApiHistorical()", t.toString())
                }
            })
        return responseHistorical
    }

    fun getApiHistoricalAll(): MutableLiveData<List<Historical>> {
        val responseHistoricalAll = MutableLiveData<List<Historical>>()
        client.getService().getHistoricalAll()
            .enqueue(object : Callback<TimeLine> {
                override fun onResponse(
                    call: Call<TimeLine>?,
                    response: Response<TimeLine>?
                ) {
                    val responseBody = response?.body()
                    var list: List<Historical> = emptyList()
                    if (responseBody != null) {
                        list = responseBody.casesMap.keys.map {
                            val originalFormat: DateFormat =
                                SimpleDateFormat("MM/dd/yy", Locale.ENGLISH)
                            val targetFormat: DateFormat =
                                SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
                            val date: Date = originalFormat.parse(it) ?: Date()
                            val formattedDate: String = targetFormat.format(date)

                            val historical = Historical(
                                formattedDate,
                                responseBody.casesMap[it] ?: 0,
                                responseBody.deathsMap[it] ?: 0,
                                responseBody.recoveredMap[it] ?: 0,
                            )

                            historical
                        }
                    }
                    responseHistoricalAll.value = list
                    Log.i("getApiHistoricalAll()", "Response OK")
                }


                override fun onFailure(call: Call<TimeLine>?, t: Throwable?) {
                    Log.i("getApiHistoricalAll()", t.toString())
                }
            })
        return responseHistoricalAll
    }

    fun getApiContinents(): MutableLiveData<List<ContinentResponse>> {
        val responseContinents = MutableLiveData<List<ContinentResponse>>()
        client.getService().getContinents().enqueue(object : Callback<List<ContinentResponse>> {
            override fun onResponse(
                call: Call<List<ContinentResponse>>?,
                response: Response<List<ContinentResponse>>?
            ) {
                responseContinents.value = response?.body()
                Log.i("getApiContinents()", "Response OK")
            }

            override fun onFailure(call: Call<List<ContinentResponse>>?, t: Throwable?) {
                Log.i("getApiContinents()", t.toString())
            }

        })
        return responseContinents
    }

    fun getApiContinent(continent: String): MutableLiveData<ContinentResponse> {
        val responseContinent = MutableLiveData<ContinentResponse>()
        client.getService().getContinent(continent).enqueue(object : Callback<ContinentResponse> {
            override fun onResponse(
                call: Call<ContinentResponse>?,
                response: Response<ContinentResponse>?
            ) {
                responseContinent.value = response?.body()
                Log.i("getApiContinent()", "Response OK")
            }

            override fun onFailure(call: Call<ContinentResponse>?, t: Throwable?) {
                Log.i("getApiContinent()", t.toString())
            }

        })
        return responseContinent
    }
}