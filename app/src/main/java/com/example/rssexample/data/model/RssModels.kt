package com.example.rssexample.data.model

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "rss")
data class RssFeed(
    @Element
    val channel: RssChannel? = null
)

@Xml(name = "channel")
data class RssChannel(
    @PropertyElement
    val title: String? = null,

    @PropertyElement
    val link: String? = null,

    @PropertyElement
    val description: String? = null,

    @PropertyElement
    val language: String? = null,

    @PropertyElement
    val lastBuildDate: String? = null,

    @Element(name = "item")
    val items: List<RssItem>? = null
)

@Xml(name = "item")
data class RssItem(
    @PropertyElement
    val title: String? = null,

    @PropertyElement
    val link: String? = null,

    @PropertyElement
    val description: String? = null,

    @PropertyElement
    val pubDate: String? = null,

    @PropertyElement
    val guid: String? = null,

    // Dublin Core namespace (dc:)
    @PropertyElement(name = "dc:creator")
    val creator: String? = null,

    @PropertyElement(name = "dc:contributor")
    val contributor: String? = null,

    // Custom elements (no namespace)
    @PropertyElement
    val dateTimeWritten: String? = null,

    @PropertyElement
    val updateDate: String? = null,

    @PropertyElement
    val expires: String? = null,

    // Media RSS namespace (media:)
    @Element(name = "media:thumbnail")
    val thumbnail: MediaThumbnail? = null,

    @Element(name = "media:content")
    val content: List<MediaContent>? = null,

    // Standard enclosure (no namespace)
    @Element
    val enclosure: Enclosure? = null
)

// media:thumbnail - self-closing with attributes only
@Xml(name = "media:thumbnail")
data class MediaThumbnail(
    @Attribute
    val url: String? = null,

    @Attribute
    val height: String? = null,

    @Attribute
    val width: String? = null
)

// media:content - has attributes and child elements
@Xml(name = "media:content")
data class MediaContent(
    @Attribute
    val url: String? = null,

    @Attribute
    val type: String? = null,

    @Attribute
    val medium: String? = null,

    @Attribute
    val height: String? = null,

    @Attribute
    val width: String? = null,

    @Element(name = "media:credit")
    val credit: MediaCredit? = null,

    @Element(name = "media:text")
    val text: MediaText? = null,

    @Element(name = "media:description")
    val description: MediaDescription? = null,

    @Element(name = "media:thumbnail")
    val thumbnail: MediaThumbnail? = null
)

// media:credit - simple text content
@Xml(name = "media:credit")
data class MediaCredit(
    @TextContent
    val value: String? = null
)

// media:text - has type attribute and text content
@Xml(name = "media:text")
data class MediaText(
    @Attribute
    val type: String? = null,

    @TextContent
    val value: String? = null
)

// media:description - has type attribute and text content
@Xml(name = "media:description")
data class MediaDescription(
    @Attribute
    val type: String? = null,

    @TextContent
    val value: String? = null
)

// Standard RSS enclosure (no namespace)
@Xml(name = "enclosure")
data class Enclosure(
    @Attribute
    val url: String? = null,

    @Attribute
    val type: String? = null,

    @Attribute
    val length: String? = null
)