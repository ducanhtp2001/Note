package com.example.note.Data.provider

import com.example.note.Data.api_service.CommonApiService
import com.example.note.Data.repository.impliment.CommonRepositoryImpl
import com.example.note.Data.repository.repo.CommonRepository
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