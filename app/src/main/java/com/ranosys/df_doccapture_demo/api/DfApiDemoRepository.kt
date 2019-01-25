package com.ranosys.df_doccapture_demo.api

import com.ranosys.df_doccapture_demo.api.interfaces.DfApiDemoCallback
import com.ranosys.df_doccapture_demo.api.interfaces.DfApiDemoService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * @Details repository object to handle api integration
 * @Author Ranosys Technologies
 * @Date 24-Dec-2018
 */
object DfApiDemoRepository {

    fun loginUser(
        requestModel: LoginRequestModel,
        callbackDfDemo: DfApiDemoCallback<LoginResponseModel>
    ) {
        val retrofit = DfApiDemoClient.retrofit
        val callGet = retrofit?.create<DfApiDemoService.VerifyApiKeyService>(
            DfApiDemoService.VerifyApiKeyService::class.java
        )
            ?.loginUser(requestModel)

        callGet?.enqueue(object : Callback<LoginResponseModel> {
            override fun onFailure(call: Call<LoginResponseModel>?, t: Throwable?) {
                callbackDfDemo.onError(t.toString())
            }

            override fun onResponse(
                call: Call<LoginResponseModel>?,
                response: Response<LoginResponseModel>?
            ) {
                if (response?.isSuccessful!!) {
                    callbackDfDemo.onSuccess(response.body()!!)
                } else {
//                        callbackDfDemo.onError(callbackDfDemo, response)
                    setError(callbackDfDemo, response)
                }
            }

        })
    }

    fun setError(callbackDfDemo: DfApiDemoCallback<*>, response: Response<*>) {
        val gson = Gson()
        val adapter = gson.getAdapter<ErrorResponse>(ErrorResponse::class.java)
        try {
            if (null != response.errorBody()) {
                val errorParser = adapter.fromJson(response.errorBody()!!.string())
                if (errorParser.message != null) {
                    callbackDfDemo.onError(errorParser.message!!)
                } else {
                    callbackDfDemo.onError(response.raw().message())
                }
            }
        } catch (e: IOException) {
            callbackDfDemo.onError("Error")
            e.printStackTrace()
        }
    }

}