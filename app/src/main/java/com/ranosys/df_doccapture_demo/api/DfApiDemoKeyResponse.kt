package com.ranosys.df_doccapture_demo.api

import com.google.gson.JsonObject

data class VerifyApiKeyResponse(
    val message: String
)

internal class ErrorResponse {

    var status: String? = null
    var message: String? = null
    val errors: JsonObject? = null
}