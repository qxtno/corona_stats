package com.github.qxtno.coronastats.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.qxtno.coronastats.model.SummaryResponse
import com.github.qxtno.coronastats.retrofitHelpers.ApiRepository

class CountrySelectViewModel : ViewModel() {
    private val apiRepository = ApiRepository()
    private var countries = MutableLiveData<List<SummaryResponse>>()

    init {
        loadCountries()
    }

    private fun loadCountries() {
        countries = apiRepository.getApiCountries()
    }

    fun getCountries(): MutableLiveData<List<SummaryResponse>> = countries
}