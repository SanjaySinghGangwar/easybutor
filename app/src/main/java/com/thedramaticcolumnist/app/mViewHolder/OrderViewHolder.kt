package com.thedramaticcolumnist.app.mViewHolder

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.Database.mDatabase.myOrder
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mLog
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.OrderItemLayoutBinding

class OrderViewHolder(
    private val context: Context,
    private val bind: OrderItemLayoutBinding,
) : RecyclerView.ViewHolder(bind.root) {


    var card: CardView = bind.card


    fun bind(item: ProductModel) {
        bind.name.text=item.product_name
        bind.price.text=item.price
        bind.shortDescription.text=item.short_description
        bind.quantity.text=item.quantity
        bind.flag.text=item.flag

        Glide.with(context)
            .load(item.image_one)
            .placeholder(R.drawable.ic_person)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.ic_error)
            .into(bind.image)
        //mLog(item)
       /* myOrder!!.child(item).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.value.toString()
                mLog(data.substring(1, data.indexOf("=")))
                val subItem=data.substring(1, data.indexOf("="))
                myOrder!!.child(item).child(subItem).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild("product_name")) {
                            bind.name.text =
                                snapshot.child("product_name").value.toString()
                        }
                        if (snapshot.hasChild("image_one")) {
                            Glide.with(context)
                                .load(snapshot.child("image_one").value.toString())
                                .placeholder(R.drawable.ic_person)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(bind.image);

                        }
                        if (snapshot.hasChild("short_description")) {
                            bind.shortDescription.text =
                                snapshot.child("short_description").value.toString()
                        }
                        if (snapshot.hasChild("price")) {
                            bind.price.text =
                                snapshot.child("price").value.toString()
                        }
                        if (snapshot.hasChild("price")) {
                            bind.price.text =
                                snapshot.child("price").value.toString()
                        }
                        if (snapshot.hasChild("quantity")) {
                            bind.quantity.text =
                                snapshot.child("quantity").value.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        mToast(context, error.message)
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                mToast(context, error.message)
            }
        })*/

        /*mDatabase.productDatabase.child(item.id.toString()).addValueEventListener(object :
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
                mUtils.mToast(context, error.message)
            }
        })*/

    }


}