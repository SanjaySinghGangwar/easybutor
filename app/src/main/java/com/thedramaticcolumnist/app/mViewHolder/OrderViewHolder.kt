package com.thedramaticcolumnist.app.mViewHolder

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.databinding.OrderItemLayoutBinding

class OrderViewHolder(
    private val context: Context,
    private val bind: OrderItemLayoutBinding,
) : RecyclerView.ViewHolder(bind.root) {


    var card: CardView = bind.card


    fun bind(item: ProductModel) {
        bind.name.text = item.product_name
        bind.price.text = item.price
        bind.shortDescription.text = item.short_description
        bind.quantity.text = item.quantity
        bind.flag.text = item.flag

        Glide.with(context)
            .load(item.image_one)
            .placeholder(R.drawable.ic_person)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.ic_error)
            .into(bind.image)

    }


}