package com.example.note.data.provider

import com.example.note.data.api_service.CommonApiService
import com.example.note.data.repository.impliment.CommonRepositoryImpl
import com.example.note.data.repository.repo.CommonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideCommonRepository(apiService: CommonApiService): CommonRepository {
        return CommonRepositoryImpl(apiService)
    }
}