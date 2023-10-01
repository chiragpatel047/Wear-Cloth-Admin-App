package com.ecomapp.admin.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.admin.Adapters.BannerAdapter
import com.ecomapp.admin.Repositories.DataRepository
import com.ecomapp.admin.ViewModels.BannerViewModel
import javax.inject.Inject

class BannerVMF @Inject constructor(val dataRepository: DataRepository ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BannerViewModel(dataRepository) as T
    }
}