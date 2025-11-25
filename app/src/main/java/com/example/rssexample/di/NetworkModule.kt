package com.example.rssexample.di

import com.example.rssexample.data.remote.RssApiService
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideTikXml(): TikXml {
        return TikXml.Builder()
            .exceptionOnUnreadXml(false)
            .addTypeConverter(String::class.java, object : com.tickaroo.tikxml.TypeConverter<String> {
                override fun read(value: String?): String {
                    return value?.trim() ?: ""
                }

                override fun write(value: String?): String {
                    return value ?: ""
                }
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        tikXml: TikXml
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://feeds.nbcnews.com/")
            .client(okHttpClient)
            .addConverterFactory(TikXmlConverterFactory.create(tikXml))
            .build()
    }

    @Provides
    @Singleton
    fun provideRssApiService(retrofit: Retrofit): RssApiService {
        return retrofit.create(RssApiService::class.java)
    }
}

