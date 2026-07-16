package com.expenseai.ui.screens.docimport

import com.expenseai.ai.DocumentExtraction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/** Hands the extraction result from Sources to the review screen (nav args can't carry it). */
@Singleton
class ExtractionResultHolder @Inject constructor() {
    private val _extraction = MutableStateFlow<DocumentExtraction?>(null)
    val extraction: StateFlow<DocumentExtraction?> = _extraction.asStateFlow()

    fun set(value: DocumentExtraction) { _extraction.value = value }
    fun clear() { _extraction.value = null }
}
