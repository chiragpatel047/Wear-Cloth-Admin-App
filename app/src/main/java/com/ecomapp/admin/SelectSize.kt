package com.ecomapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.admin.Adapters.SizeAdapter
import com.ecomapp.admin.databinding.ActivitySelectSizeBinding
import com.ecomapp.admin.databinding.AddSizeDialogBinding
import com.ecomapp.febric.Models.SizeModel

class SelectSize : AppCompatActivity() {

    lateinit var binding : ActivitySelectSizeBinding

    lateinit var sizeAdapter: SizeAdapter

    companion object{
        lateinit var sizeList : ArrayList<SizeModel>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_select_size)
        supportActionBar?.hide()

        sizeList = ArrayList()
        sizeAdapter = SizeAdapter(this,sizeList)

        binding.sizeRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.sizeRecyclerview.adapter = sizeAdapter

        binding.addSize.setOnClickListener {
            val dialogBinding : AddSizeDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.add_size_dialog,null,false)

            val dialogBuilder : AlertDialog.Builder = AlertDialog.Builder(this)
            dialogBuilder.setView(dialogBinding.root)

            val alertDialog : AlertDialog = dialogBuilder.create()
            alertDialog.show()

            dialogBinding.btnNext.setOnClickListener {
                val sizeName = dialogBinding.size.editText?.text.toString()

                if(sizeName.isEmpty()){
                    Toast.makeText(this,"Can't be empty",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                sizeList.add(SizeModel(sizeName))
                sizeAdapter.notifyDataSetChanged()
                alertDialog.dismiss()
            }
        }

        binding.btnNext.setOnClickListener {
            if(sizeList.isEmpty()){
                Toast.makeText(this,"Please add atleast one size",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val intent = Intent(this,SelectCategories::class.java)
            startActivityIfNeeded(intent,10)
        }
    }
}