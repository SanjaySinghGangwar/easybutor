package com.thedramaticcolumnist.app.ui.Products

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.databinding.ProductLayoutBinding

class ProductsAdapter(private val context: Context, private val listener: ProductsFragment)
   /* RecyclerView.Adapter<ProductsViewHolder>() */{

   /* interface ItemListener {
        fun onClickedArticle(url: String)
        fun onClickedShare(title: String, description: String, urlToImage: String, url: String)
    }


    private val items = ArrayList<ProductModel>()

    fun setItems(items: List<ProductModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

   *//* override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding: ProductLayoutBinding=
            ProductLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(context,binding, listener)
    }*//*

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }*/

}