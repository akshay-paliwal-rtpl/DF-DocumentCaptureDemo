package com.ranosys.df_doccapture_demo

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.ranosys.datafornix.api.ApiCallback
import com.ranosys.datafornix.api.DataFornixSdk
import com.ranosys.datafornix.api.datamodels.apirequestmodels.AssetType
import com.ranosys.datafornix.api.datamodels.apirequestmodels.GetOcrRequestModel
import com.ranosys.datafornix.api.datamodels.apiresultmodels.OcrResultData
import com.ranosys.datafornix.documentcapture.DocumentCaptureConfig
import com.ranosys.datafornix.documentcapture.DocumentCaptureInstance
import com.ranosys.df_doccapture_demo.GlobalSingleton.ocrResult
import com.ranosys.df_doccapture_demo.GlobalSingleton.userId
import kotlinx.android.synthetic.main.activity_upload_asset.*


class UploadAssetActivity : AppCompatActivity() {
    lateinit var documentCaptureInstance: DocumentCaptureInstance
    val config = DocumentCaptureConfig()
    lateinit var requestModel: GetOcrRequestModel
    var imagePathFront = ""
    var imagePathBack = ""
    var selectedAssetType = AssetType.Passport
    var assetTypeSpinner =
        arrayOf(AssetType.Passport, AssetType.DrivingLicense, AssetType.IdentityCard, AssetType.IsRightToWork)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_asset)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, assetTypeSpinner)
        spinner_asset_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAssetType = assetTypeSpinner[position]
                if (selectedAssetType == AssetType.IdentityCard || selectedAssetType == AssetType.DrivingLicense) {
                    config.isDocumentBackCaptureRequired = true
                    iv_asset_back.visibility = View.VISIBLE
                } else {
                    config.isDocumentBackCaptureRequired = false
                    iv_asset_back.visibility = View.GONE
                }
            }
        }
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_asset_type.adapter = spinnerAdapter
        documentCaptureInstance = DocumentCaptureInstance(this)
        btn_select_image.setOnClickListener {
            callDocCaptureSDk()
        }

        btn_submit_image.setOnClickListener {
            if (config.isDocumentBackCaptureRequired) {
                if (!imagePathBack.isNotBlank()) {
                    Toast.makeText(this@UploadAssetActivity, "Image back capture required", Toast.LENGTH_SHORT).show()
                } else {
                    requestModel = GetOcrRequestModel(userId, selectedAssetType, imagePathFront, imagePathBack)
                    getImageOCR(requestModel)
                }
            } else {
                requestModel = GetOcrRequestModel(userId, selectedAssetType, imagePathFront, null)
                getImageOCR(requestModel)
            }

        }

    }

    private fun getImageOCR(requestModel: GetOcrRequestModel) {
        requestModel.let {
            DataFornixSdk.getOcrResultForDocument(this@UploadAssetActivity, it, object : ApiCallback<OcrResultData> {
                override fun onError(message: String) {
                    Toast.makeText(this@UploadAssetActivity, message, Toast.LENGTH_SHORT).show()
                }

                override fun onException(throwable: Throwable) {
                    Toast.makeText(this@UploadAssetActivity, throwable.message, Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(result: OcrResultData) {
                    ocrResult = result
                    startActivity(Intent(this@UploadAssetActivity, AssetFormActivity::class.java))
                }
            })
        }
    }

    private fun callDocCaptureSDk() {
        documentCaptureInstance.captureDocument(
            GlobleDataSingleton.currentSdkToken,
            config,
            object : DocumentCaptureInstance.DocumentCaptureListener {
                override fun onDocumentCaptureSuccess(path: List<String>) {
                    if (path.isNotEmpty() && path.size == 1) {
                        btn_submit_image.visibility = View.VISIBLE
                        imagePathFront = path.get(0)
                        iv_asset.setImageBitmap(BitmapFactory.decodeFile(path.get(0)))
                    } else if (path.isNotEmpty() && path.size == 2) {
                        btn_submit_image.visibility = View.VISIBLE
                        imagePathFront = path[0]
                        imagePathBack = path[1]
                        iv_asset.setImageBitmap(BitmapFactory.decodeFile(path.get(0)))
                        iv_asset_back.setImageBitmap(BitmapFactory.decodeFile(path.get(1)))
                    }
                }

                override fun onDocumentCaptureFailure(error: DocumentCaptureInstance.DocumentCaptureError) {
                    Toast.makeText(this@UploadAssetActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 321) {
            if (::documentCaptureInstance.isInitialized) {
                documentCaptureInstance.onDocumentCaptureResult(requestCode, resultCode, data)

            }
        }
    }

}
