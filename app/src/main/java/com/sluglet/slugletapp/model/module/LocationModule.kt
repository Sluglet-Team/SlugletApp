package com.sluglet.slugletapp.model.module

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.sluglet.slugletapp.model.service.LocationService
import com.sluglet.slugletapp.model.service.MapService
import com.sluglet.slugletapp.model.service.impl.LocationServiceImpl
import com.sluglet.slugletapp.model.service.impl.MapServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Singleton
    @Provides
    fun provideLocationService (
        @ApplicationContext context: Context
    ) : LocationService = LocationServiceImpl (
        context = context,
        locationClient = LocationServices.getFusedLocationProviderClient(context)
    )

}