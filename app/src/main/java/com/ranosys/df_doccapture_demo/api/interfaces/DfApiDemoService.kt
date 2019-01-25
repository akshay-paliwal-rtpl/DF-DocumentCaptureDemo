package com.ranosys.df_doccapture_demo.api.interfaces

import com.ranosys.df_doccapture_demo.api.LoginRequestModel
import com.ranosys.df_doccapture_demo.api.LoginResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * @Class Interface for managing api methods to be called from fragments or activities
 * @author Ranosys Technologies
 * @Date 26-Dec-2018
 */
interface DfApiDemoService {

    interface VerifyApiKeyService {
        @POST("/login/")
        @Headers("Content-Type:application/json")
        fun loginUser(@Body requestModle: LoginRequestModel): Call<LoginResponseModel>
    }
}