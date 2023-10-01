package com.ecomapp.admin.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.admin.Models.SubCatModel
import com.ecomapp.admin.Repositories.DataRepository
import com.ecomapp.febric.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class SubCatViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    var subCat_mutableLiveData = MutableLiveData<Response<ArrayList<SubCatModel>>>()
    val subCat_liveData: LiveData<Response<ArrayList<SubCatModel>>>
        get() = subCat_mutableLiveData

    var delete_mutableLiveData = MutableLiveData<Response<String>>()
    val delete_liveData: LiveData<Response<String>>
        get() = delete_mutableLiveData

    fun LoadSubCatigories(parentCatName: String, mainCatName: String) {
        viewModelScope.async {
            val result = dataRepository.LoadSubCatigories(parentCatName, mainCatName)
            subCat_mutableLiveData.postValue(result)
        }
    }
    fun deleteSubCategory(parentCatName: String, mainCatName: String,subCatName : String) {
        viewModelScope.async {
            val result = dataRepository.deleteSubCategory(parentCatName, mainCatName,subCatName)
            delete_mutableLiveData.postValue(result)
        }
    }

}