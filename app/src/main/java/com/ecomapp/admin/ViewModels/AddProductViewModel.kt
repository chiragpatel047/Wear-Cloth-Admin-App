package com.ecomapp.admin.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.admin.Repositories.DataRepository
import com.ecomapp.febric.Models.ProuctModel
import com.ecomapp.febric.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class AddProductViewModel @Inject constructor(val dataRepository: DataRepository)  : ViewModel() {

    var addProduct_mutableLiveData = MutableLiveData<Response<String>>()
    val addProduct_liveData : LiveData<Response<String>>
        get() = addProduct_mutableLiveData

    var product_mutableLiveData = MutableLiveData<Response<ProuctModel>>()
    val product_liveData : LiveData<Response<ProuctModel>>
        get() = product_mutableLiveData

    fun updateProduct(prouctModel: ProuctModel){
        viewModelScope.async {
            val result = dataRepository.updateProduct(prouctModel)
            addProduct_mutableLiveData.postValue(result)
        }
    }

    fun LoadSingleProduct(productId : String){
        viewModelScope.async {
            val result = dataRepository.LoadSingleProduct(productId)
            product_mutableLiveData.postValue(result)
        }
    }
}