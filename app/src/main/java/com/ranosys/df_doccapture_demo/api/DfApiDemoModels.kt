package com.ranosys.df_doccapture_demo.api

/**
 * @Details
 * @Author Ranosys Technologies
 * @Date 24-Jan-2019
 */
data class LoginRequestModel(
    val email: String,
    val password: String
)

data class LoginResponseModel(
    val username: String,
    val country_code: String,
    val phone_number: String,
    val access_token: String,
    val date_joined: String,
    val name: String
)