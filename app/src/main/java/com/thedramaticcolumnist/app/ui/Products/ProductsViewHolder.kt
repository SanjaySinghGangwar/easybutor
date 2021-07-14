package com.thedramaticcolumnist.app.ui.Products

import android.content.Context
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.material.textfield.TextInputEditText
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.databinding.ProductLayoutBinding

class ProductsViewHolder(
    private val context: Context,
    private val itemBinding: ProductLayoutBinding,
    private val listener: FirebaseRecyclerAdapter<ProductModel, ProductsViewHolder>,
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var items: ProductModel
    var card: CardView = itemBinding.card


    fun bind(item: ProductModel) {
        this.items = item
        Log.i("SANJAY ", "bind: $items")

        itemBinding.name.text = item.product_name
        itemBinding.price.text = "₹ " + item.price
        Glide.with(context).load(item.image_one).into(itemBinding.image);
    }


}