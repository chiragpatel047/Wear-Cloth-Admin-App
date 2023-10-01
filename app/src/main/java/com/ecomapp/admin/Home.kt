package com.ecomapp.admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.admin.Factories.HomeVMF
import com.ecomapp.admin.ViewModels.HomeViewModel
import com.ecomapp.admin.databinding.FragmentHomeBinding
import com.ecomapp.admin.databinding.NotificationDialogBinding
import com.ecomapp.febric.Repositories.Response
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Home : Fragment() {

    lateinit var binding : FragmentHomeBinding

    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var homeVMF: HomeVMF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)

        homeViewModel = ViewModelProvider(requireActivity(),homeVMF).get(HomeViewModel::class.java)

        binding.sendNotification.setOnClickListener {

            val dialogBinding : NotificationDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),R.layout.notification_dialog,null,false)

            val alertDialog = AlertDialog.Builder(requireActivity())
                .setView(dialogBinding.root).create()

            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.show()

            dialogBinding.btnYes.setOnClickListener {
                alertDialog.dismiss()
                val title = dialogBinding.productTitle.editText?.text.toString()
                val desc = dialogBinding.productSubtitle.editText?.text.toString()

                if(title.isEmpty() or desc.isEmpty()){
                    Toast.makeText(activity,"Please fill details", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                homeViewModel.sendFBNotification(title,desc)
            }

            dialogBinding.btnDiscard.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        homeViewModel.notify_liveData.observe(requireActivity(),{
            when(it){
                is Response.Sucess ->{
                    Toast.makeText(activity,"Notification Sent", Toast.LENGTH_LONG).show()
                }

                is Response.Error -> {

                }
                is Response.Loading -> {

                }
            }
        })

        binding.banners.setOnClickListener {
            val intent = Intent(activity,Banners::class.java)
            startActivity(intent)
        }

        binding.categories.setOnClickListener {
            val intent = Intent(activity,ParentCat::class.java)
            startActivity(intent)
        }

        binding.products.setOnClickListener {
            val intent = Intent(activity,Products::class.java)
            intent.putExtra("loadFor","all")
            startActivity(intent)
        }

        return binding.root
    }

}