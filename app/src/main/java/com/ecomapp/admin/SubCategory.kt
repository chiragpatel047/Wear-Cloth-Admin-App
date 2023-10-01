package com.ecomapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.admin.Adapters.SubCatAdapter
import com.ecomapp.admin.Factories.SubCatVMF
import com.ecomapp.admin.Models.SubCatModel
import com.ecomapp.admin.ViewModels.SubCatViewModel
import com.ecomapp.admin.databinding.ActivitySubCategoryBinding
import com.ecomapp.febric.Repositories.Response
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class SubCategory : AppCompatActivity() {

    lateinit var binding : ActivitySubCategoryBinding

    lateinit var subCatList : ArrayList<SubCatModel>
    lateinit var subCatAdapter: SubCatAdapter

    lateinit var subCatViewModel: SubCatViewModel

    @Inject
    lateinit var subCatVMF: SubCatVMF

    var parentCatName : String? = null
    var mainCatName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_sub_category)
        supportActionBar?.hide()

        parentCatName = intent.getStringExtra("parentCat")
        mainCatName = intent.getStringExtra("mainCat")
        binding.topName.text = mainCatName

        subCatViewModel = ViewModelProvider(this,subCatVMF).get(SubCatViewModel::class.java)

        subCatList = ArrayList()
        subCatAdapter = SubCatAdapter(this,subCatList,::getParentCat,::getMainCat,::deleteSubCat)

        binding.subCatRecv.layoutManager = LinearLayoutManager(this)
        binding.subCatRecv.adapter = subCatAdapter

        binding.addMainCat.setOnClickListener {
            val intent = Intent(this,AddSubCategory::class.java)
            intent.putExtra("parentCat",parentCatName)
            intent.putExtra("mainCat",mainCatName)
            startActivity(intent)
        }

        subCatViewModel.subCat_liveData.observe(this,{

            when(it){
                is Response.Sucess ->{
                    subCatList.clear()
                    subCatList.addAll(it.data!!)
                    it.data.clear()
                    subCatAdapter.notifyDataSetChanged()

                    binding.loadingAnim5.visibility = View.GONE
                    if(subCatList.isEmpty()){
                        binding.noFoundText5.visibility = View.VISIBLE
                    }else{
                        binding.noFoundText5.visibility = View.GONE
                    }
                }

                is Response.Error -> {
                    Toast.makeText(this,it.errorMsg.toString(), Toast.LENGTH_LONG).show()

                }
                is Response.Loading -> {

                }
            }

        })

    }

    override fun onResume() {
        super.onResume()
        subCatViewModel.LoadSubCatigories(parentCatName!!, mainCatName!!)
    }

    fun deleteSubCat(parentCatName: String, mainCatName: String,subCatName : String){
        subCatViewModel.deleteSubCategory(parentCatName,mainCatName,subCatName)
    }

    fun getParentCat() : String{
        return parentCatName!!
    }

    fun getMainCat() : String{
        return mainCatName!!
    }
}