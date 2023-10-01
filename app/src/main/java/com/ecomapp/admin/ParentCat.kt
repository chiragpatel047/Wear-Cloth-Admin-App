package com.ecomapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ecomapp.admin.databinding.ActivityParentCatBinding

class ParentCat : AppCompatActivity() {

    lateinit var binding: ActivityParentCatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_parent_cat)
        supportActionBar?.hide()

        binding.mens.setOnClickListener {
            val intent = Intent(this,MainCat::class.java)
            intent.putExtra("parentCat","Mens")
            startActivity(intent)
        }

        binding.womens.setOnClickListener {
            val intent = Intent(this,MainCat::class.java)
            intent.putExtra("parentCat","Womens")
            startActivity(intent)
        }

        binding.kids.setOnClickListener {
            val intent = Intent(this,MainCat::class.java)
            intent.putExtra("parentCat","Kids")
            startActivity(intent)
        }
    }

}