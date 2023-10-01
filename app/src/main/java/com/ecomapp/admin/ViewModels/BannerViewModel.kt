package com.ecomapp.admin.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.admin.Models.BannerModel
import com.ecomapp.admin.Repositories.DataRepository
import com.ecomapp.febric.Models.ProuctModel
import com.ecomapp.febric.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class BannerViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    var banner_mutableLiveData = MutableLiveData<Response<ArrayList<BannerModel>>>()
    val banner_liveData : LiveData<Response<ArrayList<BannerModel>>>
        get() = banner_mutableLiveData

    fun LoadHomeBanners(){
        viewModelScope.async {
            val result = dataRepository.LoadHomeBanners()
            banner_mutableLiveData.postValue(result)
        }
    }

}