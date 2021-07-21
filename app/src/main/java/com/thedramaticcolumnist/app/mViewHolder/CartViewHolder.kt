package com.thedramaticcolumnist.app.mViewHolder

import android.content.Context
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.Database.mDatabase.productDatabase
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.CartItemLayoutBinding

class CartViewHolder(
    private val context: Context,
    private val itemBinding: CartItemLayoutBinding,
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var items: ProductModel
    var card: CardView = itemBinding.card
    var remove = itemBinding.remove
    var shortDescription = itemBinding.shortDescription


    fun bind(item: ProductModel) {
        this.items = item
        Log.i("SANJAY ", "bind: $items")
        itemBinding.name.text = item.id
        productDatabase.child(item.id.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("product_name")) {
                    itemBinding.name.text =
                        snapshot.child("product_name").value.toString()
                }
                if (snapshot.hasChild("short_description")) {
                    itemBinding.shortDescription.text =
                        snapshot.child("short_description").value.toString()
                }
                if (snapshot.hasChild("price")) {
                    itemBinding.price.text = "₹ " +
                            snapshot.child("price").value.toString()
                }
                if (snapshot.hasChild("image_one")) {
                    Glide.with(context)
                        .load(snapshot.child("image_one").value.toString())
                        .placeholder(R.drawable.ic_person)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(itemBinding.image);
                }
            }

            override fun onCancelled(error: DatabaseError) {
                mToast(context, error.message)
            }
        })

    }
}


