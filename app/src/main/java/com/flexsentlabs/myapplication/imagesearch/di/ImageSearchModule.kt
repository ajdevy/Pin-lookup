package com.flexsentlabs.myapplication.imagesearch.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.flexsentlabs.myapplication.core.database.AppDatabase
import com.flexsentlabs.myapplication.imagesearch.domain.ImageSearchRepository
import com.flexsentlabs.myapplication.imagesearch.domain.SearchImagesUseCase
import com.flexsentlabs.myapplication.imagesearch.BuildConfig
import com.flexsentlabs.myapplication.imagesearch.data.ImageSearchRepositoryImpl
import com.flexsentlabs.myapplication.imagesearch.data.PixabayRemoteMediator
import com.flexsentlabs.myapplication.imagesearch.data.api.PixabayApi
import com.flexsentlabs.myapplication.imagesearch.ui.ImageSearchViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import org.koin.core.parameter.parametersOf
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

val imageSearchModule = module {

    factory { (query: String) ->
        PixabayRemoteMediator(
            database = get(),
            searchUseCase = get(),
            query = query
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    factory { (query: String) ->
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = get<PixabayRemoteMediator> { parametersOf(query) },
            pagingSourceFactory = {
                Timber.d("Creating paging source for query: '$query'")
                val dao = get<AppDatabase>().pixabayImageDao()
                val entityPagingSource = dao.pagingSource(query)
                Timber.d("Entity paging source created for query: '$query'")
                DomainPagingSource(entityPagingSource)
            }
        )
    }

    single { SearchImagesUseCase(get()) }

    single { ImageSearchViewModel(get()) }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.PIXABAY_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .build()
            .create(PixabayApi::class.java)
    }

    single<ImageSearchRepository> { ImageSearchRepositoryImpl(get()) }

    single { SearchImagesUseCase(get<ImageSearchRepository>()) }

    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val apiKeyInterceptor = Interceptor { chain ->
            val original = chain.request()
            val originalUrl = original.url
            val newUrl = originalUrl.newBuilder()
                .addQueryParameter("key", BuildConfig.PIXABAY_API_KEY)
                .build()
            val request = original.newBuilder().url(newUrl).build()
            chain.proceed(request)
        }
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(logging)
            .build()
    }

}