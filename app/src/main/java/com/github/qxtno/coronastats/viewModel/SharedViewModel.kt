package com.github.qxtno.coronastats.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val mutableCountry = MutableLiveData<String>()
    val country: LiveData<String> get() = mutableCountry

    fun setCountry(country: String) {
        mutableCountry.value = country
    }
}