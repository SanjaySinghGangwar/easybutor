package com.thedramaticcolumnist.app.ui.Products

import android.content.Context
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.thedramaticcolumnist.app.Model.ProductListModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.databinding.ProductLayoutBinding
import com.thedramaticcolumnist.app.ui.Search.ProductsListAdapter

class ProductsViewHolder(
    private val context: Context,
    private val bind: ProductLayoutBinding,
    private val listener: ProductsListAdapter.onClickListner,
) : RecyclerView.ViewHolder(bind.root), View.OnClickListener {

    private lateinit var item: ProductListModel
    var card: CardView = bind.card


    fun bind(data: ProductListModel) {
        this.item = data
        bind.name.text = item.detail.product_name
        bind.price.text = "₹ " + item.detail.price
        Glide
            .with(context)
            .load(item.detail.image_one)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.ic_error)
            .into(bind.image);

        bind.card.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card -> {
                listener.onItemSelect(item.id)
            }
        }
    }
}