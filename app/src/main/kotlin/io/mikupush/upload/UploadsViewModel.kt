package io.mikupush.upload

import io.mikupush.notification.UploadedSignal
import io.mikupush.ui.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UploadsViewModel(private val uploadedSignal: UploadedSignal) : ViewModel() {
    private val _uiState = MutableStateFlow<List<Upload>>(listOf())
    val uiState = _uiState.asStateFlow()

    fun showAllUploads() = viewModelScope.launch {
        uploadedSignal.onSignal {
            _uiState.update { findAllUploads() }
        }

        _uiState.update { findAllUploads() }
    }
}