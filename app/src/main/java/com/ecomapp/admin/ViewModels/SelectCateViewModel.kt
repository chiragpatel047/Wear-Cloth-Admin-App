package com.ecomapp.admin.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.admin.Models.BannerModel
import com.ecomapp.admin.Models.MainCatModel
import com.ecomapp.admin.Models.ProductImageModel
import com.ecomapp.admin.Models.SubCatModel
import com.ecomapp.admin.Repositories.DataRepository
import com.ecomapp.febric.Models.ProuctModel
import com.ecomapp.febric.Models.SizeModel
import com.ecomapp.febric.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class SelectCateViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    var bannerData_mutableLiveData = MutableLiveData<Response<ArrayList<BannerModel>>>()
    val bannerData_liveData : LiveData<Response<ArrayList<BannerModel>>>
        get() = bannerData_mutableLiveData

        var mainCat_mutableLiveData = MutableLiveData<Response<ArrayList<MainCatModel>>>()
    val mainCat_liveData : LiveData<Response<ArrayList<MainCatModel>>>
        get() = mainCat_mutableLiveData

    var subCat_mutableLiveData = MutableLiveData<Response<ArrayList<SubCatModel>>>()
    val subCat_liveData: LiveData<Response<ArrayList<SubCatModel>>>
        get() = subCat_mutableLiveData

    var add_mutableLiveData = MutableLiveData<Response<String>>()
    val add_liveData: LiveData<Response<String>>
        get() = add_mutableLiveData

    fun LoadHomeBanners(){
        viewModelScope.async {
            val result = dataRepository.LoadHomeBanners()
            bannerData_mutableLiveData.postValue(result)
        }
    }

    fun LoadMainCategories(catName : String){
        viewModelScope.async {
            val result = dataRepository.LoadMainCategories(catName)
            mainCat_mutableLiveData.postValue(result)
        }
    }

    fun LoadSubCatigories(parentCatName: String, mainCatName: String) {
        viewModelScope.async {
            val result = dataRepository.LoadSubCatigories(parentCatName, mainCatName)
            subCat_mutableLiveData.postValue(result)
        }
    }
    fun AddNewProduct(parentCatName: String, mainCatName: String,subCatName : String, prouctModel: ProuctModel ,sizeList : ArrayList<SizeModel>,
                      imageList : ArrayList<ProductImageModel>,selectedList : ArrayList<String>){

        viewModelScope.async {
            val result = dataRepository.AddNewProduct(parentCatName, mainCatName,subCatName,prouctModel,sizeList,imageList,selectedList)
            add_mutableLiveData.postValue(result)
        }
    }
}