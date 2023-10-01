package com.ecomapp.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.admin.Adapters.BannerAdapter
import com.ecomapp.admin.Factories.BannerVMF
import com.ecomapp.admin.Models.BannerModel
import com.ecomapp.admin.R
import com.ecomapp.admin.ViewModels.BannerViewModel
import com.ecomapp.admin.databinding.ActivityBannersBinding
import com.ecomapp.febric.Repositories.Response
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class Banners : AppCompatActivity() {

    lateinit var binding : ActivityBannersBinding
    lateinit var bannerViewModel: BannerViewModel

    lateinit var bannerList: ArrayList<BannerModel>
    lateinit var bannerAdapter: BannerAdapter

    @Inject
    lateinit var bannerVMF: BannerVMF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_banners)
        supportActionBar?.hide()

        bannerViewModel = ViewModelProvider(this,bannerVMF).get(BannerViewModel::class.java)

        bannerList = ArrayList()
        bannerAdapter = BannerAdapter(this,bannerList)

        binding.bannerRecv.layoutManager = LinearLayoutManager(this)
        binding.bannerRecv.adapter = bannerAdapter

        bannerViewModel.banner_liveData.observe(this,{
            when(it){
                is Response.Sucess -> {

                    bannerList.clear()
                    bannerList.addAll(it.data!!)
                    bannerAdapter.notifyDataSetChanged()
                    binding.loadingAnim.visibility = View.GONE
                    it.data.clear()

                    if(bannerList.isEmpty()){
                        binding.noFoundText.visibility = View.VISIBLE
                    }else{
                        binding.noFoundText.visibility = View.GONE
                    }
                }
                is Response.Error -> {

                }
                is Response.Loading -> {

                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        bannerViewModel.LoadHomeBanners()
    }
}