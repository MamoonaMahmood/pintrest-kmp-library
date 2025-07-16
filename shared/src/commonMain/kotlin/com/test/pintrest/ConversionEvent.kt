package com.test.pintrest

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonArray

@Serializable
class ConversionEvent(
    val eventName: String,
    val eventId: String,
    val advertisingId: String,
    val eventTime: Int,
    val actionSource: String,
    val userJson: JsonObject?,
    val customData: JsonObject?,
    val language: String,
    val setUserCalled: Boolean = false
) {


    private constructor(builder: Builder) : this(
        eventName = builder.eventName,
        eventId = builder.eventId,
        advertisingId =  builder.advertisingId,
        actionSource = builder.actionSource,
        eventTime = builder.eventTime,
        userJson = builder.userJson,
        customData = builder.customData,
        language = builder.language,
    )

    class Builder( val eventName: String, val eventId: String, val advertisingId: String) {
        var eventTime: Int = 0
        var actionSource: String = ""
        var userJson: JsonObject? = null
        var customData: JsonObject? = null
        var customDataAvailable: Boolean = false
        var language: String = ""
        var setUserCalled: Boolean = false

        fun build(): ConversionEvent {
            return ConversionEvent(
                eventName = eventName,
                eventId =  eventId,
                advertisingId =  advertisingId,
                eventTime = eventTime,
                actionSource = actionSource,
                userJson = userJson,
                customData =  customData,
                language = language,
                setUserCalled =  setUserCalled
            )
        }
        fun setActionSource(actionSource: String) = apply { this.actionSource = actionSource }

        fun setUserData(userData: UserData): Builder {
            setUserCalled = true
            this.userJson = userData.toJson()
            return this
        }
        fun setCustomData(customData: CustomData): Builder {
            this.customData = customData.toJson()
            return this
        }
        fun setTimestamp(timestamp: Instant): Builder {
            this.eventTime = timestamp.epochSeconds.toInt()
            val unixTimestampSeconds = timestamp.epochSeconds
            return this
        }

        fun setLanguage(language: String): Builder {
            this.language = language
            return this
        }

    }

    fun toJson(): JsonObject {
        return buildJsonObject {
            put(EventConstants.EventParams.EVENT_NAME, JsonPrimitive(eventName))
            put(EventConstants.EventParams.EVENT_ID, JsonPrimitive(eventId))
            put(EventConstants.EventParams.ACTION_SOURCE, JsonPrimitive(actionSource))
            put(EventConstants.EventParams.EVENT_TIME, JsonPrimitive(eventTime))
            if (!setUserCalled) { //no user is logged in, we gave these credentials at initialization
                val newJsonObj = buildJsonObject {
                    userJson?.forEach { (key, value) ->
                        put(key, value)
                    }
                    //not provided at the time of creation, specifically for ios,
                    //this parameter would be set by user builder
                    if (advertisingId != "") {
                        val hashedVal = convertToHash(advertisingId)
                        putJsonArray(EventConstants.UserEventParams.USER_HASHED_ID) {
                            add(JsonPrimitive(hashedVal))
                        }
                    }
                }
                put(EventConstants.EventParams.EVENT_USER_DATA, newJsonObj)
            }
            else{
                userJson?.let {
                    put(EventConstants.EventParams.EVENT_USER_DATA, it)
                }
            }
            customData?.let { put(EventConstants.EventParams.EVENT_CUSTOM_DATA, it) }
        }
    }
}