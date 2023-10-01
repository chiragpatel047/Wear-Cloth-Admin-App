package com.ecomapp.febric.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecomapp.admin.databinding.DeliveryItemSingleBinding
import com.ecomapp.febric.Models.OrderModel
import com.ecomapp.admin.R
import com.ecomapp.admin.databinding.DeleteDialogBinding
import com.ecomapp.admin.databinding.DetailsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class DeliveryItemAdapter(
    myContext: Context,
    myDeliveryItemModel: ArrayList<OrderModel>,
    val deliveredOrderFun: (String, String,String, String) -> Unit,
    val cancelOrderFun: (String, String,String, String) -> Unit

) : RecyclerView.Adapter<DeliveryItemAdapter.DeliveryItemViewHolder>() {

    var context = myContext
    var deliveryItemArrayList = myDeliveryItemModel

    class DeliveryItemViewHolder(devliveryItemBinding: DeliveryItemSingleBinding) :
        RecyclerView.ViewHolder(devliveryItemBinding.getRoot()) {

        var devliveryItemBinding: DeliveryItemSingleBinding
        init {
            this.devliveryItemBinding = devliveryItemBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryItemViewHolder {

        val binding: DeliveryItemSingleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.delivery_item_single, parent, false
        )
        return DeliveryItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return deliveryItemArrayList.size
    }

    override fun onBindViewHolder(holder: DeliveryItemViewHolder, position: Int) {

        var singleItem = deliveryItemArrayList.get(position)

        holder.devliveryItemBinding.orderno.text = "Order no : "+singleItem.orderId?.substring(6,14)
        holder.devliveryItemBinding.trackingno.text = singleItem.orderId
        holder.devliveryItemBinding.amount.text = singleItem.productPrice+"₹"
        holder.devliveryItemBinding.quantity.text = "1"

        holder.devliveryItemBinding.delivered.setOnClickListener {

            val dialogBinding : DeleteDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.delete_dialog,null,false)

            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogBinding.root).create()

            dialogBinding.subCatName.text = "Is Delivered ? "
            dialogBinding.subCatName2.text = " Are you sure do you want to mark this order as delivered ?"
            dialogBinding.btnYes.text = "Yes"
            dialogBinding.btnDiscard.text = "No"

            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.show()

            dialogBinding.btnYes.setOnClickListener{
                alertDialog.dismiss()
                deliveryItemArrayList.remove(singleItem)
                Toast.makeText(context,"Mark as Delivered",Toast.LENGTH_SHORT).show()
                deliveredOrderFun.invoke(singleItem.orderId!!, singleItem.userId!!,singleItem.productId!!,singleItem.productImage!!)
                notifyDataSetChanged()
            }

            dialogBinding.btnDiscard.setOnClickListener {
                alertDialog.dismiss()
            }

        }

        holder.devliveryItemBinding.cancel.setOnClickListener {

            val dialogBinding : DeleteDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.delete_dialog,null,false)

            val alertDialog = AlertDialog.Builder(context)
                .setView(dialogBinding.root).create()

            dialogBinding.subCatName.text = "Cancel Order"
            dialogBinding.subCatName2.text = " Are you sure do you want to cancel this order ?"
            dialogBinding.btnYes.text = "Yes"
            dialogBinding.btnDiscard.text = "No"

            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.show()

            dialogBinding.btnYes.setOnClickListener{
                alertDialog.dismiss()
                deliveryItemArrayList.remove(singleItem)
                Toast.makeText(context,"Order Cancelled",Toast.LENGTH_SHORT).show()
                cancelOrderFun.invoke(singleItem.orderId!!, singleItem.userId!!,singleItem.productId!!,singleItem.productImage!!)
                notifyItemRemoved(position)
                notifyDataSetChanged()
            }

            dialogBinding.btnDiscard.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        holder.itemView.setOnClickListener {

            val bottomSheetView : DetailsBottomSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.details_bottom_sheet,null,false)

            var bottomSheet = BottomSheetDialog(context,R.style.BottomSheetDialogTheme)
            bottomSheet.setContentView(bottomSheetView.root)
            bottomSheet.create()

            bottomSheet.show()

            Glide.with(context).load(singleItem.productImage).into(bottomSheetView.itemImage)
            bottomSheetView.itemSubTitle.text = singleItem.productTitle
            bottomSheetView.itemColor.text = "Size : " + singleItem.selectedSize
            bottomSheetView.trackingno6.text = singleItem.productPrice+"₹"
            bottomSheetView.fullName.text = singleItem.name
            bottomSheetView.middleAddress.text = singleItem.address
            bottomSheetView.topAddress.text = singleItem.city+", "+singleItem.state+" "+singleItem.zipCode
            bottomSheetView.itemPrice.text = singleItem.productPrice+"₹"
            bottomSheetView.trackingno3.text = singleItem.phoneNO
            bottomSheetView.deliveryDate.text = singleItem.deliveryDate

            bottomSheetView.continueShopping.setOnClickListener {
                bottomSheet.dismiss()
            }
        }
    }

}