package com.github.qxtno.coronastats.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class CurrentDataViewModelFactory(private val country: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CurrentDataViewModel::class.java)) {
            CurrentDataViewModel(country) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }

}