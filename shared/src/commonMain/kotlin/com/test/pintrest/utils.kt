package com.test.pintrest

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.SHA256
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonArray


fun wrapInDataArray(event: JsonObject): JsonObject {
    return buildJsonObject {
        putJsonArray("data") {
            add(event)
        }
    }
}
@OptIn(ExperimentalStdlibApi::class)
fun convertToHash(input: String): String {
    val hashBytes = CryptographyProvider.Default
        .get(SHA256)
        .hasher()
        .hashBlocking(input.encodeToByteArray()).toHexString()

    return hashBytes
}