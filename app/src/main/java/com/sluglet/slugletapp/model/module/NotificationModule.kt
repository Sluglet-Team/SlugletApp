package com.sluglet.slugletapp.model.module

import android.content.BroadcastReceiver
import android.content.Context
import com.sluglet.slugletapp.model.service.NotificationService
import com.sluglet.slugletapp.model.service.impl.NotificationServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Singleton
    @Provides
    fun provideNotificationService (
        @ApplicationContext context: Context,
    ) : NotificationService = NotificationServiceImpl (
        context = context,
    )
}