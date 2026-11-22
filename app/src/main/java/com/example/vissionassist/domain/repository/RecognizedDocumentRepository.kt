package com.example.vissionassist.domain.repository

import com.example.vissionassist.domain.model.RecognizedDocument
import kotlinx.coroutines.flow.Flow

interface RecognizedDocumentRepository {

    suspend fun saveDocument(document: RecognizedDocument)
    fun getAllDocuments(): Flow<List<RecognizedDocument>>
    suspend fun getDocumentById(id: String): RecognizedDocument?
    suspend fun deleteDocument(id: String)
}