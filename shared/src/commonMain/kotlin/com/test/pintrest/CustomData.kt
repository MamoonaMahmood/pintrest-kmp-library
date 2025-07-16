package com.test.pintrest

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

@Serializable
class CustomData(
    val currency: String?,
    val value: String?,
    val contentIds: MutableList<String>,
    val searchString: String?,
    val contentName: String?,
    val contentCategory: String?,
    val contentBrand: String?,
    val orderId: String?,
) {
    private constructor(builder: Builder) : this(
        currency = builder.currency,
        value = builder.value,
        contentIds = builder.contentIds,
        searchString = builder.searchString,
        contentName = builder.contentName,
        contentCategory = builder.contentCategory,
        contentBrand = builder.contentBrand,
        orderId = builder.orderId,
    )

    class Builder {
        var currency: String? = null
        var value: String? = null
        var contentIds: MutableList<String> = mutableListOf()
        var searchString: String? = null
        var contentName: String? = null
        var contentCategory: String? = null
        var contentBrand: String? = null
        var orderId: String? = null

        fun setCurrency(currency: String) = apply { this.currency = currency }
        fun setValue(value: String) = apply { this.value = value }
        fun setContentIds(someValue: String) = apply { contentIds.add(someValue) }
        fun setContentName(name: String) = apply { contentName = name }
        fun setContentCategory(categoryName: String) = apply { contentCategory = categoryName }
        fun setContentBrand(brandName: String) = apply { contentBrand = brandName }
        fun setOrderId(orderId: String) = apply { this.orderId = orderId }
        fun setContentIds(contentIds: List<String>) = apply {
            this.contentIds.clear()
            this.contentIds.addAll(contentIds)
        }
        fun setContentId(contentId: String) = apply {
            this.contentIds.add(contentId)
        }
        fun build(): CustomData {
            return CustomData(
                currency = currency,
                value = value,
                searchString = searchString,
                contentName = contentName,
                contentCategory = contentCategory,
                contentBrand = contentBrand,
                orderId = orderId,
                contentIds = contentIds
            )
        }
    }

    fun toJson(): JsonObject {

        return buildJsonObject {
            if (currency != null) put(EventConstants.CustomEventParams.CUSTOM_CURRENCY, JsonPrimitive(currency))
            if (value != null) put(EventConstants.CustomEventParams.CUSTOM_VALUE, JsonPrimitive(value))
            if (searchString != null) put(EventConstants.CustomEventParams.CUSTOM_SEARCH, JsonPrimitive(searchString))
            if (contentName != null) put(EventConstants.CustomEventParams.CUSTOM_CONTENT_NAME, JsonPrimitive(contentName))
            if (contentCategory != null) put(EventConstants.CustomEventParams.CUSTOM_CONTENT_CATEGORY, JsonPrimitive(contentCategory))
            if (contentBrand != null) put(EventConstants.CustomEventParams.CUSTOM_CONTENT_BRAND, JsonPrimitive(contentBrand))
            if (orderId != null) put(EventConstants.CustomEventParams.CUSTOM_ORDER_ID, JsonPrimitive(orderId))
            if (contentIds.isNotEmpty()) put(EventConstants.CustomEventParams.CUSTOM_CONTENT_IDS, JsonArray(contentIds.map { JsonPrimitive(it) }))
        }
    }
}