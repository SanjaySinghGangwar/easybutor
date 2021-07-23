package com.thedramaticcolumnist.app.mViewHolder

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.Database.mDatabase.productDatabase
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.Model.cart
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.CartItemLayoutBinding


class CartViewHolder(
    private val context: Context,
    private val itemBinding: CartItemLayoutBinding,
    private val listener: ItemListener,
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var items: ProductModel
    var card: CardView = itemBinding.card
    var quantity = itemBinding.quantity
    var remove = itemBinding.remove
    var saveForLater = itemBinding.saveForLater
    var totalAmount: Int = 0

    interface ItemListener {
        fun onPayClicked(arrayListt: ArrayList<cart>)
    }

    fun bind(item: ProductModel) {
        this.items = item
        itemBinding.quantity.setSelection(items.quantity?.toInt()!!.minus(1))

        var amount: Int = 0
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
                if (snapshot.hasChild("price") && snapshot.hasChild("quantity")) {
                    ///itemBinding.price.text = snapshot.child("price").value.toString()
                    val price = snapshot.child("price").value.toString()
                    val quan = items.quantity?.toInt()
                    val total = (price.toInt() * quan!!)
                    itemBinding.price.text = total.toString()
                    amount += total

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


