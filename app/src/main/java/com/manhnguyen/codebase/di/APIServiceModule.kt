package com.manhnguyen.codebase.di

import com.manhnguyen.codebase.BuildConfig
import com.manhnguyen.codebase.data.api.Api
import com.manhnguyen.codebase.util.JsonUtil.Companion.instance
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

class APIServiceModule {

    companion object {
        val apiModule = module {
            single { Api(get()) }
        }
    }

}