package com.example.rssexample.di

import com.example.rssexample.data.repository.RssRepositoryImpl
import com.example.rssexample.domain.repository.RssRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRssRepository(
        rssRepositoryImpl: RssRepositoryImpl
    ): RssRepository
}

