package com.test.pintrest

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

@Serializable
class UserData(
    val identifierIds: MutableList<String>, //required field + user_data is a required parameter
    val externalId: String, //optional(userId + also hashed)
    val clickId: String, //optional
    val emailList: MutableList<String>,
) {

    private constructor(builder: Builder) : this(
        identifierIds = builder.identifierIds,
        externalId = builder.externalId,
        clickId = builder.clickId,
        emailList = builder.emailList,
    )

    class Builder {
        var identifierIds: MutableList<String> = mutableListOf()
        var externalId: String = ""
        var clickId: String = ""
        var emailList: MutableList<String> = mutableListOf()



        fun setIdentifierIds(identifierIds: List<String>) = apply {
            this.identifierIds.clear()
            this.identifierIds.addAll(identifierIds)
        }

        fun setIdentifierId(identifierId: String) = apply {
            this.identifierIds.add(identifierId)
        }

        fun setEmail (email: String) = apply {
            this.emailList.add(email)
        }

        fun setEmaiList (email: MutableList<String>) =  apply {
            this.emailList.clear()
            this.emailList.addAll(email)
        }
        fun setExternalId(externalId: String) = apply {
            this.externalId = externalId
        }

        fun setClickId(clickId: String) = apply {
            this.clickId = clickId
        }


        fun build(): UserData{
            return UserData(
                identifierIds = identifierIds,
                externalId = externalId,
                clickId = clickId,
                emailList = emailList
            )
        }
    }

    fun toJson(): JsonObject{

        return buildJsonObject {
            if (identifierIds.isNotEmpty()) {
                val hashedIdentifiers = identifierIds.map {
                    JsonPrimitive(convertToHash(it))
                }
                put(EventConstants.UserEventParams.USER_HASHED_ID, JsonArray(hashedIdentifiers))
            }
            if (externalId != "") {
                val hashedExternalId = JsonPrimitive(convertToHash(externalId))
                put(EventConstants.UserEventParams.USER_EXTERNAL_ID, hashedExternalId)
            }
            if (clickId != "") {
                val hashedClickId = JsonPrimitive(convertToHash(clickId))
                put(EventConstants.UserEventParams.USER_CLICK_ID, hashedClickId)
            }
            if (emailList.isNotEmpty()) {
                val hashedEmails = emailList.map {
                    JsonPrimitive(convertToHash(it))
                }
                put(EventConstants.UserEventParams.USER_EMAIL, JsonArray(hashedEmails))
            }

        }
    }

}