package com.manhnguyen.codebase.data.api

import android.content.Context
import com.kwabenaberko.newsapilib.NewsApiClient
import com.manhnguyen.codebase.BuildConfig
import com.manhnguyen.codebase.util.JsonUtil
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

class Api constructor(private val context: Context) {
    lateinit var newsApiClient: NewsApiClient

    var imagePosterBaseUrl: String = ""
    var imageBackDropBaseUrl: String = ""
    init {
        initializeUrls()
    }

    private fun initializeUrls() {
        newsApiClient = NewsApiClient(BuildConfig.API_KEY)
    }

    private fun <T> apiBuilder(baseUrl: String, apiClass: Class<T>): T {
        return retrofitInstance(baseUrl).create(apiClass)
    }

    private fun retrofitInstance(baseUrl: String): Retrofit {
        val okHttpClient: OkHttpClient = headersInjectedHTTPClient
        return Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    JsonUtil.instance.gson
                )
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }


    private fun retrofitNoLog(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(JsonUtil.instance.gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(siteInfoUrl)
            .build()
    }

    /**
     * @return HTTP client with headers injected
     */
    private val headersInjectedHTTPClient: OkHttpClient
        get() {
            var builder: OkHttpClient.Builder = OkHttpClient.Builder()
            builder = injectRequestHeadersBuilder(builder)
            if (BuildConfig.DEBUG) {
                builder = injectLoggingBuilder(builder)
            }
            builder = injectUnsafeBuilder(builder)
            builder.interceptors().add(Interceptor { chain -> onOnIntercept(chain) })
            return builder.build()
        }

    private fun onOnIntercept(chain: Interceptor.Chain): Response {
        try {
            return chain.proceed(chain.request().addAuthQuery())
        } catch (exception: SocketTimeoutException) {
            exception.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return chain.proceed(chain.request())
    }

    private fun Request.addAuthQuery(): Request {
        return this.newBuilder().url(
            this.url.newBuilder().addQueryParameter("api_key", "a2c5deb5fdb2ebb10ce53c1fe6b06eca")
                .build()
        ).build()
    }

    private fun injectRequestHeadersBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.addInterceptor { chain ->
            val request: Request = chain.request()
            chain.proceed(request)
        }
        return builder
    }

    private fun injectLoggingBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(loggingInterceptor)
        return builder
    }

    private val unsafeHostnameVerifier: HostnameVerifier
        get() {
            return object : HostnameVerifier {
                override fun verify(
                    hostname: String,
                    session: SSLSession
                ): Boolean {
                    return true
                }
            }
        }

    private fun injectUnsafeBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.hostnameVerifier(unsafeHostnameVerifier)
        val unsafeTrustManager: X509TrustManager =
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    x509Certificates: Array<X509Certificate>,
                    s: String
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    x509Certificates: Array<X509Certificate>,
                    s: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            }
        val trustAllCerts: Array<TrustManager> =
            arrayOf(unsafeTrustManager)
        var sslContext: SSLContext? = null
        try {
            sslContext = SSLContext.getInstance("SSL")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        try {
            assert(sslContext != null)
            sslContext!!.init(null, trustAllCerts, SecureRandom())
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory: SSLSocketFactory = sslContext!!.socketFactory
        builder.sslSocketFactory(sslSocketFactory, unsafeTrustManager)
        return builder
    }

    private val siteInfoUrl: String
        get() {
            return "https://api.github.com"
        }

}