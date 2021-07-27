package com.thedramaticcolumnist.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mLog
import com.thedramaticcolumnist.app.databinding.ProductImageLayoutBinding
import com.thedramaticcolumnist.app.mViewHolder.ProductsImagesViewHolder

class productImagesAdapter(private val context: Context, private val splitString: List<String>) :
    RecyclerView.Adapter<ProductsImagesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsImagesViewHolder {
        val binding: ProductImageLayoutBinding =
            ProductImageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsImagesViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: ProductsImagesViewHolder, position: Int) {
        holder.bind(splitString[position])
        Glide.with(context)
            .load(splitString[position])
            .error(R.drawable.ic_default_product)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView);
    }

    override fun getItemCount(): Int {
        return splitString.size
    }

}
