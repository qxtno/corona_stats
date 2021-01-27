package com.github.qxtno.coronastats.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.qxtno.coronastats.model.Historical
import com.github.qxtno.coronastats.model.SummaryResponse
import com.github.qxtno.coronastats.retrofitHelpers.ApiRepository

class CurrentDataViewModel(country: String) : ViewModel() {
    private val apiRepository = ApiRepository()
    private var summary = MutableLiveData<SummaryResponse>()
    private var historical = MutableLiveData<List<Historical>>()
    private var mutableCountry = MutableLiveData<String>()
    fun setCountry(country: String) {
        mutableCountry.value = country

        loadSummary(country)
        loadHistorical(country)
    }

    init {
        setCountry(country)
    }

    private fun loadSummary(country: String) {
        summary = apiRepository.getApiSummary(country)
    }

    private fun loadHistorical(country: String) {
        historical = apiRepository.getApiHistorical(country)
    }


    fun getSummary(): MutableLiveData<SummaryResponse> = summary
    fun getHistorical(): MutableLiveData<List<Historical>> = historical

    fun getRefreshed(): MutableLiveData<SummaryResponse> {
        summary = apiRepository.getApiSummary(mutableCountry.value ?: "poland")
        return summary
    }

    fun getHistoricalRefreshed(): MutableLiveData<List<Historical>> {
        historical = apiRepository.getApiHistorical(mutableCountry.value ?: "poland")
        return historical
    }

}