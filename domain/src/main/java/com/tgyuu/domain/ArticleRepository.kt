package com.tgyuu.domain

interface ArticleRepository {
    suspend fun getArticles() : List<String>
}