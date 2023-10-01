package com.ecomapp.admin.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.admin.Repositories.DataRepository
import com.ecomapp.admin.ViewModels.SubCatViewModel
import javax.inject.Inject

class SubCatVMF @Inject constructor(val dataRepository: DataRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SubCatViewModel(dataRepository) as T
    }
}