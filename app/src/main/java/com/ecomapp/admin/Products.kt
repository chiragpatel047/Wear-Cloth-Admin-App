package com.ecomapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.admin.Factories.ProductVMF
import com.ecomapp.admin.ViewModels.ProductViewModel
import com.ecomapp.admin.databinding.ActivityProductsBinding
import com.ecomapp.febric.Adapters.ProductSimpleAdapter
import com.ecomapp.febric.Models.ProuctModel
import com.ecomapp.febric.Repositories.Response
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class Products : AppCompatActivity() {

    lateinit var binding : ActivityProductsBinding

    lateinit var productViewModel: ProductViewModel

    @Inject
    lateinit var productVMF: ProductVMF

    lateinit var productList : ArrayList<ProuctModel>
    lateinit var productAdapter : ProductSimpleAdapter

    var loadFor : String? = ""
    var loadUsing : String? = ""

    var parentCat : String? = ""
    var mainCat : String? = ""
    var subCat : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_products)
        supportActionBar?.hide()

        productViewModel = ViewModelProvider(this,productVMF).get(ProductViewModel::class.java)

        loadFor = intent.getStringExtra("loadFor")

        loadUsing = intent.getStringExtra("loadUsing")

        parentCat = intent.getStringExtra("parentCat")
        mainCat = intent.getStringExtra("mainCat")
        subCat = intent.getStringExtra("subCat")

        if(loadFor=="banner"){
            productViewModel.LoadBannersProducts(loadUsing!!)
        }else if(loadFor=="all"){
            productViewModel.LoadAllProducts()
        }else if(loadFor=="cat"){
            productViewModel.LoadProducts(parentCat!!, mainCat!!,subCat!!)
        }

        productList = ArrayList()
        productAdapter = ProductSimpleAdapter(this,productList,::deleteItem,::deleteFor,::deleteProductFromBanner,::deleteProductFromCat)

        binding.productsRecv.layoutManager = LinearLayoutManager(this)
        binding.productsRecv.adapter = productAdapter

        binding.addProduct.setOnClickListener {
            val intent = Intent(this,AddProduct::class.java)
            startActivity(intent)
        }

        productViewModel.product_liveData.observe(this,{

            when(it){
                is Response.Sucess -> {
                    productList.clear()

                    for(singleitem in it.data!!){
                        productList.add(0,singleitem)
                    }
                    
                    it.data.clear()
                    productAdapter.notifyDataSetChanged()
                    binding.loadingAnim3.visibility = View.GONE

                    if(productList.isEmpty()){
                        binding.noFoundText3.visibility = View.VISIBLE
                    }else{
                        binding.noFoundText3.visibility = View.GONE
                    }
                }

                is Response.Error -> {

                }
                is Response.Loading ->{

                }
            }
        })

        productViewModel.banner_product_liveData.observe(this,{

            when(it){
                is Response.Sucess -> {
                    productList.clear()

                    for(singleitem in it.data!!){
                        productList.add(0,singleitem)
                    }

                    it.data.clear()
                    productAdapter.notifyDataSetChanged()
                    binding.loadingAnim3.visibility = View.GONE

                    if(productList.isEmpty()){
                        binding.noFoundText3.visibility = View.VISIBLE
                    }else{
                        binding.noFoundText3.visibility = View.GONE
                    }
                }

                is Response.Error -> {

                }
                is Response.Loading ->{

                }
            }
        })

        productViewModel.cat_product_liveData.observe(this,{

            when(it){
                is Response.Sucess -> {
                    productList.clear()

                    for(singleitem in it.data!!){
                        productList.add(0,singleitem)
                    }

                    it.data.clear()
                    productAdapter.notifyDataSetChanged()
                    binding.loadingAnim3.visibility = View.GONE

                    if(productList.isEmpty()){
                        binding.noFoundText3.visibility = View.VISIBLE
                    }else{
                        binding.noFoundText3.visibility = View.GONE
                    }
                }

                is Response.Error -> {

                }
                is Response.Loading ->{

                }
            }
        })
    }

    fun deleteFor() : String{
        return loadFor!!
    }

    fun deleteItem(productId : String) {
        productViewModel.deleteProduct(productId)
    }
    fun deleteProductFromBanner(productId : String){
       productViewModel.deleteProductFromBanner(loadUsing!!,productId)
    }
    fun deleteProductFromCat(productId : String){
        productViewModel.deleteProductFromCat(parentCat!!,mainCat!!,subCat!!,productId)
    }
}