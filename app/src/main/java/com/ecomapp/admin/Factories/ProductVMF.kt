package com.ecomapp.admin.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.admin.Repositories.DataRepository
import com.ecomapp.admin.ViewModels.ProductViewModel
import javax.inject.Inject

class ProductVMF @Inject constructor(val dataRepository: DataRepository)  : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(dataRepository) as T
    }
}