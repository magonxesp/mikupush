package io.mikupush.upload

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
import java.util.*
import kotlin.io.path.Path

class UploadViewModel(
    private val notifier: Notifier,
    private val fileUploader: FileUploader
) : ViewModel() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val uploadJobs = mutableMapOf<UUID, Job>()
    private val _uploads = MutableStateFlow<List<Upload>>(listOf())
    val uploads = _uploads.asStateFlow()

    fun startUpload(filePath: String) {
        val fileId = UUID.randomUUID()
        val job = viewModelScope.launch(Dispatchers.IO) {
            upload(fileId, filePath)
        }

        uploadJobs[fileId] = job
    }

    private suspend fun upload(fileId: UUID, filePath: String) {
        logger.debug("starting upload file {}", filePath)
        val file = Path(filePath).toFile()

        var upload = Upload(
            details = UploadDetails(
                id = fileId,
                fileName = file.name,
                fileMimeType = Tika().detect(file),
                fileSizeBytes = file.length(),
                uploadedAt = Clock.System.now()
            )
        )

        _uploads.update { state -> listOf(upload) + state }
        notifier.notify(
            title = "Uploading ${upload.details.fileName} 🚀",
            message = "It will take some time, please be patient"
        )

        try {
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
            onFailedUpload(upload)
        }

        uploadJobs.remove(fileId)
    }

    fun delete(fileId: UUID) = viewModelScope.launch {
        backendHttpClient.use {
            backendHttpClient.delete("/upload/$fileId") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
        }

        _uploads.update { state -> state.filter { it.details.id != fileId } }
    }

    fun cancel(fileId: UUID) {
        val job = uploadJobs[fileId] ?: return
        job.cancel()

        delete(fileId)
    }

    fun loadUploads() = viewModelScope.launch {
        _uploads.update { findAllUploads() }
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
        copyToClipboard("$backendBaseUrl/${upload.details.id}")

        notifier.notify(
            title = "Link copied to the clipboard 📎",
            message = "The file ${upload.details.fileName} has been uploaded"
        )

        updateUploadProgress(upload.copy(progress = 1f))
    }

    private suspend fun onFailedUpload(upload: Upload) {
        notifier.notifyError(
            title = "Failed uploading ${upload.details.fileName}",
            message = "An error occurred uploading the file, try again later"
        )
    }
}