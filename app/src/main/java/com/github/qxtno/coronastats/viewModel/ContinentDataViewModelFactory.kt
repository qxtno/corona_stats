package com.github.qxtno.coronastats.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ContinentDataViewModelFactory(
    private val continent: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ContinentDataViewModel::class.java)) {
            ContinentDataViewModel(continent) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}