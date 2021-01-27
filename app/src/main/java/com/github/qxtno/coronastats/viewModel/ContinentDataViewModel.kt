package com.github.qxtno.coronastats.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.qxtno.coronastats.model.ContinentResponse
import com.github.qxtno.coronastats.model.SummaryResponse
import com.github.qxtno.coronastats.retrofitHelpers.ApiRepository

class ContinentDataViewModel(private val continent: String) : ViewModel() {
    private val apiRepository = ApiRepository()
    private var continentData = MutableLiveData<ContinentResponse>()
    private var countries = MutableLiveData<List<SummaryResponse>>()

    init {
        loadContinent(continent)
    }

    private fun loadContinent(continent: String) {
        continentData = apiRepository.getApiContinent(continent)
        countries = apiRepository.getApiCountries()
    }

    fun getContinent(): MutableLiveData<ContinentResponse> = continentData
    fun getCountries(): MutableLiveData<List<SummaryResponse>> = countries

    fun updateContinent(): MutableLiveData<ContinentResponse> {
        continentData = apiRepository.getApiContinent(continent)
        return continentData
    }

}