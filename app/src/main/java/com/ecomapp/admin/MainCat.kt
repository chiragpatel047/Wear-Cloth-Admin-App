package com.ecomapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.admin.Factories.MainCatVMF
import com.ecomapp.admin.Models.MainCatModel
import com.ecomapp.admin.ViewModels.MainCatViewModel
import com.ecomapp.admin.databinding.ActivityMainCatBinding
import com.ecomapp.febric.Adapters.MainCatAdapter
import com.ecomapp.febric.Repositories.Response
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainCat : AppCompatActivity() {

    lateinit var binding: ActivityMainCatBinding

    lateinit var mainCatViewModel: MainCatViewModel

    lateinit var mainCatList : ArrayList<MainCatModel>
    lateinit var mainCatAdapter: MainCatAdapter

    @Inject
    lateinit var mainCatVMF: MainCatVMF

    var parentCatName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main_cat)
        supportActionBar?.hide()

        parentCatName = intent.getStringExtra("parentCat")
        binding.topName.text = parentCatName

        mainCatViewModel = ViewModelProvider(this,mainCatVMF).get(MainCatViewModel::class.java)

        mainCatList = ArrayList()
        mainCatAdapter = MainCatAdapter(this,mainCatList,::getParentCat,::deleteMainCat)

        binding.catRecv.layoutManager = LinearLayoutManager(this)
        binding.catRecv.adapter = mainCatAdapter

        mainCatViewModel.mainCat_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{

                    mainCatList.clear()
                    mainCatList.addAll(it.data!!)
                    it.data.clear()

                    mainCatAdapter.notifyDataSetChanged()

                    binding.loadingAnim4.visibility = View.GONE

                    if(mainCatList.isEmpty()){
                        binding.noFoundText4.visibility = View.VISIBLE
                    }else{
                        binding.noFoundText4.visibility = View.GONE
                    }
                }

                is Response.Error ->{

                }
                is Response.Loading -> {

                }
            }
        })

        binding.addMainCat.setOnClickListener {
            val intent = Intent(this,AddMainCat::class.java)
            intent.putExtra("parentCat",parentCatName)
            startActivity(intent)
        }
    }

    fun getParentCat() : String{
        return parentCatName!!
    }

    fun deleteMainCat(parentCat : String,mainCat : String){
        mainCatViewModel.deleteMainCategory(parentCat,mainCat)
    }

    override fun onResume() {
        super.onResume()
        mainCatViewModel.LoadMainCategories(parentCatName!!)
    }
}