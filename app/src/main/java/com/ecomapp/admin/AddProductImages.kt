package com.ecomapp.admin

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.ecomapp.admin.Adapters.AddPhotoAdapter
import com.ecomapp.admin.databinding.ActivityAddProductImagesBinding

class AddProductImages : AppCompatActivity() {

    lateinit var binding: ActivityAddProductImagesBinding
    lateinit var addImageAdapter : AddPhotoAdapter

    companion object{
        lateinit var addImageArrayList : ArrayList<String>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product_images)
        supportActionBar?.hide()

        addImageArrayList = ArrayList()
        addImageAdapter = AddPhotoAdapter(this,addImageArrayList)

        binding.addPhotosRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.addPhotosRecyclerview.adapter = addImageAdapter

        binding.btnNext.setOnClickListener {
            if(addImageArrayList.isEmpty()){
                Toast.makeText(this,"Please select images",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val intent = Intent(this,SelectSize::class.java)
            startActivityIfNeeded(intent,20)
        }

        binding.addImage.setOnClickListener {
            if (Build.VERSION.SDK_INT < 19) {
                var intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Choose Pictures")
                    , 1
                )
            }
            else { // For latest versions API LEVEL 19+
                var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent,1);
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            // if multiple images are selected
            if (data?.getClipData() != null) {
                var count = data.clipData?.itemCount

                for (i in 0..count!! - 1) {
                    var imageUri: Uri = data.clipData?.getItemAt(i)?.uri!!
                    addImageArrayList.add(imageUri.toString())
                }
                addImageAdapter.notifyDataSetChanged()

            } else if (data?.getData() != null) {
                // if single image is selected

                var imageUri: Uri = data.data!!
                addImageArrayList.add(imageUri.toString())
                addImageAdapter.notifyDataSetChanged()
            }

        }

    }
}