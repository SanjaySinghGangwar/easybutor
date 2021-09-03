package com.thedramaticcolumnist.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thedramaticcolumnist.app.Model.WishListModel
import com.thedramaticcolumnist.app.databinding.WishlistItemLayoutBinding
import com.thedramaticcolumnist.app.mViewHolder.WishListViewHolder

class WishListAdapter(
    private val context: Context,
    private val listener: onClickListner,
) : RecyclerView.Adapter<WishListViewHolder>() {

    interface onClickListner {
        fun onRemoveCLick(id: String)
        fun onQuantityUpdate(id: String, quantity: String)
    }

    private val items = ArrayList<WishListModel>()

    fun setItems(items: ArrayList<WishListModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): WishListViewHolder {
        val binding: WishlistItemLayoutBinding =
            WishlistItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishListViewHolder(context, binding, listener)
    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
