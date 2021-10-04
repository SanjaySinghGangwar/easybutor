package com.thedramaticcolumnist.app.ui.Products.ProdutsBySeller

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thedramaticcolumnist.app.databinding.CategoryLayoutNameBinding

class CategoryAdapter(
    private val context: Context,
    private val listener: onClickListner
) : RecyclerView.Adapter<CategorySellerViewHolder>() {
    private val items = ArrayList<String>()

    interface onClickListner {
        fun onCategorySelect(categoryName: String)
    }

    fun setItems(items: ArrayList<String>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CategorySellerViewHolder {
        val binding: CategoryLayoutNameBinding =
            CategoryLayoutNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategorySellerViewHolder(context, binding, listener)
    }

    override fun onBindViewHolder(holder: CategorySellerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}