package com.ecomapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.admin.Factories.HomeVMF
import com.ecomapp.admin.ViewModels.HomeViewModel
import com.ecomapp.admin.databinding.ActivityMainBinding
import com.ecomapp.febric.Repositories.Response
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        supportActionBar?.hide()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_framelayout,Home())
        transaction.commit()

        binding.bottomNavigationView.setOnItemSelectedListener {

            var transaction = supportFragmentManager.beginTransaction()

            when(it.itemId){

                R.id.nav_home ->{
                    transaction.replace(R.id.home_framelayout,Home())
                }

                R.id.nav_shop ->{
                    transaction.replace(R.id.home_framelayout,Orders())
                }
            }
            transaction.commit()
            return@setOnItemSelectedListener true
        }

    }
}