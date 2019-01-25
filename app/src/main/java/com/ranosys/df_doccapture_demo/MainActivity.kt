package com.ranosys.df_doccapture_demo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.ranosys.datafornix.api.ApiCallback
import com.ranosys.datafornix.api.DataFornixSdk
import com.ranosys.datafornix.api.datamodels.apirequestmodels.CreateUserRequestModel
import com.ranosys.datafornix.api.datamodels.apiresultmodels.CreateUserResponse
import com.ranosys.df_doccapture_demo.GlobalSingleton.userId
import com.ranosys.df_doccapture_demo.GlobleDataSingleton.loginUserModel
import com.ranosys.df_doccapture_demo.api.DfApiDemoRepository
import com.ranosys.df_doccapture_demo.api.LoginRequestModel
import com.ranosys.df_doccapture_demo.api.LoginResponseModel
import com.ranosys.df_doccapture_demo.api.interfaces.DfApiDemoCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    var tokenAxis = "axis:ORzS1n3HKEu8P5TIvwnMW85pk20Z8ywO"
    var tokenRanosys = "ranosys:gIC7fNkEGjRJ7oA0dFefEhCU6150lSB4"


    var sdkTokenRanosys = "ranosys:po5KAZjv7zFIU8NFHn9LRTIvEn0ciSq6"
    var sdkTokenAxis = "axis:po5KAZjv7zFIU8NFHn9LRTIvEn0ciSq6"


    var currentApiToken = tokenRanosys

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTenantSelectionSpinner()
        DataFornixSdk.initializeSdk(currentApiToken)
        DataFornixSdk.isShowLoading = true
        btn_login.setOnClickListener { loginToTestServer() }
    }

    private fun callAPI() {

        val requestData =
            CreateUserRequestModel(
                et_email.text.toString(),
                loginUserModel.username,
                loginUserModel.phone_number.toLong(),
                loginUserModel.country_code
            )

        DataFornixSdk.createUser(this@MainActivity, requestData, object : ApiCallback<CreateUserResponse> {
            override fun onError(message: String) {
                Toast.makeText(this@MainActivity, message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onException(throwable: Throwable) {
                Toast.makeText(this@MainActivity, throwable.message, Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(result: CreateUserResponse) {
                userId = result.data._id
                startActivity(Intent(this@MainActivity, UploadAssetActivity::class.java))
                finish()
            }
        })
    }

    private fun loginToTestServer() {
        if (et_email.text.isNotEmpty() && et_password.text.isNotEmpty()) {
            val requestModel =
                LoginRequestModel(et_email.text.toString(), et_password.text.toString())
            DfApiDemoRepository.loginUser(
                requestModel,
                object : DfApiDemoCallback<LoginResponseModel> {
                    override fun onException(error: Throwable) {
                        Toast.makeText(
                            this@MainActivity,
                            error.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onError(error: String) {
                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
                    }

                    override fun onSuccess(t: LoginResponseModel) {
                        Toast.makeText(this@MainActivity, "Login Success", Toast.LENGTH_LONG)
                            .show()
                        GlobleDataSingleton.loginUserModel = t
                        callAPI()
                    }

                })
        } else {
            Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_LONG).show()
        }
    }

    private fun setTenantSelectionSpinner() {
        val tenantsList = ArrayList<String>()
        tenantsList.add("Ranosys")
        tenantsList.add("Axis")

        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tenantsList)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_tenant_selection.adapter = dataAdapter
        spinner_tenant_selection.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        currentApiToken = tokenRanosys

                        GlobleDataSingleton.currentSdkToken = sdkTokenRanosys

                        DataFornixSdk.initializeSdk(currentApiToken)
                    }

                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        currentApiToken = if (position == 0) tokenRanosys else tokenAxis
                        GlobleDataSingleton.currentSdkToken = if (position == 0) sdkTokenRanosys else sdkTokenAxis

                        DataFornixSdk.initializeSdk(currentApiToken)
                    }

                }
    }

}
