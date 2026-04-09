package com.expenseai.security

import com.expenseai.ai.GemmaModelManager
import java.io.File
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Verifies the integrity of the on-device Gemma model
 * to prevent model tampering or adversarial model injection.
 */
@Singleton
class ModelIntegrityChecker @Inject constructor(
    private val modelManager: GemmaModelManager,
    private val encryptedPreferences: EncryptedPreferences
) {
    data class IntegrityResult(
        val isValid: Boolean,
        val message: String
    )

    fun verifyModelIntegrity(): IntegrityResult {
        val modelPath = modelManager.getModelPath()
            ?: return IntegrityResult(false, "Model file not found")

        val modelFile = File(modelPath)
        if (!modelFile.exists()) {
            return IntegrityResult(false, "Model file does not exist")
        }

        // Check file size is reasonable for an on-device Gemma model bundle.
        val fileSizeMB = modelFile.length() / (1024 * 1024)
        if (fileSizeMB < 100 || fileSizeMB > 5000) {
            return IntegrityResult(false, "Model file size ($fileSizeMB MB) is suspicious")
        }

        // Verify checksum
        val currentChecksum = computeChecksum(modelFile)
        val storedChecksum = encryptedPreferences.getModelChecksum()

        if (storedChecksum == null) {
            // First time - store the checksum
            encryptedPreferences.storeModelChecksum(currentChecksum)
            return IntegrityResult(true, "Model checksum stored for future verification")
        }

        return if (currentChecksum == storedChecksum) {
            IntegrityResult(true, "Model integrity verified")
        } else {
            IntegrityResult(false, "Model checksum mismatch - possible tampering detected")
        }
    }

    private fun computeChecksum(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val buffer = ByteArray(8192)

        file.inputStream().use { input ->
            var bytesRead = input.read(buffer)
            while (bytesRead != -1) {
                digest.update(buffer, 0, bytesRead)
                bytesRead = input.read(buffer)
            }
        }

        return digest.digest().joinToString("") { "%02x".format(it) }
    }
}
