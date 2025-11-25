package com.example.rssexample.presentation.feed

import com.example.rssexample.domain.model.NewsArticle

sealed class FeedUiState {
    object Initial : FeedUiState()
    object Loading : FeedUiState()
    data class Success(val articles: List<NewsArticle>) : FeedUiState()
    data class Error(val message: String) : FeedUiState()
}

