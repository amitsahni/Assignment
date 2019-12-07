package com.assignment.data

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.assignment.BuildConfig
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber
import java.net.URLDecoder
import java.text.DateFormat
import java.util.concurrent.TimeUnit


object RetrofitManager {

    val success = MutableLiveData<String>()
    val error = MutableLiveData<String>()
    val failure = MutableLiveData<String>()
    private val LINE_SEPARATOR = System.getProperty("line.separator")!!

    private val isDebug = BuildConfig.DEBUG

    private val gson = GsonBuilder()
        .enableComplexMapKeySerialization()
        .serializeNulls()
        .setDateFormat(DateFormat.LONG)
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setPrettyPrinting()
        .create()

    private val factory = GsonConverterFactory.create(gson)


    private val loggerInterceptor = LoggerInterceptor()
    private val globalHandlerInterceptor = GlobalHandler()
    private val ioInterceptor = IO()


    val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(10.toLong(), TimeUnit.SECONDS)
        .readTimeout(30.toLong(), TimeUnit.SECONDS)
        .writeTimeout(30.toLong(), TimeUnit.SECONDS)
        .addInterceptor(UserAgentInterceptor())
        .addInterceptor(globalHandlerInterceptor)
        .addInterceptor(loggerInterceptor)
        .addInterceptor(ioInterceptor)
        .build()

    fun retrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(factory)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    fun OkHttpClient.Builder.headers(headers: Map<String, String> = emptyMap()): OkHttpClient.Builder {
        if (headers.isNotEmpty()) {
            addInterceptor {
                val builder = it.request().newBuilder()
                headers.forEach { (key, value) ->
                    builder.addHeader(key, value)
                }
                it.proceed(builder.build())
            }
        }
        return this
    }

    private class UserAgentInterceptor : Interceptor {

        private val userAgent: String by lazy {
            buildUserAgent()
        }

        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            builder.header("User-Agent", userAgent)
            return chain.proceed(builder.build())
        }

        private fun buildUserAgent(): String {
            val versionName = BuildConfig.VERSION_NAME
            val versionCode = BuildConfig.VERSION_CODE
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            val version = Build.VERSION.SDK_INT
            val versionRelease = Build.VERSION.RELEASE
            val installerName = BuildConfig.APPLICATION_ID
            return "$installerName / $versionName($versionCode); ($manufacturer; $model; SDK $version; Android $versionRelease)"
        }
    }

    private class LoggerInterceptor : Interceptor {

        @SuppressLint("TimberArgCount")
        override fun intercept(it: Interceptor.Chain): Response {
            if (!isDebug) {
                return it.proceed(it.request())
            }
            Timber.d(

                "╔════ Request ════════════════════════════════════════════════════════════════════════════"
            )
            it.request().let {
                Timber.d("║ URL: ${it.url()}")
                Timber.d("║ URL Decode:")
                Log.d(
                    RetrofitManager::class.java.name,
                    URLDecoder.decode(it.url().toString(), "UTF-8")
                )
                URLDecoder.decode(it.url().toString(), "UTF-8").split(LINE_SEPARATOR.toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                    .forEach {
                        Timber.d("║ $it")
                    }
                Timber.d("║ ")
                Timber.d("║ Method: @${it.method()}")
                if (it.headers().size() > 0) {
                    Timber.d("║ ")
                    Timber.d("║ Headers: ")
                }
                for (i in 0 until it.headers().size()) {
                    Timber.d("║   ─ ${it.headers().name(i)} : ${it.headers().value(i)}")
                }
                it.body()?.let { body ->
                    if (body !is MultipartBody) {
                        Timber.d("║ ")
                        Timber.d("║ Body:")
                        val buffer = okio.Buffer()
                        body.writeTo(buffer)
                        buffer.readUtf8().split(LINE_SEPARATOR.toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                            .forEach {
                                try {
                                    Timber.d("║ ${URLDecoder.decode(it, "UTF-8")}")
                                } catch (e: java.lang.Exception) {
                                    Timber.e(e)
                                }
                            }
                    }

                }
            }
            Timber.d(

                "╚═══════════════════════════════════════════════════════════════════════════════════════"
            )
            val startNs = System.nanoTime()
            val response = it.proceed(it.request())
            val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
            val source = response.body()?.source()
            source?.request(Long.MAX_VALUE)
            source?.buffer()?.clone()?.readUtf8()?.apply {
                Timber.d(

                    "╔════ Response ═══════════════════════════════════════════════════════════════════════════"
                )
                Timber.d("║ URL: ${response.request().url()}")
                Timber.d("║ ")
                Timber.d(

                    "║ Status Code: ${response.code()} / ${response.message()} - $tookMs ms"
                )
                val size = response.headers().size() ?: 0
                if (size > 0) {
                    Timber.d("║ ")
                    Timber.d("║ Headers: ")
                }
                val headers = response.headers()
                for (i in 0 until size) {
                    Timber.d("║   ─ ${headers.name(i)} : ${headers.value(i)}")
                }
                Timber.d("║ ")
                Timber.d("║ ")
                try {
                    if (this.startsWith("{")) {
                        JSONObject(this).toString(1)?.let {
                            val lines =
                                it.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()
                            for (line in lines) {
                                Timber.d("║ $line")
                            }
                            // Log.d("", it)

                        }
                    } else {
                        JSONArray(this).toString(1)?.let {
                            val lines =
                                it.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()
                            lines.forEach {
                                Timber.d("║ $it")
                            }
                            //Log.d("", it)
                        }
                    }
                    Timber.d("║ $this")
                } catch (e: Exception) {
                    Timber.e("║ ${e.message}")
                }
                Timber.d(

                    "╚═══════════════════════════════════════════════════════════════════════════════════════"
                )
            }
            return response
        }

    }

    private class GlobalHandler : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder().build()
            val response = chain.proceed(request)
            if (response.isSuccessful) {
                val source = response.body()?.source()
                source?.request(java.lang.Long.MAX_VALUE)
                source?.buffer()?.clone()?.readUtf8()?.apply {
                    success.postValue(this)
                }
            } else {
                val source = response.body()?.source()
                source?.request(Long.MAX_VALUE)
                source?.buffer()?.clone()?.readUtf8()?.apply {
                    error.postValue(this)
                }
            }
            return response
        }

    }

    private class IO : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            try {
                val request = chain.request().newBuilder().build()
                return chain.proceed(request)
            } catch (e: Exception) {
                failure.postValue(e.toString())
            }
            return chain.proceed(chain.request())
        }

    }
}