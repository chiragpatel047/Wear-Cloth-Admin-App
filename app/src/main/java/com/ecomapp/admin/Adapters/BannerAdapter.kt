package com.ecomapp.admin.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecomapp.admin.AddMainCat
import com.ecomapp.admin.Models.BannerModel
import com.ecomapp.admin.Products
import com.ecomapp.admin.R
import com.ecomapp.admin.databinding.DeliveredSingleBinding
import com.ecomapp.admin.databinding.MainCategorySingleBinding
import com.ecomapp.febric.Adapters.DeliverdAdapter

class BannerAdapter(myContext : Context , myList : ArrayList<BannerModel> )
    : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    val context = myContext
    val bannerList = myList

    class BannerViewHolder(mainCategorySingleBinding: MainCategorySingleBinding) :
        RecyclerView.ViewHolder(mainCategorySingleBinding.getRoot()) {

        var mainCategorySingleBinding: MainCategorySingleBinding
        init {
            this.mainCategorySingleBinding = mainCategorySingleBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding: MainCategorySingleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.main_category_single, parent, false
        )

        return BannerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {

        val singleItem = bannerList.get(position)

        Glide.with(context)
            .load(singleItem.mainImage)
            .into(holder.mainCategorySingleBinding.mainCatItemImage)

        holder.mainCategorySingleBinding.mainCatItemName.text = singleItem.mainTitle
        holder.mainCategorySingleBinding.editCover.visibility = View.VISIBLE
        holder.mainCategorySingleBinding.removeImage3.visibility = View.VISIBLE
        holder.mainCategorySingleBinding.removeImage.visibility = View.GONE

        holder.itemView.setOnClickListener{
            val bannerPos = position+1

            val intent = Intent(context,Products::class.java)
            intent.putExtra("loadFor","banner")
            intent.putExtra("loadUsing",bannerPos.toString())
            context.startActivity(intent)
        }

        holder.mainCategorySingleBinding.editCover.setOnClickListener {
            val bannerPos = position+1

            val intent = Intent(context,AddMainCat::class.java)
            intent.putExtra("cover_name",singleItem.mainTitle)
            intent.putExtra("banner_pos",bannerPos.toString())
            context.startActivity(intent)
        }
    }
}