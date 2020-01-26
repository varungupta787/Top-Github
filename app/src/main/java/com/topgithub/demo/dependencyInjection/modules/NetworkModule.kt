package com.topgithub.demo.dependencyInjection.modules


import com.topgithub.demo.data.network.ApiService
import com.topgithub.demo.dependencyInjection.scope.ApplicationScope

import java.util.concurrent.TimeUnit

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
@Module
class NetworkModule {

    val BASE_URL = "https://github-trending-api.now.sh"

    @Provides
    @ApplicationScope
    fun getGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @ApplicationScope
    fun geRrxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @ApplicationScope
    fun getInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    @ApplicationScope
    fun getApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @ApplicationScope
    fun getRetrofit(
        gsonFactory: GsonConverterFactory, okHttpClient: OkHttpClient,
        rxJavaFactory: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addCallAdapterFactory(rxJavaFactory).addConverterFactory(gsonFactory).build()
    }

    @Provides
    @ApplicationScope
    fun getOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addNetworkInterceptor(interceptor).connectTimeout(
            120,
            TimeUnit.SECONDS
        ).build()
    }
}
