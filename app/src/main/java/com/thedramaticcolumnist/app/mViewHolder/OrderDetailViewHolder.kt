package com.thedramaticcolumnist.app.mViewHolder

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.databinding.OrderDetailLayoutBinding

class OrderDetailViewHolder (
    private val context: Context,
    private val bind: OrderDetailLayoutBinding,
) : RecyclerView.ViewHolder(bind.root) {


    var card: CardView = bind.card


    fun bind(item: ProductModel) {
        bind.name.text=item.product_name
        bind.price.text=item.price
        bind.shortDescription.text=item.short_description
        bind.quantity.text=item.quantity
    }


}