package io.mikupush.upload

import io.mikupush.backendBaseUrl
import io.mikupush.notification.Notifier
import io.mikupush.ui.ViewModel
import io.mikupush.ui.copyToClipboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.io.path.Path

class UploadViewModel(private val notifier: Notifier) : ViewModel() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val uploadRequestChannel = Channel<UploadRequest>(100)
    private val uploadStateChannel = Channel<UploadState>(1)

    private val _uiState = MutableStateFlow<List<UploadState>>(listOf())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            updateUploadsStates()

            with(FileUploader(1)) {
                uploadFiles(uploadRequestChannel, uploadStateChannel)
            }
        }
    }

    fun upload(filePath: String) = viewModelScope.launch {
        logger.debug("starting upload file {}", filePath)

        val request = UploadRequest(UUID.randomUUID(), Path(filePath))
        uploadRequestChannel.send(request)
    }

    private fun CoroutineScope.updateUploadsStates() = launch {
        logger.debug("listening to progress updates")
        for (uploadState in uploadStateChannel) {
            updateUploadState(uploadState)
            notifySuccessUpload(uploadState)
            notifyFailedUpload(uploadState)
        }
    }

    private suspend fun updateUploadState(uploadState: UploadState) {
        val exists = _uiState.value.any { uploadState.fileId == it.fileId }

        if (!exists) {
            _uiState.update { state -> listOf(uploadState) + state }
            notifyNewUpload(uploadState)
            return
        }

        _uiState.update { state ->
            state.map { if (uploadState.fileId == it.fileId) uploadState else it }
        }
    }

    private suspend fun notifyNewUpload(uploadState: UploadState) {
        notifier.notify(
            title = "Uploading ${uploadState.fileName} 🚀",
            message = "It will take some time, please be patient"
        )
    }

    private suspend fun notifySuccessUpload(uploadState: UploadState) {
        if (uploadState.isFinished() && uploadState.finishState == UploadFinishState.SUCCESS) {
            notifier.notify(
                title = "Link copied to the clipboard 📎",
                message = "The file ${uploadState.fileName} has been uploaded"
            )

            copyToClipboard("$backendBaseUrl/${uploadState.fileId}")
            _uiState.update { state -> state.filter { it.fileId != uploadState.fileId } }
        }
    }

    private suspend fun notifyFailedUpload(uploadState: UploadState) {
        if (uploadState.isFinished() && uploadState.finishState == UploadFinishState.FAILED) {
            notifier.notifyError(
                title = "Failed uploading ${uploadState.fileName}",
                message = "An error occurred uploading the file, try again later"
            )

            _uiState.update { state -> state.filter { it.fileId != uploadState.fileId } }
        }
    }
}