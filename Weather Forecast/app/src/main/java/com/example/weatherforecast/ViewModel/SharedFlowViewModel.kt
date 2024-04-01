package com.example.weatherforecast.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SharedFlowViewModel : ViewModel() {
    private val _languageChangeFlow = MutableSharedFlow<String>()
    val languageChangeFlow: SharedFlow<String> = _languageChangeFlow

    fun setLanguage(language: String) {
        viewModelScope.launch {
            _languageChangeFlow.emit(language)
        }
    }
}