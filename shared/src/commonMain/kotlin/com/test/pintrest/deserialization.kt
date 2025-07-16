package com.test.pintrest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

@Serializable
data class PinterestEvent(
    val status: String,
    @SerialName("error_message") val errorMessage: String? = null,
    @SerialName("warning_message") val warningMessage: String? = null,
)

@Serializable
data class PinterestResponse(
    @SerialName("num_events_received")
    val numEventsReceived: Int,
    @SerialName("num_events_processed")
    val numEventsProcessed: Int,
    val events: List<PinterestEvent>
)

@Serializable
data class GenericHttpResponse(
    val code: Int? = null,
    val message: String? = null,
    val details: PinterestResponse? = null
)

sealed class PintrestApiResult{
    data class successResult(val pintrestSucessResponse: PinterestResponse) : PintrestApiResult()
    data class failureResult(val code: Int?, val message: String?, val pintrestResponse: PinterestResponse?) : PintrestApiResult()
}

fun parsePinterestApiResponse(json: String): PintrestApiResult {
    val jsonParser = Json { ignoreUnknownKeys = true }

    return try {
        val success = jsonParser.decodeFromString<PinterestResponse>(json)
        PintrestApiResult.successResult(success)
    } catch (e: SerializationException) {
        // Parsing as success failed, try parsing as error
        try {
            val error = jsonParser.decodeFromString<GenericHttpResponse>(json)
            PintrestApiResult.failureResult(error.code, error.message, error.details)
        } catch (inner: Exception) {
            // Really malformed — throw or handle as unexpected
            throw IllegalArgumentException("Unrecognized Pinterest response", inner)
        }
    }
}