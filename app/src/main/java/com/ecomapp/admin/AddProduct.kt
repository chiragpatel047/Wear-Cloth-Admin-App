package com.ecomapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.admin.Factories.AddProductVMF
import com.ecomapp.admin.ViewModels.AddProductViewModel
import com.ecomapp.admin.databinding.ActivityAddProductBinding
import com.ecomapp.admin.databinding.LoadingDialogBinding
import com.ecomapp.febric.Models.ProuctModel
import com.ecomapp.febric.Repositories.Response
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class AddProduct : AppCompatActivity() {

    lateinit var binding : ActivityAddProductBinding

    var productId : String? = ""

    lateinit var addProductViewModel: AddProductViewModel

    @Inject
    lateinit var addProductVMF: AddProductVMF

    var productMainImage : String = ""
    var noOfRating : Float = 0F
    var rate : Float = 0F

    companion object{
        var productTitle : String = ""
        var productSubTitle : String = ""
        var productDesc : String = ""
        var productOldPrice : String = ""
        var productPrice : String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_product)
        supportActionBar?.hide()

        addProductViewModel = ViewModelProvider(this,addProductVMF).get(AddProductViewModel::class.java)

        productId = intent.getStringExtra("productId")

        val dialogBinding : LoadingDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.loading_dialog,null,false)

        val loadingDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root).create()

        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        if(productId==null){

        }else {
            addProductViewModel.LoadSingleProduct(productId!!)
            binding.btnNext.setText("UPDATE")

            addProductViewModel.product_liveData.observe(this,{
                when(it){
                    is Response.Sucess -> {

                        productTitle = it.data?.productTitle!!
                        productSubTitle = it.data?.productSubTitle!!
                        productDesc = it.data?.productDesc!!
                        productOldPrice = it.data?.productOldPrice!!
                        productPrice = it.data?.productPrice!!
                        productMainImage = it.data?.productMainImage!!
                        noOfRating = it.data?.noOfRating!!
                        rate = it.data?.rate!!

                        binding.productTitle.editText?.setText(productTitle)
                        binding.productSubtitle.editText?.setText(productSubTitle)
                        binding.productDesc.editText?.setText(productDesc)
                        binding.productOldprice.editText?.setText(productOldPrice)
                        binding.productPrice.editText?.setText(productPrice)

                    }

                    is Response.Error -> {

                    }
                    is Response.Loading ->{

                    }
                }
            })

        }

        binding.btnNext.setOnClickListener {

            productTitle = binding.productTitle.editText?.text.toString()
            productSubTitle = binding.productSubtitle.editText?.text.toString()
            productDesc = binding.productDesc.editText?.text.toString()
            productOldPrice = binding.productOldprice.editText?.text.toString()
            productPrice = binding.productPrice.editText?.text.toString()

            if(productTitle.isEmpty() ||
                productSubTitle.isEmpty() ||
                productDesc.isEmpty() ||
                productOldPrice.isEmpty() ||
                productPrice.isEmpty()){

                Toast.makeText(this,"Please filled all details",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(productId==null){
                val intent = Intent(this,AddProductImages::class.java)
                startActivityIfNeeded(intent,30)
            }else {
                loadingDialog.show()

                val prouctModel = ProuctModel(productId, productTitle, productSubTitle, productDesc,
                    productOldPrice, productPrice,productMainImage,noOfRating,rate)
                addProductViewModel.updateProduct(prouctModel)

                addProductViewModel.addProduct_liveData.observe(this,{
                    when(it){
                        is Response.Sucess -> {
                            loadingDialog.dismiss()
                            Toast.makeText(this,"Updated successfully",Toast.LENGTH_LONG).show()
                            finish()
                        }

                        is Response.Error -> {
                            Toast.makeText(this,it.errorMsg,Toast.LENGTH_LONG).show()

                        }
                        is Response.Loading ->{

                        }
                    }
                })
            }
        }
    }
}