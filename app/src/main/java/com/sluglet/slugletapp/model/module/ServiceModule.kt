package com.sluglet.slugletapp.model.module

import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.AccountService
import com.sluglet.slugletapp.model.service.MapService
import com.sluglet.slugletapp.model.service.NavService
import com.sluglet.slugletapp.model.service.StorageService
import com.sluglet.slugletapp.model.service.impl.LogServiceImpl
import com.sluglet.slugletapp.model.service.impl.AccountServiceImpl
import com.sluglet.slugletapp.model.service.impl.MapServiceImpl
import com.sluglet.slugletapp.model.service.impl.NavServiceImpl
import com.sluglet.slugletapp.model.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideLogService(impl: LogServiceImpl): LogService

    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun provideMapService(impl: MapServiceImpl): MapService

    @Binds
    abstract fun provideNavService(impl: NavServiceImpl): NavService
    /* FIXME(CAMDEN): implement configuration service if using remote config from firebase
    @Binds
    abstract fun provideConfigurationService(impl: ConfigurationServiceImpl): ConfigurationService
     */
}