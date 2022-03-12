package com.example.sosudatech

import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    companion object {
        private var instance: LoginRepository? = null

        fun getinstance(): LoginRepository {
            if (instance == null) {
                instance = LoginRepository()
            }

            return instance as LoginRepository
        }
    }
    fun getLogin(itemRequestedData: ItemRequestedData): MutableLiveData<Response<LoginResponsedata>>? {
        val logindata = MutableLiveData<Response<LoginResponsedata>>()

        val LoginDataActivity: ApiInterface = RetrofitClient.apiInterface
        LoginDataActivity.getLogin(itemRequestedData)
            .enqueue(object : Callback<LoginResponsedata> {
                override fun onResponse(call: Call<LoginResponsedata>, response: Response<LoginResponsedata>?) {
                    val itemItem = response?.body()
                    if(response!=null && response.code()==200)
                    {
                        logindata.value = response
                        logindata
                    }
                    else
                    {
                        logindata.value = null
                    }
                }

                override fun onFailure(call: Call<LoginResponsedata>?, t: Throwable) {
                    logindata.value = null
                }

            })
        return logindata
    }
}