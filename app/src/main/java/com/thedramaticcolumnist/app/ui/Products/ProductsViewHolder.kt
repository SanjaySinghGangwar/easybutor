package com.thedramaticcolumnist.app.ui.Products

import android.content.Context
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.databinding.ProductLayoutBinding

class ProductsViewHolder(
    private val context: Context,
    private val itemBinding: ProductLayoutBinding,
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var items: ProductModel
    var card: CardView = itemBinding.card


    fun bind(item: ProductModel) {
        this.items = item
        Log.i("SANJAY ", "bind: $items")

        itemBinding.name.text = item.product_name
        itemBinding.price.text = "₹ " + item.price
        Glide.with(context).load(item.image_one).diskCacheStrategy(DiskCacheStrategy.ALL).into(itemBinding.image);
    }
}