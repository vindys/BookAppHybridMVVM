package com.example.core.di

import android.app.Application
import androidx.room.Room
import com.example.core.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "app_database").build()
    @Provides
    @Singleton
    fun provideBookDao(database: AppDatabase) = database.bookDao()

}