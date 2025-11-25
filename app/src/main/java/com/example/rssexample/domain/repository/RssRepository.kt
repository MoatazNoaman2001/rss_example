package com.example.rssexample.domain.repository

import com.example.rssexample.domain.model.NewsArticle
import com.example.rssexample.domain.util.Result

interface RssRepository {
    suspend fun getFeedItems(): Result<List<NewsArticle>>
}

