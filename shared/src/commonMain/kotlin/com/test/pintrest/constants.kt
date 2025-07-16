package com.test.pintrest

object EventConstants {
    object EventParams {
        const val EVENT_NAME = "event_name"
        const val ACTION_SOURCE = "action_source"
        const val EVENT_TIME = "event_time"
        const val EVENT_ID = "event_id"
        const val EVENT_USER_DATA = "user_data"
        const val EVENT_CUSTOM_DATA = "custom_data"
        const val EVENT_DATA = "data"
        const val WIFI = "wifi"
        const val APP_ID = "app_id"
        const val APP_NAME = "app_name"
        const val OS_VERSION = "os_version"
    }
    object UserEventParams {
        const val USER_HASHED_ID = "hashed_maids"
        const val USER_EMAIL = "em"
        const val USER_EXTERNAL_ID = "external_id"
        const val USER_CLICK_ID = "click_id"
    }
    object CustomEventParams {
        const val CUSTOM_CURRENCY = "currency"
        const val CUSTOM_VALUE = "value"
        const val CUSTOM_CONTENT_NAME = "content_name"
        const val CUSTOM_CONTENT_CATEGORY = "content_category"
        const val CUSTOM_CONTENT_BRAND = "content_brand"
        const val CUSTOM_ORDER_ID = "order_id"
        const val CUSTOM_CONTENT_IDS = "content_ids"
        const val CUSTOM_SEARCH = "search_string"
    }

}