package com.thedramaticcolumnist.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thedramaticcolumnist.app.Model.CartListModel
import com.thedramaticcolumnist.app.databinding.CartItemLayoutBinding
import com.thedramaticcolumnist.app.mViewHolder.CartViewHolder

class CartListAdapter (private val context: Context,
private val listener: onClickListner,
) : RecyclerView.Adapter<CartViewHolder>() {

    interface onClickListner {
        fun onRemoveCLick(id: String)
        fun onQuantityUpdate(id: String, quantity: String)
    }

    private val items = ArrayList<CartListModel>()

    fun setItems(items: ArrayList<CartListModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CartViewHolder {
        val binding: CartItemLayoutBinding =
            CartItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(context, binding, listener)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}