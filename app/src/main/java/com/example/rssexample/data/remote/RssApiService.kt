package com.example.rssexample.data.remote

import com.example.rssexample.data.model.RssFeed
import retrofit2.http.GET

interface RssApiService {
    @GET("nbcnews/public/world")
    suspend fun getRssFeed(): RssFeed
}

