package com.ecomapp.admin.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecomapp.admin.Models.SubCatModel
import com.ecomapp.admin.R
import com.ecomapp.admin.databinding.SubCategorySingleBinding
import com.ecomapp.febric.Models.SizeModel

class SizeAdapter(
    myContext: Context,
    mySubCatList: ArrayList<SizeModel>
) : RecyclerView.Adapter<SizeAdapter.SubCatViewHolder>() {

    var context = myContext
    var subCatList = mySubCatList

    class SubCatViewHolder(subCatBinding: SubCategorySingleBinding) :
        RecyclerView.ViewHolder(subCatBinding.getRoot()) {

        var subCatBinding: SubCategorySingleBinding

        init {
            this.subCatBinding = subCatBinding
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCatViewHolder {
        val binding: SubCategorySingleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.sub_category_single, parent, false
        )

        return SubCatViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return subCatList.size
    }

    override fun onBindViewHolder(holder: SubCatViewHolder, position: Int) {
        var singleCat = subCatList.get(position)

        holder.subCatBinding.subCatName.text = singleCat.SizeName

        holder.subCatBinding.removeImage.setOnClickListener {
            subCatList.remove(singleCat)
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener {

//            val intent = Intent(context, Products::class.java)
//            intent.putExtra("parentCat",parentCat)
//            intent.putExtra("mainCat",mainCat)
//            intent.putExtra("subCat",singleCat.subCatName)
//            context.startActivity(intent)

        }
    }
}