package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitClient private constructor() {
    companion object {
        private lateinit var INSTANCE: Retrofit
        private  var token: String = ""
        private  var personKey: String = ""

        private fun getRetrofitInstance(): Retrofit {
            val httpCliente = OkHttpClient.Builder()

            httpCliente.addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader(TaskConstants.HEADER.TOKEN_KEY, token)
                    .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                    .build()
                chain.proceed(request)
            }

            if (!::INSTANCE.isInitialized) {
                synchronized(RetrofitClient::class) {
                    INSTANCE = Retrofit
                        .Builder()
                        .baseUrl("http://devmasterteam.com/CursoAndroidAPI/")
                        .client(httpCliente.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
            return INSTANCE
        }
        fun <T>getService(serviceClass: Class<T>): T {
            return getRetrofitInstance().create(serviceClass)
        }

        fun addHeaders(token: String, personKey: String) {
            this.token = token
            this.personKey = personKey
        }


    }
}