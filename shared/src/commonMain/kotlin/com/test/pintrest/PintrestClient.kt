package com.test.pintrest

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.core.use
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class PintrestClient private constructor(
    val accountId: String = "",
    val bearerToken: String = "",
    val advertisingId: String = "",
) {
    val client: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = bearerToken,
                        refreshToken = ""
                    )
                }
                //this is useful for testing
//                    sendWithoutRequest { request ->
//                        request.url.host == "https://api.pinterest.com/v5/ad_accounts/123456789012/events"
//                    }
            }
        }
    }


    suspend fun sendEvent(body: ConversionEvent): PintrestApiResult {

        val requestBody = wrapInDataArray(body.toJson())
        val response = client.post("https://api.pinterest.com/v5/ad_accounts/$accountId/events") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }
        println("PintrestApiResponse: ${parsePinterestApiResponse(response.bodyAsText())}")
        return parsePinterestApiResponse(response.bodyAsText())
    }

    companion object {
        private var pinterestClient: PintrestClient? = null

        fun initialize(
            accountId: String,
            bearerToken: String,
            advertisingId: String = ""
        ) {
            pinterestClient = PintrestClient(
                accountId = accountId,
                bearerToken = bearerToken,
                advertisingId = advertisingId
            )
        }

        fun getClient(): PintrestClient {
            if(pinterestClient == null) {
                throw IllegalStateException("pinterestClient not initialized")
            }
            return pinterestClient!!
        }

        @OptIn(ExperimentalUuidApi::class)
        fun eventBuilder(eventName: String): ConversionEvent.Builder {
            return ConversionEvent.Builder(
                eventName = eventName,
                eventId = Uuid.random().toHexString(),
                advertisingId = pinterestClient!!.advertisingId
            )
        }
    }
}
