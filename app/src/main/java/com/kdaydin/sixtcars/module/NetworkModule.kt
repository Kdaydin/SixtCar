package com.kdaydin.sixtcars.module

import android.content.Context
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kdaydin.sixtcars.application.ApplicationConstants
import com.kdaydin.sixtcars.data.remote.HttpStatusCodeInterceptor
import com.kdaydin.sixtcars.data.remote.SixtApi
import com.kdaydin.sixtcars.data.repository.SixtRepository
import com.kdaydin.sixtcars.data.repository.SixtRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        createWebService<SixtApi>(
            okHttpClient = createHttpClient(androidContext()),
            factory = RxJava2CallAdapterFactory.create(),
            baseUrl = ApplicationConstants.BASE_URL
        )
    }
    factory<SixtRepository> { SixtRepositoryImpl(sixtApi = get()) }
}

/* Returns a custom OkHttpClient instance with interceptor. Used for building Retrofit service */
fun createHttpClient(context: Context): OkHttpClient {
    val client = OkHttpClient.Builder()
    client.readTimeout(5 * 60, TimeUnit.SECONDS)
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    client.addInterceptor {
        val original = it.request()
        val requestBuilder = original.newBuilder()
        requestBuilder.header("Content-Type", "application/json")
        requestBuilder.header("Accept", "application/json")
        val request = requestBuilder.method(original.method, original.body).build()
        return@addInterceptor it.proceed(request)
    }
    client.addInterceptor(HttpStatusCodeInterceptor())
    client.addInterceptor(logging)
    return client.build()
}

/* function to build our Retrofit service */
inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    factory: CallAdapter.Factory,
    baseUrl: String
): T {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().serializeNulls()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT).create()
            )
        )
        .addCallAdapterFactory(CoroutineCallAdapterFactory()).addCallAdapterFactory(factory)
        .client(okHttpClient).build()
    return retrofit.create(T::class.java)
}