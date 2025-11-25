package com.example.rssexample.presentation.feed

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rssexample.domain.model.MediaImage
import com.example.rssexample.domain.model.NewsArticle
import kotlinx.coroutines.launch

@Composable
fun NewsArticleCard(article: NewsArticle) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // Image Gallery - Show all media images
            if (article.mediaImages.isNotEmpty()) {
                ImageGallery(images = article.mediaImages)
            } else if (!article.thumbnailUrl.isNullOrEmpty()) {
                // Fallback to thumbnail if no media images
                AsyncImage(
                    model = article.thumbnailUrl,
                    contentDescription = article.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop,
                    onError = {
                        println("ERROR loading thumbnail: ${article.thumbnailUrl}")
                    }
                )
            } else {
                // Debug: No images found
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "📷 No images available",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Images: ${article.mediaImages.size}, Thumb: ${article.thumbnailUrl != null}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Author(s) and Contributors
                AuthorSection(
                    author = article.author,
                    contributor = article.contributor
                )
                
                // Description
                if (article.description.isNotEmpty()) {
                    Text(
                        text = article.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                        overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Expand/Collapse for more metadata
                if (!isExpanded && (article.dateTimeWritten != null || article.updateDate != null)) {
                    TextButton(
                        onClick = { isExpanded = true },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text("Show more details", style = MaterialTheme.typography.labelSmall)
                    }
                }
                
                // Extended Metadata (when expanded)
                if (isExpanded) {
                    MetadataSection(article = article)
                    TextButton(
                        onClick = { isExpanded = false },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text("Show less", style = MaterialTheme.typography.labelSmall)
                    }
                }
                
                // Date Row (always visible)
                DateSection(
                    pubDate = article.pubDate,
                    dateTimeWritten = article.dateTimeWritten,
                    showFull = isExpanded
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageGallery(images: List<MediaImage>) {
    if (images.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { images.size })
    val scope = rememberCoroutineScope()

    Column {
        // Swipeable Image Pager
        Box(modifier = Modifier.fillMaxWidth()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) { page ->
                AsyncImage(
                    model = images[page].url,
                    contentDescription = images[page].caption ?: images[page].description,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onError = {
                        println("ERROR loading image: ${images[page].url}")
                    }
                )
            }

            // Image counter badge
            if (images.size > 1) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "${pagerState.currentPage + 1}/${images.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Image caption/credit
        val currentImage = images[pagerState.currentPage]
        if (!currentImage.caption.isNullOrEmpty() || !currentImage.credit.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(8.dp)
            ) {
                currentImage.caption?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
                currentImage.credit?.let {
                    Text(
                        text = "📷 $it",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Image navigation dots (clickable with animation)
        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                images.forEachIndexed { index, _ ->
                    Surface(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (index == pagerState.currentPage) 8.dp else 6.dp)
                            .clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = if (index == pagerState.currentPage)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    ) {}
                }
            }
        }
    }
}

@Composable
fun AuthorSection(author: String?, contributor: String?) {
    if (author != null || contributor != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            author?.let {
                Text(
                    text = "By $it",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            contributor?.let {
                Text(
                    text = "Contributing: $it",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun MetadataSection(article: NewsArticle) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Article Details",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        article.dateTimeWritten?.let {
            MetadataRow("Written", formatDate(it))
        }
        
        article.updateDate?.let {
            MetadataRow("Updated", formatDate(it))
        }
        
        article.guid?.let {
            MetadataRow("ID", it.substringAfterLast("/").take(20) + "...")
        }
        
        if (article.mediaImages.isNotEmpty()) {
            MetadataRow("Images", "${article.mediaImages.size} photo(s)")
        }
    }
}

@Composable
fun MetadataRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DateSection(pubDate: String, dateTimeWritten: String?, showFull: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (pubDate.isNotEmpty()) {
            Text(
                text = "📅 ${formatDate(pubDate)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun formatDate(pubDate: String): String {
    // Simple formatting - you can enhance this with proper date parsing
    return try {
        // RSS date format: "Thu, 21 Nov 2024 12:00:00 GMT"
        val parts = pubDate.split(",")
        if (parts.size >= 2) {
            parts[1].trim().substringBeforeLast(":")
        } else {
            pubDate
        }
    } catch (e: Exception) {
        pubDate
    }
}

