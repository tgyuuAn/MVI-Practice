package com.tgyuu.data.repository

import com.tgyuu.domain.ArticleRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor() : ArticleRepository {
    override suspend fun getArticles(): List<String> {
        delay(500L)
        return listOf("a", "b", "c", "d")
    }
}