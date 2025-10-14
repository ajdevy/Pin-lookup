package com.flexsentlabs.myapplication.core.database.di

import android.content.Context
import androidx.room.Room
import com.flexsentlabs.myapplication.core.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(androidContext()) }
    single { get<AppDatabase>().pixabayImageDao() }
    single { get<AppDatabase>().pixabayRemoteKeysDao() }
}

fun provideDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "pixabay_database"
    ).build()
}
