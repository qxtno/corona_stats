package com.github.qxtno.coronastats.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.github.qxtno.coronastats.model.ContinentResponse
import com.github.qxtno.coronastats.model.Historical
import com.github.qxtno.coronastats.model.SummaryResponse
import com.github.qxtno.coronastats.retrofitHelpers.ApiRepository

class GlobalDataViewModel(application: Application) : AndroidViewModel(application) {
    private val apiRepository = ApiRepository()
    private var global = MutableLiveData<SummaryResponse>()
    private var historical = MutableLiveData<List<Historical>>()
    private var continents = MutableLiveData<List<ContinentResponse>>()

    init {
        loadGlobalAll()
        loadAllHistorical()
        loadContinents()
    }

    private fun loadGlobalAll() {
        global = apiRepository.getApiGlobalAll()
    }

    private fun loadAllHistorical() {
        historical = apiRepository.getApiHistoricalAll()
    }

    private fun loadContinents() {
        continents = apiRepository.getApiContinents()
    }

    fun getGlobalAll(): MutableLiveData<SummaryResponse> = global
    fun getHistoricalAll(): MutableLiveData<List<Historical>> = historical
    fun getContinents(): MutableLiveData<List<ContinentResponse>> = continents

    fun updateGlobalAll(): MutableLiveData<SummaryResponse> {
        global = apiRepository.getApiGlobalAll()
        return global
    }

    fun updateHistoricalAll(): MutableLiveData<List<Historical>> {
        historical = apiRepository.getApiHistoricalAll()
        return historical
    }

    fun updateContinents(): MutableLiveData<List<ContinentResponse>> {
        continents = apiRepository.getApiContinents()
        return continents
    }

}