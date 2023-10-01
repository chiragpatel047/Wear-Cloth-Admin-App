package com.ecomapp.admin.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.ecomapp.admin.R
import com.ecomapp.admin.databinding.ReviewImageSingleBinding

class AddPhotoAdapter(myContext: Context, myImageList: ArrayList<String>) :
    RecyclerView.Adapter<AddPhotoAdapter.AddImageViewHolder>() {

    var context = myContext
    var imageList = myImageList

    class AddImageViewHolder(reviewImageBinding: ReviewImageSingleBinding) :
        RecyclerView.ViewHolder(reviewImageBinding.getRoot()) {

        var reviewImageBinding: ReviewImageSingleBinding

        init {
            this.reviewImageBinding = reviewImageBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddImageViewHolder  {

            var binding : ReviewImageSingleBinding =  DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.review_image_single, parent, false)

            return AddImageViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: AddImageViewHolder, position: Int) {

        var singleImage = imageList.get(position)

        Glide.with(context)
            .load(singleImage)
            .into(holder.reviewImageBinding.reviewImage)

        holder.reviewImageBinding.removeImage.visibility = View.VISIBLE
        holder.reviewImageBinding.removeImage.setOnClickListener {
            imageList.removeAt(position)
            notifyDataSetChanged()
        }
    }
}