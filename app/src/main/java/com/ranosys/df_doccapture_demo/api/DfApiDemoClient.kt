package com.ranosys.df_doccapture_demo.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ranosys.df_doccapture_demo.BuildConfig
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Class Used for getting the retrofit instance for API calling
 * @author Ranosys Technologies
 * @Date 24-Dec-2018
 */
internal class DfApiDemoClient {

    companion object {

        var retrofit: Retrofit? = null
            get() {
                field = field ?: Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson!!))
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client!!)
                    .build()
                return field
            }

        private var gson: Gson? = null
            get() {
                field = field ?: GsonBuilder().create()
                return field
            }

        private var interceptor = HttpLoggingInterceptor()
            get() {
                if (BuildConfig.DEBUG)
                    field.level = HttpLoggingInterceptor.Level.BODY
                else
                    field.level = HttpLoggingInterceptor.Level.NONE
                return field
            }

        private var client: OkHttpClient? = null
            get() {
                val dispatcher = Dispatcher()
                dispatcher.maxRequests = 1
                field = field ?: OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .dispatcher(dispatcher)
                    .build()
                return field
            }

    }
}