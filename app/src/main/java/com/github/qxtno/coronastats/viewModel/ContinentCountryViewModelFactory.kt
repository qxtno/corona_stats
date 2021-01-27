package com.github.qxtno.coronastats.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
@Suppress("UNCHECKED_CAST")
class ContinentCountryViewModelFactory(private val country: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ContinentCountryViewModel::class.java)) {
            ContinentCountryViewModel(country) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }

}