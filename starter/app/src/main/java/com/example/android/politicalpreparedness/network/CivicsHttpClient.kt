package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.Constants
import okhttp3.OkHttpClient

class CivicsHttpClient: OkHttpClient() {

    companion object {

        fun getClient(): OkHttpClient {
            return Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val url = original
                                .url()
                                .newBuilder()
                                .addQueryParameter("key", Constants.API_KEY)
                                .build()
                        val request = original
                                .newBuilder()
                                .url(url)
                                .build()
                        chain.proceed(request)
                    }
                    .build()
        }
    }
}