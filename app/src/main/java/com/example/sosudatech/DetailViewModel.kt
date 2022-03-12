package com.example.rachtr

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sosudatech.DetailRepository
import com.example.sosudatech.DetailsDataResponse
import retrofit2.Response

class DetailViewModel : ViewModel {

    private var mutableLiveData: MutableLiveData<DetailsDataResponse>? = null

    private var itemRepository: DetailRepository? = null

    constructor() {
        if (mutableLiveData != null) {
            return
        }
        itemRepository = DetailRepository.getinstance()
        mutableLiveData = itemRepository?.getDetails()
    }


    fun getDetails(): MutableLiveData<DetailsDataResponse>? {

        if (mutableLiveData != null) {
            mutableLiveData
        }
        return mutableLiveData
    }

    class ViewModelFactory() : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel() as T
            }
            throw IllegalArgumentException("Unknown class name")
        }

    }
}