package com.example.sosudatech

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import retrofit2.Response

class LoginViewModel : ViewModel {

    private var mutableLiveData: MutableLiveData<Response<LoginResponsedata>>? = null

    private var itemRepository: LoginRepository? = null

    constructor(itemRequestedData: ItemRequestedData) {
        if (mutableLiveData != null) {
            return
        }
        itemRepository = LoginRepository.getinstance()
        mutableLiveData = itemRepository?.getLogin(itemRequestedData)
    }


    fun getLogin(): MutableLiveData<Response<LoginResponsedata>>? {

        if (mutableLiveData != null) {
            mutableLiveData
        }
        return mutableLiveData
    }

    class ViewModelFactory(val itemRequestedData: ItemRequestedData) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(itemRequestedData) as T
            }
            throw IllegalArgumentException("Unknown class name")
        }

    }
}