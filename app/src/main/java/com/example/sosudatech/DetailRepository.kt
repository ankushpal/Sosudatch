package com.example.sosudatech

import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailRepository {
    companion object {
        private var instance: DetailRepository? = null

        fun getinstance(): DetailRepository {
            if (instance == null) {
                instance = DetailRepository()
            }

            return instance as DetailRepository
        }
    }
    fun getDetails(): MutableLiveData<DetailsDataResponse> {
        val detailsdata = MutableLiveData<DetailsDataResponse>()

        val detailsDataActivity: ApiInterface = RetrofitClient.apiInterface
        detailsDataActivity.getDetails()
            .enqueue(object : Callback<DetailsDataResponse> {
                override fun onResponse(call: Call<DetailsDataResponse>, response: Response<DetailsDataResponse>?) {
                    val itemItem = response?.body()
                    if(response!=null && response.code()==200)
                    {
                        detailsdata.value = itemItem
                        detailsdata
                    }
                    else
                    {
                        detailsdata.value = null
                    }
                }

                override fun onFailure(call: Call<DetailsDataResponse>?, t: Throwable) {
                    detailsdata.value = null
                }

            })
        return detailsdata
    }
}