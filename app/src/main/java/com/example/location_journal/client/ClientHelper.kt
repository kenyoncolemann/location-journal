import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject

suspend fun requestHeatmapFromServer(json: String): Bitmap = withContext(Dispatchers.IO) {
    val client = OkHttpClient()

    try {
        Log.d("HeatmapClient", "JSON: $json")
        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            json
        )

        val request = Request.Builder()
            .url("http://10.0.2.2:5050/heatmap")  // Flask default
            .post(requestBody)
            .build()

        Log.d("HeatmapClient", "Sending request to Flask server...")

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            Log.e("HeatmapClient", "Error: ${response.code} ${response.message}")
            throw Exception("Request failed with code ${response.code}")
        }

        val inputStream = response.body?.byteStream()
        if (inputStream != null) {
            return@withContext BitmapFactory.decodeStream(inputStream)
        } else {
            throw Exception("Empty response body")
        }

    } catch (e: Exception) {
        Log.e("HeatmapClient", "Exception while requesting heatmap", e)
        throw e
    }
}

suspend fun requestAbsaMoodList(journalText: String): List<Double> = withContext(Dispatchers.IO) {
    val client = OkHttpClient()

    try {
        val payload = JSONObject().apply {
            put("text", journalText)
        }

        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            payload.toString()
        )

        val request = Request.Builder()
            .url("http://10.0.2.2:5050/absa")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw Exception("Request failed with code ${response.code}")
        }

        val responseString = response.body?.string()
            ?: throw Exception("Empty response body")

        val json = JSONObject(responseString)
        val dataArray = json.getJSONArray("Data")

        return@withContext List(dataArray.length()) { i ->
            dataArray.getDouble(i)
        }

    } catch (e: Exception) {
        Log.e("AbsaClient", "Error requesting ABSA", e)
        throw e
    }
}
