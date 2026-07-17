package com.expenseai.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/** Renders PDF pages to bitmaps (built-in PdfRenderer) and OCRs each page. */
@Singleton
class PdfTextExtractor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ocrService: OCRService
) {
    suspend fun extract(uri: Uri): List<String> = withContext(Dispatchers.IO) {
        val cacheFile = File.createTempFile("import_doc", ".pdf", context.cacheDir)
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                cacheFile.outputStream().use { output -> input.copyTo(output) }
            } ?: throw IOException("Cannot open document")

            ParcelFileDescriptor.open(cacheFile, ParcelFileDescriptor.MODE_READ_ONLY).use { pfd ->
                PdfRenderer(pfd).use { renderer ->
                    val pageCount = minOf(renderer.pageCount, MAX_PAGES)
                    (0 until pageCount).map { index ->
                        renderer.openPage(index).use { page ->
                            val scale = minOf(
                                RENDER_SCALE.toFloat(),
                                MAX_BITMAP_EDGE.toFloat() / maxOf(page.width, page.height)
                            )
                            val bitmap = Bitmap.createBitmap(
                                (page.width * scale).toInt().coerceAtLeast(1),
                                (page.height * scale).toInt().coerceAtLeast(1),
                                Bitmap.Config.ARGB_8888
                            )
                            try {
                                bitmap.eraseColor(Color.WHITE)
                                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                                ocrService.extractText(bitmap)
                            } finally {
                                bitmap.recycle()
                            }
                        }
                    }
                }
            }
        } finally {
            cacheFile.delete()
        }
    }

    companion object {
        const val MAX_PAGES = 10
        private const val RENDER_SCALE = 2
        private const val MAX_BITMAP_EDGE = 4096
    }
}
