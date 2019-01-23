package com.ranosys.df_doccapture_demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.ranosys.datafornix.api.ApiCallback
import com.ranosys.datafornix.api.DataFornixSdk
import com.ranosys.datafornix.api.datamodels.apirequestmodels.AssetType
import com.ranosys.datafornix.api.datamodels.apirequestmodels.UploadIdentityAssetRequestModel
import com.ranosys.datafornix.api.datamodels.apiresultmodels.OcrResultData
import com.ranosys.datafornix.api.datamodels.apiresultmodels.UploadAssetBasicResponse
import com.ranosys.df_doccapture_demo.GlobalSingleton.ocrResult
import com.ranosys.df_doccapture_demo.GlobalSingleton.userId
import kotlinx.android.synthetic.main.activity_asset_form.*

class AssetFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asset_form)
        setFormView()
        btnUploadData.setOnClickListener {
            val uploadImageModel = UploadIdentityAssetRequestModel(
                getAssetType(ocrResult.assetType),
                getEditTextValueForKey("first_name"),
                getEditTextValueForKey("middle_name"),
                getEditTextValueForKey("last_name"),
                getEditTextValueForKey("nationality"),
                getEditTextValueForKey("gender"),
                getEditTextValueForKey("birth_date"),
                getEditTextValueForKey("licence_number"),
                getEditTextValueForKey("issue_date"),
                getEditTextValueForKey("expiry_date"),
                getEditTextValueForKey("country_code"),
                getEditTextValueForKey("address"),
                getEditTextValueForKey("identity_number"),
                getEditTextValueForKey("visa_number"),
                getEditTextValueForKey("visa_type"),
                getEditTextValueForKey("passport_number"),
                getEditTextValueForKey("remarks"),
                false,
                40.0,
                50.0,
                "Asia"
            )

            DataFornixSdk.uploadIdentityAsset(
                this@AssetFormActivity,
                userId,
                uploadImageModel,
                object : ApiCallback<UploadAssetBasicResponse> {
                    override fun onError(message: String) {
                        Toast.makeText(this@AssetFormActivity, message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onException(throwable: Throwable) {
                        Toast.makeText(this@AssetFormActivity, throwable.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(result: UploadAssetBasicResponse) {
                        Toast.makeText(this@AssetFormActivity, result.message, Toast.LENGTH_SHORT).show()
                    }
                })
        }

    }

    private fun getEditTextValueForKey(key: String): String {
        var value: String = ""
        for (viewCount in 0..fieldsLayout.childCount) {
            val viewItem = fieldsLayout.getChildAt(viewCount)
            if (viewItem is EditText) {
                if (viewItem.tag == key) {
                    value = viewItem.text.toString()
                    break
                }
            }
        }
        return value
    }

    private fun setFormView() {
        tvAssetType.text = ocrResult.assetType
        for (propertyItem in ocrResult.properties) {
            val textViewItem = TextView(this)
            val editTextItem = EditText(this)
            editTextItem.tag = propertyItem.key
            textViewItem.text = propertyItem.key ?: ""
            editTextItem.setText(propertyItem.value ?: "")
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(0, 0, 0, 10)
            editTextItem.layoutParams = lp
            fieldsLayout.addView(textViewItem)
            fieldsLayout.addView(editTextItem)
        }
    }

    fun getFieldValueFromOcrResult(ocrResultData: OcrResultData, key: String): String {
        var value: String = ""
        for (item in ocrResultData.properties) {
            if (item.key == key) {
                value = item.value ?: ""
                break
            }
        }
        return value
    }

    fun getAssetType(assetName: String): AssetType {
        return when (assetName) {
            "Driving License" -> AssetType.DrivingLicense
            "Passport" -> AssetType.Passport
            "Identity Card" -> AssetType.IdentityCard
            "Is Right To Work" -> AssetType.IsRightToWork
            else -> AssetType.DrivingLicense
        }
    }
}
