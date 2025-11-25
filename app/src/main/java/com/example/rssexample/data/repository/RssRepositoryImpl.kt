package com.example.rssexample.data.repository

import com.example.rssexample.data.model.RssItem
import com.example.rssexample.data.remote.RssApiService
import com.example.rssexample.domain.model.MediaImage
import com.example.rssexample.domain.model.NewsArticle
import com.example.rssexample.domain.repository.RssRepository
import com.example.rssexample.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RssRepositoryImpl @Inject constructor(
    private val apiService: RssApiService
) : RssRepository {

    override suspend fun getFeedItems(): Result<List<NewsArticle>> = withContext(Dispatchers.IO) {
        try {
            val rssFeed = apiService.getRssFeed()
            
            // Debug raw RSS feed
            println("DEBUG RSS: Feed parsed")
            println("  Channel title: ${rssFeed.channel?.title}")
            println("  Channel items count: ${rssFeed.channel?.items?.size}")
            
            rssFeed.channel?.items?.firstOrNull()?.let { rawItem ->
                println("\nDEBUG First RAW Item:")
                println("  Title: ${rawItem.title}")

                println("  Creator: ${rawItem.creator}")
                println("  Contributor: ${rawItem.contributor}")
                println("  Thumbnail: ${rawItem.thumbnail?.url}")
                println("  Media content count: ${rawItem.content?.size}")
                rawItem.content?.forEach { media ->
                    println("    - Medium: ${media.medium}, URL: ${media.url}")
                    println("      Credit: ${media.credit?.value}")
                    println("      Text: ${media.text?.value}")
                }
            }
            
            val items = rssFeed.channel?.items?.map { it.toNewsArticle() } ?: emptyList()
            
            // Debug: Log first item to verify parsing
            items.firstOrNull()?.let { article ->
                println("\nDEBUG First CONVERTED Article:")
                println("  Title: ${article.title}")
                println("  Thumbnail: ${article.thumbnailUrl}")
                println("  Media Images: ${article.mediaImages.size}")
                article.mediaImages.forEachIndexed { index, img ->
                    println("    Image $index: ${img.url.take(50)}...")
                    println("      Credit: ${img.credit}")
                }
            }
            
            Result.Success(items)
        } catch (e: Exception) {
            println("ERROR RSS Parsing: ${e.message}")
            e.printStackTrace()
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }

    private fun RssItem.toNewsArticle(): NewsArticle {
        // Extract all media:content images with full metadata
        val mediaImages = this.content
            ?.filter { it.medium == "image" }
            ?.map { media ->
                MediaImage(
                    url = media.url.orEmpty(),
                    credit = media.credit?.value,
                    description = media.description?.value?.stripHtmlTags(),
                    caption = media.text?.value,
                    width = media.width,
                    height = media.height
                )
            } ?: emptyList()
        
        // Fallback: If no media images, try to use thumbnail or enclosure as a single image
        val finalImages = if (mediaImages.isEmpty()) {
            val fallbackUrl = this.thumbnail?.url ?: this.enclosure?.url
            if (fallbackUrl != null) {
                listOf(MediaImage(url = fallbackUrl))
            } else {
                emptyList()
            }
        } else {
            mediaImages
        }
        
        return NewsArticle(
            title = this.title.orEmpty(),
            link = this.link.orEmpty(),
            description = this.description.orEmpty().stripHtmlTags(),
            pubDate = this.pubDate.orEmpty(),
            author = this.creator,
            contributor = this.contributor,
            guid = this.guid,
            dateTimeWritten = this.dateTimeWritten,
            updateDate = this.updateDate,
            expires = this.expires,
            thumbnailUrl = this.thumbnail?.url ?: this.enclosure?.url,
            mediaImages = finalImages
        )
    }

    private fun String.stripHtmlTags(): String {
        return this.replace(Regex("<.*?>"), "")
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .trim()
    }
}

