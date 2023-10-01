package com.ecomapp.admin.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.admin.Models.MainCatModel
import com.ecomapp.admin.Repositories.DataRepository
import com.ecomapp.febric.Models.OrderModel
import com.ecomapp.febric.Repositories.Response
import com.ecomapp.wear.Models.NotificationModel
import kotlinx.coroutines.async
import javax.inject.Inject

class MainViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    var order_mutableLiveData = MutableLiveData<Response<ArrayList<OrderModel>>>()
    val order_liveData : LiveData<Response<ArrayList<OrderModel>>>
        get() = order_mutableLiveData

    var deliver_mutableLiveData = MutableLiveData<Response<String>>()
    val deliver_liveData : LiveData<Response<String>>
        get() = deliver_mutableLiveData

    var cancel_mutableLiveData = MutableLiveData<Response<String>>()
    val cancel_liveData : LiveData<Response<String>>
        get() = cancel_mutableLiveData

    fun loadOrders(orderCat : String){
        viewModelScope.async {
            val result = dataRepository.loadOrders(orderCat)
            order_mutableLiveData.postValue(result)
        }
    }

    fun DeliveredOrder(orderId : String, userId : String,productId: String,productMainImage : String){
        viewModelScope.async {
            val result = dataRepository.DeliveredOrder(orderId,userId,productId,productMainImage)
            deliver_mutableLiveData.postValue(result)
        }
    }

    fun CancelOrder(orderId : String, userId : String,productId: String,productMainImage : String){
        viewModelScope.async {
            val result = dataRepository.CancelOrder(orderId,userId,productId,productMainImage)
            cancel_mutableLiveData.postValue(result)
        }
    }

}