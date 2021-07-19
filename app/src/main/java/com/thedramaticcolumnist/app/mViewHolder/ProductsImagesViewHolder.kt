package com.thedramaticcolumnist.app.mViewHolder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.ProductImageLayoutBinding

class ProductsImagesViewHolder(
    private val context: Context,
    private val binding: ProductImageLayoutBinding,
) : RecyclerView.ViewHolder(binding.root) {

    val imageView = binding.image
    private lateinit var items: String

    fun bind(mList: String) {
        items = mList
        //Glide.with(context).load(items).into(binding.image);
        //mToast(context, items)
    }

}
