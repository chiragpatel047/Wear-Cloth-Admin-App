package com.ecomapp.admin.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecomapp.admin.Models.SubCatModel
import com.ecomapp.admin.Products
import com.ecomapp.admin.R
import com.ecomapp.admin.databinding.DeleteDialogBinding
import com.ecomapp.admin.databinding.SubCategorySingleBinding

class SubCatAdapter(
    myContext: Context,
    mySubCatList: ArrayList<SubCatModel>,
    val getParentCat: () -> String,
    val getMainCat: () -> String,
    val deleteSubCatFun: (String, String, String) -> Unit
) : RecyclerView.Adapter<SubCatAdapter.SubCatViewHolder>() {

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

        holder.subCatBinding.subCatName.text = singleCat.subCatName

        holder.subCatBinding.removeImage.setOnClickListener {

            val dialogBinding : DeleteDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.delete_dialog,null,false)

            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogBinding.root).create()

            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.show()

            dialogBinding.btnYes.setOnClickListener {
                alertDialog.dismiss()

                val parentCat : String = getParentCat.invoke()
                val mainCat : String = getMainCat.invoke()

                subCatList.remove(singleCat)
                notifyDataSetChanged()

                deleteSubCatFun.invoke(parentCat,mainCat, singleCat.subCatName!!)
            }

            dialogBinding.btnDiscard.setOnClickListener {
                alertDialog.dismiss()
            }

        }

        holder.itemView.setOnClickListener {

            val parentCat : String = getParentCat.invoke()
            val mainCat : String = getMainCat.invoke()

            val intent = Intent(context, Products::class.java)
            intent.putExtra("parentCat",parentCat)
            intent.putExtra("loadFor","cat")
            intent.putExtra("mainCat",mainCat)
            intent.putExtra("subCat",singleCat.subCatName)
            context.startActivity(intent)

        }
    }
}