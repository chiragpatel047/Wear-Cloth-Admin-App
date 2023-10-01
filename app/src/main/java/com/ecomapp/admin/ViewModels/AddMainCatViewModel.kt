package com.ecomapp.admin.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.admin.Models.BannerModel
import com.ecomapp.admin.Models.MainCatModel
import com.ecomapp.admin.Repositories.DataRepository
import com.ecomapp.febric.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class AddMainCatViewModel @Inject constructor(val dataRepository: DataRepository)  : ViewModel() {

    var add_mutableLiveData = MutableLiveData<Response<String>>()
    val add_liveData : LiveData<Response<String>>
        get() = add_mutableLiveData

    var update_mutableLiveData = MutableLiveData<Response<String>>()
    val update_liveData : LiveData<Response<String>>
        get() = update_mutableLiveData
    fun AddMainCategory(parentCat : String,mainCatModel: MainCatModel){
        viewModelScope.async {
            val result = dataRepository.AddMainCategory(parentCat,mainCatModel)
            add_mutableLiveData.postValue(result)
        }
    }
    fun UpdateBanners(pos : String,bannerModel: BannerModel){
        viewModelScope.async {
            val result = dataRepository.UpdateBanners(pos,bannerModel)
            update_mutableLiveData.postValue(result)
        }
    }
}