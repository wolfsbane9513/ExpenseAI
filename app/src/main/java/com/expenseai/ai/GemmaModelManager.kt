package com.expenseai.ai

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.FileOutputStream
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
    private val supportedExtensions = listOf(".litertlm", ".task", ".bin", ".tflite")

    fun getModelPath(): String? {
        val modelFile = modelDir.listFiles()?.find {
            supportedExtensions.any { extension -> it.name.endsWith(extension, ignoreCase = true) }
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

    fun supportedModelFormats(): List<String> = supportedExtensions

    fun getModelFileName(): String? = getModelPath()?.let { File(it).name }

    fun getImportSummary(): String =
        "Import a MediaPipe-compatible Gemma bundle (${supportedExtensions.joinToString()}) into app storage. Models are too large to package inside the APK."

    suspend fun importModel(uri: Uri) {
        updateStatus(ModelStatus.DOWNLOADING, "Importing model into app storage…")

        val displayName = resolveDisplayName(uri)
            ?: throw IllegalArgumentException("Unable to determine the selected file name.")
        if (supportedExtensions.none { displayName.endsWith(it, ignoreCase = true) }) {
            throw IllegalArgumentException("Unsupported model format. Use ${supportedExtensions.joinToString()} files.")
        }

        val safeName = displayName.replace(Regex("[^A-Za-z0-9._-]"), "_")
        val targetDir = getModelDirectory()
        targetDir.listFiles()?.forEach { existing -> existing.delete() }

        val targetFile = File(targetDir, safeName)
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(targetFile).use { output ->
                input.copyTo(output)
            }
        } ?: throw IllegalStateException("Unable to open the selected model file.")
    }

    fun clearModel() {
        getModelDirectory().listFiles()?.forEach { it.delete() }
        updateStatus(ModelStatus.NOT_DOWNLOADED, "Model removed from device storage.")
    }

    private fun resolveDisplayName(uri: Uri): String? {
        context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
            ?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex >= 0) return cursor.getString(columnIndex)
                }
            }
        return uri.lastPathSegment?.substringAfterLast('/')
    }
}
