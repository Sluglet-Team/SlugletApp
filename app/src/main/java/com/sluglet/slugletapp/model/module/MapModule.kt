package com.sluglet.slugletapp.model.module

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.sluglet.slugletapp.model.service.MapService
import com.sluglet.slugletapp.model.service.impl.MapServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModule {
    @Singleton
    @Provides
    fun provideMapClient (
        @ApplicationContext context: Context
    ) : MapService = MapServiceImpl (
        context = context,
        locationClient = LocationServices.getFusedLocationProviderClient(context)
    )

}