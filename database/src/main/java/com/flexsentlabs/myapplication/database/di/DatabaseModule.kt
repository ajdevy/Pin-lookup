package com.flexsentlabs.myapplication.database.di

import com.flexsentlabs.myapplication.database.pixabay.PixabayDatabase
import com.flexsentlabs.myapplication.database.pixabay.PixabayImageDao
import com.flexsentlabs.myapplication.database.pixabay.PixabayRemoteKeysDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<PixabayDatabase> {
        PixabayDatabase.create(androidContext())
    }
    
    single<PixabayImageDao> {
        get<PixabayDatabase>().pixabayImageDao()
    }
    
    single<PixabayRemoteKeysDao> {
        get<PixabayDatabase>().pixabayRemoteKeysDao()
    }
}
