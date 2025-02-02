package io.mikupush.upload

import io.mikupush.notification.Notifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.concurrent.thread
import kotlin.io.path.Path

class UploadViewModel(private val notifier: Notifier) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val uploadChannel = Channel<UploadRequest>(100)
    private val progressChannel = Channel<UploadProgress>(1)
    private val finishedChannel = Channel<UploadFinished>(1)
    private val uploader = FileUploader(uploadChannel, progressChannel, finishedChannel)

    data class UploadingItem(
        val fileId: UUID,
        val fileName: String,
        val fileMimeType: String,
        val fileSizeBytes: Long,
        val progress: Float = 0f,
        val bytesUploadedRate: Long = 0,
    )

    private val _uiState = MutableStateFlow<List<UploadingItem>>(listOf())
    val uiState = _uiState.asStateFlow()

    init {
        val job = CoroutineScope(Job()).launch {
            onProgressUpdated()
            onFinishUpload()
        }

        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            job.cancel()
        })
    }

    suspend fun upload(filePath: String) {
        logger.debug("starting upload file {}", filePath)
        uploader.startUploadingFiles()

        val request = UploadRequest(UUID.randomUUID(), Path(filePath))
        val item = request.toItem()

        uploadChannel.send(request)
        _uiState.update { state -> listOf(item) + state }
    }

    private fun CoroutineScope.onProgressUpdated() = launch {
        logger.debug("listening to progress updates")
        for (progress in progressChannel) {
            _uiState.update { state ->
                state.map {
                    if (it.fileId == progress.fileId) {
                        it.updateItem(progress)
                    } else {
                        it
                    }
                }
            }
        }
    }

    private fun CoroutineScope.onFinishUpload() = launch {
        logger.debug("listening to finished uploads")
        for (finished in finishedChannel) {
            when(finished.state) {
                UploadFinishState.SUCCESS -> {
                    notifier.notify(
                        title = "The file ${finished.fileName} has been uploaded",
                        message = "The link is copied to the clipboard"
                    )

                    _uiState.update { state -> state.filter { it.fileId != finished.fileId } }
                }
                UploadFinishState.FAILED -> notifier.notifyError(
                    title = "Failed uploading ${finished.fileName}",
                    message = "An error occurred uploading the file, try again later"
                )
            }
        }
    }

    private fun UploadRequest.toItem(): UploadingItem {
        val file = path.toFile()

        return UploadingItem(
            fileId = fileId,
            fileName = file.name,
            fileSizeBytes = file.length(),
            fileMimeType = Tika().detect(file),
        )
    }

    private fun UploadingItem.updateItem(progress: UploadProgress) = copy(
        progress = progress.progress,
        bytesUploadedRate = progress.bytesRate,
    )
}