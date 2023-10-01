package com.ecomapp.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.admin.Factories.mainVMF
import com.ecomapp.admin.ViewModels.MainViewModel
import com.ecomapp.admin.databinding.FragmentOrdersBinding
import com.ecomapp.febric.Adapters.DeliverdAdapter
import com.ecomapp.febric.Adapters.DeliveryItemAdapter
import com.ecomapp.febric.Models.OrderModel
import com.ecomapp.febric.Repositories.Response
import com.ecomapp.wear.Models.NotificationModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class Orders : Fragment() {

    lateinit var mainViewModel: MainViewModel
    lateinit var binding : FragmentOrdersBinding

    @Inject
    lateinit var mainVMF: mainVMF

    lateinit var orderList : ArrayList<OrderModel>
    lateinit var deliveryItemAdapter: DeliveryItemAdapter
    lateinit var deliveredAdapter: DeliverdAdapter

    var selectedTab : String = "PendingOrders"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_orders, container, false)

        mainViewModel = ViewModelProvider(requireActivity(),mainVMF).get(MainViewModel::class.java)
        mainViewModel.loadOrders(selectedTab)

        orderList = ArrayList()

        deliveryItemAdapter = DeliveryItemAdapter(requireActivity(),orderList,::deliveredOrder,::cancelOrder)
        deliveredAdapter = DeliverdAdapter(requireActivity(),orderList,selectedTab)

        binding.deliveryItemsRecv.layoutManager = LinearLayoutManager(activity)
        binding.deliveryItemsRecv.adapter = deliveryItemAdapter

        binding.topTabBar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                orderList.clear()
                binding.loadingAnim2.visibility = View.VISIBLE
                binding.noFoundText2.visibility = View.GONE

                when(tab?.position){
                    0 ->{
                        selectedTab  = "PendingOrders"
                        deliveryItemAdapter = DeliveryItemAdapter(requireActivity(),orderList,::deliveredOrder,::cancelOrder)
                        binding.deliveryItemsRecv.adapter = deliveryItemAdapter
                        mainViewModel.loadOrders(selectedTab)
                    }
                    1 ->{
                        selectedTab  = "DeliveredOrders"
                        deliveredAdapter = DeliverdAdapter(requireActivity(),orderList,selectedTab)
                        binding.deliveryItemsRecv.adapter = deliveredAdapter
                        mainViewModel.loadOrders(selectedTab)
                    }

                    2 ->{
                        selectedTab  = "CancelledOrders"
                        deliveredAdapter = DeliverdAdapter(requireActivity(),orderList,selectedTab)
                        binding.deliveryItemsRecv.adapter = deliveredAdapter
                        mainViewModel.loadOrders(selectedTab)
                    }
                }

                deliveryItemAdapter.notifyDataSetChanged()
                deliveredAdapter.notifyDataSetChanged()

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        mainViewModel.order_liveData.observe(viewLifecycleOwner,{
            when(it){
                is Response.Sucess -> {

                    orderList.clear()

                    for(singleitem in it.data!!){
                        orderList.add(0,singleitem)
                    }

                    binding.loadingAnim2.visibility = View.GONE

                    if(orderList.isEmpty()){
                        binding.noFoundText2.visibility = View.VISIBLE
                    }else{
                        binding.noFoundText2.visibility = View.GONE
                    }

                    it.data.clear()
                    
                    deliveryItemAdapter.notifyDataSetChanged()
                    deliveredAdapter.notifyDataSetChanged()
                }

                is Response.Error -> {

                }
                is Response.Loading ->{

                }
            }
        })

        return binding.root
    }

    fun deliveredOrder(orderId : String, userId : String,productId: String,productMainImage : String){
        mainViewModel.DeliveredOrder(orderId,userId,productId,productMainImage)
    }

    fun cancelOrder(orderId : String, userId : String,productId: String,productMainImage : String){
        mainViewModel.CancelOrder(orderId,userId,productId,productMainImage)
    }
}