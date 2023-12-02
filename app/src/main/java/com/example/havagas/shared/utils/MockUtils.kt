package com.example.havagas.shared.utils

import android.content.Context
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type

class MockUtils(private val context: Context) {

    fun <T> getMockFromAsset(fileName: String, type: Class<T>): T? {
        val json: String? = loadJsonFromAsset("mocks/$fileName")
        return if (json != null) {
            Gson().fromJson(json, type)
        } else {
            null
        }
    }

    private fun loadJsonFromAsset(fileName: String): String? {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}