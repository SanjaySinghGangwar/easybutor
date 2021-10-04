package com.thedramaticcolumnist.app.ui.Search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thedramaticcolumnist.app.Model.ProductListModel
import com.thedramaticcolumnist.app.databinding.ProductLayoutBinding
import com.thedramaticcolumnist.app.ui.Products.ProductsViewHolder

class ProductsListAdapter(
    private val context: Context,
    private val listener: onClickListner
) : RecyclerView.Adapter<ProductsViewHolder>() {
    private val items = ArrayList<ProductListModel>()

    interface onClickListner {
        fun onItemSelect(id: String)
    }

    fun setItems(items: ArrayList<ProductListModel>) {

        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProductsViewHolder {
        val binding: ProductLayoutBinding =
            ProductLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(context, binding, listener)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}