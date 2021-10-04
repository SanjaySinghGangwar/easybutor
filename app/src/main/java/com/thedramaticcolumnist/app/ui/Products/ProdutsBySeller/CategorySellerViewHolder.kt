package com.thedramaticcolumnist.app.ui.Products.ProdutsBySeller

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.databinding.CategoryLayoutNameBinding

class CategorySellerViewHolder(
    private val context: Context,
    private val bind: CategoryLayoutNameBinding,
    private val listener: CategoryAdapter.onClickListner
) : RecyclerView.ViewHolder(bind.root), View.OnClickListener {

    private lateinit var items: String

    fun bind(item: String) {
        items = item
        bind.categoryName.text = item
        bind.card.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card -> {
                listener.onCategorySelect(items)
            }
        }
    }
}