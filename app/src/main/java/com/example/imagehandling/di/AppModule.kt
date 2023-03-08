package com.example.imagehandling.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

/**
 * Created by umair.khalid on 23,September,2021
 **/

@Module
@InstallIn(ActivityComponent::class)
class AppModule {

    @Provides
    fun provideActivityResultRegistry(@ActivityContext activity: Context) =
        (activity as? AppCompatActivity)?.activityResultRegistry
            ?: throw IllegalArgumentException("You must use AppCompatActivity")
}