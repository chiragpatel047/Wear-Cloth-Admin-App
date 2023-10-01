package com.ecomapp.admin.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.admin.Models.MainCatModel
import com.ecomapp.admin.Models.SubCatModel
import com.ecomapp.admin.Repositories.DataRepository
import com.ecomapp.febric.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class AddSubCatViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel(){

    var add_mutableLiveData = MutableLiveData<Response<String>>()
    val add_liveData : LiveData<Response<String>>
        get() = add_mutableLiveData

    fun AddSubCategory(parentCatName : String,mainCatName : String,subCatModel: SubCatModel){
        viewModelScope.async {
            val result = dataRepository.AddSubCategory(parentCatName,mainCatName,subCatModel)
            add_mutableLiveData.postValue(result)
        }
    }

}