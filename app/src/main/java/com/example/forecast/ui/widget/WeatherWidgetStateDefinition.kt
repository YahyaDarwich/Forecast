package com.example.forecast.ui.widget

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.io.OutputStream

object WeatherWidgetStateDefinition : GlanceStateDefinition<WeatherWidgetState> {
    private const val DATA_STORE_FILE_NAME = "weather widget state"
    private val Context.dataStore by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = WeatherWidgetSerializer
    )

    override suspend fun getDataStore(
        context: Context,
        fileKey: String
    ): DataStore<WeatherWidgetState> {
        return context.dataStore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return context.dataStoreFile(DATA_STORE_FILE_NAME)
    }
}

object WeatherWidgetSerializer : Serializer<WeatherWidgetState> {
    override val defaultValue: WeatherWidgetState
        get() = WeatherWidgetState.Error("unavailable")

    override suspend fun readFrom(input: InputStream): WeatherWidgetState = try {
        Json.decodeFromString(
            WeatherWidgetState.serializer(),
            input.readBytes().decodeToString()
        )
    } catch (exception: SerializationException) {
        throw CorruptionException("Could not read weather data: ${exception.message}")
    }

    override suspend fun writeTo(t: WeatherWidgetState, output: OutputStream) {
        output.use {
            it.write(
                Json.encodeToString(WeatherWidgetState.serializer(), t).encodeToByteArray()
            )
        }
    }

}