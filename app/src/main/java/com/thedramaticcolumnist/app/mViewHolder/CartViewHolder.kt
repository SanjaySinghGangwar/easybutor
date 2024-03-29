package com.thedramaticcolumnist.app.mViewHolder

import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.Database.mDatabase.productDatabase
import com.thedramaticcolumnist.app.Model.CartListModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.adapter.CartListAdapter
import com.thedramaticcolumnist.app.databinding.CartItemLayoutBinding


class CartViewHolder(
    private val context: Context,
    private val bind: CartItemLayoutBinding,
    private val listener: CartListAdapter.onClickListner,
) : RecyclerView.ViewHolder(bind.root), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private lateinit var items: CartListModel

    fun bind(data: CartListModel) {
        this.items = data
        bind.quantity.setSelection(items.details.quantity?.toInt()!!.minus(1))
        bind.quantity.onItemSelectedListener = this
        var amount = 0
        productDatabase.child(items.details.id.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                bind.name.text =
                    snapshot.child("product_name").value.toString()

                bind.shortDescription.text =
                    snapshot.child("short_description").value.toString()


                val price = snapshot.child("price").value.toString()
                val quan = items.details.quantity?.toInt()
                val total = (price.toInt() * quan!!)
                bind.price.text = total.toString()
                amount += total


                Glide.with(context)
                    .load(snapshot.child("image_one").value.toString())
                    .placeholder(R.drawable.ic_person)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_error)
                    .into(bind.image);


            }


            override fun onCancelled(error: DatabaseError) {
                mToast(context, error.message)
            }
        })
        bind.remove.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.remove -> {
                listener.onRemoveCLick(items.id)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        listener.onQuantityUpdate(items.id, parent?.selectedItem.toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


}


