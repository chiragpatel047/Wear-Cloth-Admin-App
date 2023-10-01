package com.ecomapp.admin.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.admin.Models.MainCatModel
import com.ecomapp.admin.Repositories.DataRepository
import com.ecomapp.febric.Models.ProuctModel
import com.ecomapp.febric.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class ProductViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    var product_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val product_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = product_mutableLiveData

    var delete_mutableLiveData = MutableLiveData<Response<String>>()
    val delete_liveData : LiveData<Response<String>>
        get() = delete_mutableLiveData

    var banner_product_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val banner_product_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = banner_product_mutableLiveData

    var cat_product_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val cat_product_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = cat_product_mutableLiveData

    var delete_from_banner_mutableLiveData = MutableLiveData<Response<String>>()
    val delete_from_banner_liveData : LiveData<Response<String>>
        get() = delete_from_banner_mutableLiveData

    var delete_from_cat_mutableLiveData = MutableLiveData<Response<String>>()
    val delete_from_cat_liveData : LiveData<Response<String>>
        get() = delete_from_cat_mutableLiveData

    fun LoadAllProducts(){
        viewModelScope.async {
            val result = dataRepository.LoadAllProducts()
            product_mutableLiveData.postValue(result)
        }
    }

    fun deleteProduct(productId : String){
        viewModelScope.async {
            val result = dataRepository.deleteProduct(productId)
            delete_mutableLiveData.postValue(result)
        }
    }
    fun LoadBannersProducts(loadUsing : String){
        viewModelScope.async {
            val result = dataRepository.LoadBannersProducts(loadUsing)
            banner_product_mutableLiveData.postValue(result)
        }
    }
    fun LoadProducts(parentCatName : String,mainCatName : String,subCatName : String){
        viewModelScope.async {
            val result = dataRepository.LoadProducts(parentCatName,mainCatName,subCatName)
            cat_product_mutableLiveData.postValue(result)
        }
    }

    fun deleteProductFromBanner(pos : String , productId : String){
        viewModelScope.async {
            val result = dataRepository.deleteProductFromBanner(pos,productId)
            delete_from_banner_mutableLiveData.postValue(result)
        }
    }

    fun deleteProductFromCat(parentCatName : String,mainCatName : String,subCatName : String, productId : String){
        viewModelScope.async {
            val result = dataRepository.deleteProductFromCat(parentCatName,mainCatName,subCatName,productId)
            delete_from_cat_mutableLiveData.postValue(result)
        }
    }
}