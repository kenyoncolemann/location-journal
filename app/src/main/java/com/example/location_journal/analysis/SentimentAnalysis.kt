package com.example.location_journal.analysis

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

object SentimentAnalyzer {

    private const val BASE_URL = "https://api-inference.huggingface.co/"
    private const val MODEL = "j-hartmann/emotion-english-distilroberta-base"
    private const val API_TOKEN = "Bearer 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"

    private val api: HuggingFaceApi by lazy {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", API_TOKEN)
                .build()
            chain.proceed(request)
        }.build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HuggingFaceApi::class.java)
    }

    suspend fun analyze(text: String): String = withContext(Dispatchers.IO) {
        try {
            val response = api.analyzeSentiment(mapOf("inputs" to text))
            val topLabel = response.maxByOrNull { it.score }?.label ?: return@withContext "Calm"
            return@withContext mapToSixMoods(topLabel)
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext "Calm"
        }
    }

    fun mapToSixMoods(rawSentiment: String): String {
        return when (rawSentiment.lowercase()) {
            "joy", "happiness", "excited" -> "Happy"
            "sadness", "depressed", "down" -> "Sad"
            "anger", "mad", "annoyed", "frustrated" -> "Angry"
            "surprise", "shocked" -> "Surprised"
            "fear", "anxious", "nervous", "worried", "tense" -> "Stressed"
            "calm", "relaxed", "neutral", "content" -> "Calm"
            else -> "Calm" // fallback if unmapped
        }
    }

    interface HuggingFaceApi {
        @POST("models/$MODEL")
        @Headers("Content-Type: application/json")
        suspend fun analyzeSentiment(@Body body: Map<String, String>): List<SentimentResult>
    }

    data class SentimentResult(
        @SerializedName("label") val label: String,
        @SerializedName("score") val score: Float
    )
}