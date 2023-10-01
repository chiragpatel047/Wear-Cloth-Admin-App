package com.ecomapp.admin.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.admin.Repositories.DataRepository
import com.ecomapp.febric.Models.OrderModel
import com.ecomapp.febric.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class HomeViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    var notify_mutableLiveData = MutableLiveData<Response<String>>()
    val notify_liveData : LiveData<Response<String>>
        get() = notify_mutableLiveData

    fun sendFBNotification(title : String, text : String){
        viewModelScope.async {
            val result = dataRepository.sendFBNotification(title,text)
            notify_mutableLiveData.postValue(result)
        }
    }
}