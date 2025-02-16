package io.mikupush.upload

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import io.ktor.client.request.*
import io.ktor.http.*
import io.mikupush.backendBaseUrl
import io.mikupush.http.backendHttpClient
import io.mikupush.notification.Notifier
import io.mikupush.ui.ViewModel
import io.mikupush.ui.copyToClipboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import java.awt.Desktop
import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path


class UploadViewModel(
    private val notifier: Notifier,
    private val fileUploader: FileUploader
) : ViewModel() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val uploadJobs = mutableMapOf<UUID, Job>()
    private val _uploads = MutableStateFlow<List<Upload>>(listOf())
    private val _showWindow = MutableStateFlow(false)
    val uploads = _uploads.asStateFlow()
    val showWindow = _showWindow.asStateFlow()

    fun startUpload(filePath: String, fileId: UUID = UUID.randomUUID(), notifyUpload: Boolean = true) {
        _uploads.update { state ->
            state.filter { it.details.id != fileId }
        }

        val job = viewModelScope.launch(Dispatchers.IO) {
            upload(fileId, filePath, notifyUpload)
        }

        uploadJobs[fileId] = job
    }

    fun startUploadMultiple(filePaths: List<String>) = viewModelScope.launch {
        notifier.notify(
            title = "Uploading ${filePaths.size} files 🚀",
            message = "It will take some time, please be patient"
        )

        for (filePath in filePaths) {
            startUpload(filePath, notifyUpload = false)
        }
    }

    private suspend fun upload(fileId: UUID, filePath: String, notifyUpload: Boolean = true) {
        logger.debug("starting upload file {}", filePath)
        val path = Path(filePath)
        val file = path.toFile()

        if (!file.isFile) {
            return
        }

        var upload = Upload(
            details = UploadDetails(
                id = fileId,
                fileName = file.name,
                fileMimeType = Tika().detect(file),
                fileSizeBytes = file.length(),
                uploadedAt = Clock.System.now()
            ),
            path = path
        )

        try {
            _uploads.update { state -> listOf(upload) + state }

            if (notifyUpload) {
                notifier.notify(
                    title = "Uploading ${upload.details.fileName} 🚀",
                    message = "It will take some time, please be patient"
                )
            }

            fileUploader.upload(
                fileId = fileId,
                file = file,
                onProgress = { progress ->
                    upload = upload.copy(progress = progress)
                    updateUploadProgress(upload)
                },
                onByteRate = { rate ->
                    upload = upload.copy(bytesUploadedRate = rate)
                    updateUploadProgress(upload)
                }
            )

            onSuccessUpload(upload)
        } catch (exception: Exception) {
            logger.warn("failed to upload file ${file.path}", exception)
            onFailedUpload(upload, exception)
        }

        uploadJobs.remove(fileId)
    }

    fun delete(fileId: UUID) = viewModelScope.launch {
        val upload = _uploads.value.firstOrNull {
            it.details.id == fileId
        }

        if (upload?.isFinishedWithError() != true) {
            backendHttpClient.use {
                backendHttpClient.delete("/upload/$fileId") {
                    contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                }
            }
        }

        _uploads.update { state -> state.filter { it.details.id != fileId } }
    }

    fun cancel(fileId: UUID) {
        val job = uploadJobs[fileId] ?: return
        job.cancel()

        delete(fileId)
    }

    fun copyLinkToClipboard(fileId: UUID) = viewModelScope.launch {
        val upload = findById(fileId) ?: return@launch
        copyToClipboard("$backendBaseUrl/${upload.details.id}")

        notifier.notify(
            title = "Link copied to the clipboard 📎",
            message = "The file ${upload.details.fileName} has been uploaded"
        )
    }

    fun showInFileExplorer(path: Path) = viewModelScope.launch {
        val desktop = Desktop.getDesktop()
        val file = path.parent.toFile()

        if (file.isDirectory) {
            desktop.open(file)
        }
    }

    fun loadUploads() = viewModelScope.launch {
        _uploads.update { findAllUploads() }
    }

    fun showWindow() = viewModelScope.launch {
        logger.debug("Show window")
        _showWindow.update { _ -> true }
    }

    fun closeWindow() = viewModelScope.launch {
        logger.debug("Close window")
        _showWindow.update { _ -> false }
    }

    private fun updateUploadProgress(upload: Upload) = viewModelScope.launch {
        _uploads.update { state ->
            state.map { if (upload.details.id == it.details.id) upload else it }
        }
    }

    private suspend fun onSuccessUpload(upload: Upload) {
        backendHttpClient.use {
            backendHttpClient.put("/upload/details") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(upload.details)
            }
        }

        upload.insert()
        copyLinkToClipboard(upload.details.id)

        updateUploadProgress(upload.copy(finished = true))
    }

    private suspend fun onFailedUpload(upload: Upload, exception: Exception) {
        notifier.notifyError(
            title = "Failed uploading ${upload.details.fileName}",
            message = "An error occurred uploading the file, try again later"
        )

        updateUploadProgress(upload.copy(finished = true, error = exception.message ?: "failed with unknown reason"))
    }
}