package com.thedramaticcolumnist.app.mViewHolder

import android.content.Context
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.databinding.CategoryLayoutBinding

class CategoryViewHolder(
    private val context: Context,
    private val itemBinding: CategoryLayoutBinding,
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var items: ProductModel
    var card: CardView = itemBinding.card


    fun bind(item: ProductModel) {
        this.items = item
        Log.i("SANJAY ", "bind: $items")
        itemBinding.name.text = item.name
        Glide.with(context).load(item.icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(itemBinding.image);
    }
}