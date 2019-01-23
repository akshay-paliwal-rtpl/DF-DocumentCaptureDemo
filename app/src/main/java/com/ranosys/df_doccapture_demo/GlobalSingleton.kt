package com.ranosys.df_doccapture_demo

import com.ranosys.datafornix.api.datamodels.apiresultmodels.OcrResultData

object GlobalSingleton {
    var userId: String = ""
    lateinit var ocrResult: OcrResultData
}