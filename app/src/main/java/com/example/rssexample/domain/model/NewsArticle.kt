package com.example.rssexample.domain.model

data class NewsArticle(
    val title: String,
    val link: String,
    val description: String,
    val pubDate: String,
    val author: String? = null,
    val contributor: String? = null,
    val guid: String? = null,
    val dateTimeWritten: String? = null,
    val updateDate: String? = null,
    val expires: String? = null,
    val thumbnailUrl: String? = null,
    val mediaImages: List<MediaImage> = emptyList()
)

data class MediaImage(
    val url: String,
    val credit: String? = null,
    val description: String? = null,
    val caption: String? = null,
    val width: String? = null,
    val height: String? = null
)

