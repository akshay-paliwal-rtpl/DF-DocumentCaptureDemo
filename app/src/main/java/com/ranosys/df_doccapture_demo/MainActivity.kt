package com.ranosys.df_doccapture_demo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ranosys.datafornix.api.ApiCallback
import com.ranosys.datafornix.api.DataFornixSdk
import com.ranosys.datafornix.api.datamodels.apirequestmodels.CreateUserRequestModel
import com.ranosys.datafornix.api.datamodels.apiresultmodels.CreateUserResponse
import com.ranosys.df_doccapture_demo.GlobalSingleton.userId
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DataFornixSdk.initializeSdk("ranosys:GQrFmqL9Vkx1J7qFD9nyVgZ9EfG7OzEq")
        btn_create_user.setOnClickListener {
            callAPI()
        }
    }

    private fun callAPI() {
        /*  val requestModel = CreateUserRequestModel(et_email.text.toString(), et_name.text.toString(),
              et_phone_no.text.toString().toLong(),
              et_country_code.text.toString() )*/

        val requestModel = CreateUserRequestModel(
            "mustafa.bohra@ranosys.com",
            "Mustafa",
            8829889052,
            "91"
        )
        DataFornixSdk.createUser(this@MainActivity, requestModel, object : ApiCallback<CreateUserResponse> {
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
}
