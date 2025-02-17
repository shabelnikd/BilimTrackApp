package com.shabelnikd.bilimtrack

import android.app.Application
import android.content.Context
import com.shabelnikd.bilimtrack.model.core.RetrofitClient
import com.shabelnikd.bilimtrack.model.repositories.BilimTrackRepository
import com.shabelnikd.bilimtrack.model.service.BilimTrackApiService
import com.shabelnikd.bilimtrack.utils.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRetrofitClient(context: Application): RetrofitClient {
        return RetrofitClient.getInstance(context)
    }

    @Provides
    fun provideBilimTrackApiService(retrofitClient: RetrofitClient): BilimTrackApiService {
        return retrofitClient.retrofitService
    }

    @Provides
    fun provideBilimTrackRepository(bilimTrackApiService: BilimTrackApiService): BilimTrackRepository {
        return BilimTrackRepository(bilimTrackApiService)
    }
}