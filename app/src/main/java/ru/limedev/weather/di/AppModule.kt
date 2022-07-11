package ru.limedev.weather.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.limedev.weather.data.db.WeatherDb
import ru.limedev.weather.data.network.RestClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRestClient(): RestClient = RestClient

    @Singleton
    @Provides
    fun provideWeatherDb(
        @ApplicationContext appContext: Context
    ) = Room.databaseBuilder(appContext, WeatherDb::class.java, "weather_db").build()

    @Singleton
    @Provides
    fun provideWeatherDbDao(db: WeatherDb) = db.getWeatherDbDao()
}