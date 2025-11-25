package com.example.rssexample.domain.usecase

import com.example.rssexample.domain.model.NewsArticle
import com.example.rssexample.domain.repository.RssRepository
import com.example.rssexample.domain.util.Result
import javax.inject.Inject

class GetRssFeedUseCase @Inject constructor(
    private val repository: RssRepository
) {
    suspend operator fun invoke(): Result<List<NewsArticle>> {
        return repository.getFeedItems()
    }
}

