package com.ecomapp.febric.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecomapp.admin.AddProduct
import com.ecomapp.admin.R
import com.ecomapp.admin.databinding.DeleteDialogBinding
import com.ecomapp.admin.databinding.LoadingDialogBinding
import com.ecomapp.admin.databinding.ProductSimpleSingleItemBinding
import com.ecomapp.febric.Models.ProuctModel

class ProductSimpleAdapter(
    myContext: Context,
    myItemModel: ArrayList<ProuctModel>,
    val deleteItem: (String) -> Unit,
    val deleteForFun: () -> String,
    val deleteProductFromBannerFun: (String) -> Unit,
    val deleteProductFromCatFun : (String) -> Unit
) : RecyclerView.Adapter<ProductSimpleAdapter.ProductSimpleViewHolder>() {

    var context = myContext
    var productList = myItemModel

    class ProductSimpleViewHolder(productSimpleItemBinding: ProductSimpleSingleItemBinding) :
        RecyclerView.ViewHolder(productSimpleItemBinding.getRoot()) {

        var productSimpleItemBinding: ProductSimpleSingleItemBinding

        init {
            this.productSimpleItemBinding = productSimpleItemBinding
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSimpleViewHolder {
        val binding: ProductSimpleSingleItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.product_simple_single_item, parent, false
        )

        return ProductSimpleViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductSimpleViewHolder, position: Int) {

        var singleItem = productList.get(position)

        Glide.with(context).load(singleItem.productMainImage).into(holder.productSimpleItemBinding.itemImage)
        holder.productSimpleItemBinding.itemText.text = singleItem.productTitle
        holder.productSimpleItemBinding.itemName.text = singleItem.productSubTitle

        holder.productSimpleItemBinding.itemRating.rating = singleItem.rate!!
        holder.productSimpleItemBinding.itemNumberOfRating.text = "("+singleItem.noOfRating.toString().replace(".0","").toString()+")"

        holder.productSimpleItemBinding.itemOldPrice.text = singleItem.productOldPrice+"₹"
        holder.productSimpleItemBinding.itemNewPrice.text = singleItem.productPrice+"₹"

        holder.productSimpleItemBinding.removeImage.setOnClickListener {

            val dialogBinding : DeleteDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.delete_dialog,null,false)

            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogBinding.root).create()

            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.show()

            dialogBinding.btnYes.setOnClickListener {
                alertDialog.dismiss()

                val deleteFor = deleteForFun.invoke()

                productList.remove(singleItem)
                notifyDataSetChanged()

                if(deleteFor=="banner"){
                    deleteProductFromBannerFun.invoke(singleItem.productId!!)
                }else if(deleteFor=="all"){
                    deleteItem.invoke(singleItem.productId!!)
                }else if(deleteFor=="cat"){
                    deleteProductFromCatFun.invoke(singleItem.productId!!)
                }
            }

            dialogBinding.btnDiscard.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, AddProduct::class.java)
            intent.putExtra("productId",singleItem.productId)
            context.startActivity(intent)
        }
    }
}