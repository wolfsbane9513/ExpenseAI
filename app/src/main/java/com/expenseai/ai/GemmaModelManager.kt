package com.expenseai.ai

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

enum class ModelStatus {
    NOT_DOWNLOADED,
    DOWNLOADING,
    LOADING,
    READY,
    ERROR
}

@Singleton
class GemmaModelManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _status = MutableStateFlow(ModelStatus.NOT_DOWNLOADED)
    val status: StateFlow<ModelStatus> = _status.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val modelDir = File(context.filesDir, "gemma_model")

    fun getModelPath(): String? {
        val modelFile = modelDir.listFiles()?.find {
            it.name.endsWith(".bin") || it.name.endsWith(".tflite")
        }
        return modelFile?.absolutePath
    }

    fun isModelAvailable(): Boolean = getModelPath() != null

    fun updateStatus(status: ModelStatus, error: String? = null) {
        _status.value = status
        _errorMessage.value = error
    }

    fun getModelDirectory(): File {
        if (!modelDir.exists()) modelDir.mkdirs()
        return modelDir
    }
}
