package com.ecomapp.febric.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecomapp.admin.SubCategory
import com.ecomapp.admin.Models.MainCatModel
import com.ecomapp.admin.R
import com.ecomapp.admin.databinding.DeleteDialogBinding
import com.ecomapp.admin.databinding.MainCategorySingleBinding

class MainCatAdapter(
    myContext: Context,
    myMaimCatList: ArrayList<MainCatModel>,
    val onCatClicked: () -> String,
    val deleteMainCatFun: (String, String) -> Unit,
) : RecyclerView.Adapter<MainCatAdapter.MainCatViewHolder>() {

    var context = myContext
    var mainCatList = myMaimCatList
    class MainCatViewHolder(mainCatBinding: MainCategorySingleBinding) :
        RecyclerView.ViewHolder(mainCatBinding.getRoot()) {

        var mainCatBinding: MainCategorySingleBinding

        init {
            this.mainCatBinding = mainCatBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCatViewHolder {
        val binding: MainCategorySingleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.main_category_single, parent, false
        )

        return MainCatViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return mainCatList.size
    }

    override fun onBindViewHolder(holder: MainCatViewHolder, position: Int) {
        val singleMainCat = mainCatList.get(position)

        Glide.with(context).load(singleMainCat.mainCatImage).into(holder.mainCatBinding.mainCatItemImage)
        holder.mainCatBinding.mainCatItemName.text = singleMainCat.mainCatName

        holder.mainCatBinding.removeImage.setOnClickListener {

            val dialogBinding : DeleteDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.delete_dialog,null,false)

            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogBinding.root).create()

            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.show()

            dialogBinding.btnYes.setOnClickListener {
                alertDialog.dismiss()
                mainCatList.remove(singleMainCat)
                notifyDataSetChanged()
                deleteMainCatFun.invoke(onCatClicked.invoke(), singleMainCat.mainCatName!!)
            }

            dialogBinding.btnDiscard.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SubCategory::class.java)
            intent.putExtra("mainCat",singleMainCat.mainCatName)
            intent.putExtra("parentCat",onCatClicked.invoke())
            context.startActivity(intent)
        }
    }
}