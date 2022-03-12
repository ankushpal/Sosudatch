package com.example.sosudatech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var emailId:String? = null
    var pass:String? = null
    var loginViewModel: LoginViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }
    fun initUI()
    {
        btnLogin.setOnClickListener {
          emailId = etEmailId.text.toString().trim()
            pass=  etPassword.text.toString().trim()
            if(emailId.equals(""))
            {
                etEmailId?.requestFocus()
                etEmailId?.setError("Enter User Name")
            }else if(pass.equals(""))
            {
                etPassword?.requestFocus()
                etPassword?.setError("Enter Password")
            }
            else
            {
                callApi(emailId!!,pass!!)
            }

        }
        }

    private fun callApi(emailId: String, pass: String) {
        idPBLoading!!.visibility = View.VISIBLE
        val itemRequestedData = ItemRequestedData(emailId,pass)
        loginViewModel = ViewModelProviders.of(this, LoginViewModel.ViewModelFactory(itemRequestedData)).get(LoginViewModel::class.java)
        loginViewModel!!.getLogin()?.observe(this, androidx.lifecycle.Observer { baseResponse ->
            if (baseResponse != null )
                idPBLoading!!.visibility = View.GONE
            when {
                baseResponse.code() == 200 -> {

                    Toast.makeText(this," Login Successful",Toast.LENGTH_LONG).show()
                    val openMainActivity = Intent(this@MainActivity, DetailsActivity::class.java)
                    openMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    openMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(openMainActivity)
                    finish()
                    val editor = getSharedPreferences(CommonClass.Common.MY_PREFS_NAME, MODE_PRIVATE).edit()
                    editor.putBoolean(CommonClass.Common.isSession, true)
                    editor.apply()
                }
                baseResponse.code() == 500 -> {

                    Toast.makeText(this,"Username or Password is invalid.",Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "Some thing went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })

}
}