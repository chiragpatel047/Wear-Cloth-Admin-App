package com.ecomapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.admin.Factories.AddMainCatVMF
import com.ecomapp.admin.Factories.MainCatVMF
import com.ecomapp.admin.Models.BannerModel
import com.ecomapp.admin.Models.MainCatModel
import com.ecomapp.admin.ViewModels.AddMainCatViewModel
import com.ecomapp.admin.databinding.ActivityAddMainCatBinding
import com.ecomapp.admin.databinding.LoadingDialogBinding
import com.ecomapp.febric.Repositories.Response
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class AddMainCat : AppCompatActivity() {

    lateinit var binding : ActivityAddMainCatBinding

    var catImageUri : String? = null
    lateinit var addMainCatViewModel: AddMainCatViewModel

    @Inject
    lateinit var addMainCatVMF: AddMainCatVMF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_main_cat)
        supportActionBar?.hide()

        val parentCat = intent.getStringExtra("parentCat")
        val bannerName = intent.getStringExtra("cover_name")
        val banner_pos = intent.getStringExtra("banner_pos")

        val dialogBinding : LoadingDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.loading_dialog,null,false)

        val loadingDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root).create()

        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingDialog.setCancelable(false)

        if(bannerName!=null){
            binding.textView14.text="Edit Cover"
            binding.catName.editText?.setText(bannerName)
        }

        addMainCatViewModel = ViewModelProvider(this,addMainCatVMF).get(AddMainCatViewModel::class.java)

        binding.selectImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 1)

        }


        binding.btnAdd.setOnClickListener {
            val catName = binding.catName.editText?.text.toString()
            if(catName.isEmpty()){
                Toast.makeText(this,"Can't be empty",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (binding.imageSelectedIndicator.text.equals("No image selected")){
                Toast.makeText(this,"Please select image",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            loadingDialog.show()

            if(bannerName==null){
                val mainCatModel = MainCatModel(catName,catImageUri)
                addMainCatViewModel.AddMainCategory(parentCat!!,mainCatModel)

                addMainCatViewModel.add_liveData.observe(this,{
                    when(it){
                        is Response.Sucess -> {
                            loadingDialog.dismiss()
                            Toast.makeText(this,"Added successfully",Toast.LENGTH_LONG).show()
                            finish()
                        }
                        is Response.Error -> {
                            loadingDialog.dismiss()
                            Toast.makeText(this,it.errorMsg,Toast.LENGTH_LONG).show()
                        }
                        is Response.Loading -> {

                        }
                    }
                })

            }else{
                val bannerModel = BannerModel(catImageUri,binding.catName.editText?.text.toString())
                addMainCatViewModel.UpdateBanners(banner_pos!!,bannerModel)

                addMainCatViewModel.update_liveData.observe(this,{
                    when(it){
                        is Response.Sucess -> {
                            loadingDialog.dismiss()
                            Toast.makeText(this,"Added successfully",Toast.LENGTH_LONG).show()
                            finish()
                        }
                        is Response.Error -> {
                            loadingDialog.dismiss()
                            Toast.makeText(this,it.errorMsg,Toast.LENGTH_LONG).show()
                        }
                        is Response.Loading -> {

                        }
                    }
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==1 && resultCode== RESULT_OK && data!=null){
            catImageUri = data.data.toString()
            binding.imageSelectedIndicator.text=catImageUri
        }
    }
}