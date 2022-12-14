package com.metropolia.eatthefrog.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.net.URL

/**
 * Provides a Service, which contains the functionality for making queries to the API.
 * @see <a href="https://premium.zenquotes.io/zenquotes-documentation/">ZenQuotes Documentation</a>
 */
object APIService {

    data class Result(val q: String, val a: String, val h: String)

    var quote = MutableLiveData(Result("The greatest glory in living lies not in never falling, but in rising every time we fall.", "Nelson Mandela", ""))

    private val baseURL = URL("https://zenquotes.io/api/")

    interface Service {
        @GET("random/")
        suspend fun getRandomMotivationalQuote(): List<Result>
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: Service = retrofit.create(Service::class.java)

    init {
        MainScope().launch {
            Log.d("API called", "called")
            quote.value = service.getRandomMotivationalQuote()[0]
        }
    }
}