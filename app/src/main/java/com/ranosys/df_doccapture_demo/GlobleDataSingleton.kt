package com.ranosys.df_doccapture_demo

import com.ranosys.datafornix.api.datamodels.apiresultmodels.OcrResultData
import com.ranosys.df_doccapture_demo.api.LoginResponseModel

object GlobleDataSingleton {
    lateinit var ocrResult: OcrResultData
    lateinit var userId: String
    lateinit var currentToken: String
    lateinit var currentSdkToken: String
    lateinit var loginUserModel: LoginResponseModel
    lateinit var userMail : String

}