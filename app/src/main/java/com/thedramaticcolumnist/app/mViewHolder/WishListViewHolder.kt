package com.thedramaticcolumnist.app.mViewHolder

import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.Database.mDatabase.productDatabase
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils
import com.thedramaticcolumnist.app.adapter.WishListAdapter
import com.thedramaticcolumnist.app.Model.WishListModel
import com.thedramaticcolumnist.app.databinding.WishlistItemLayoutBinding

class WishListViewHolder(
    private val context: Context,
    private val bind: WishlistItemLayoutBinding,
    private val listener: WishListAdapter.onClickListner,
) : RecyclerView.ViewHolder(bind.root), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var items: WishListModel
    var card: CardView = bind.card
    var remove = bind.remove

    fun bind(data: WishListModel) {
        var amount = 0
        this.items = data

        bind.quantity.setSelection(items.details.quantity?.toInt()!!.minus(1))
        productDatabase.child(items.details.id.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                bind.name.text = snapshot.child("product_name").value.toString()
                bind.shortDescription.text = snapshot.child("short_description").value.toString()

                Glide.with(context)
                    .load(snapshot.child("image_one").value.toString())
                    .placeholder(R.drawable.ic_person)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_error)
                    .into(bind.image);
                val price = snapshot.child("price").value.toString()
                val quan = items.details.quantity?.toInt()
                val total = (price.toInt() * quan!!)
                bind.price.text = total.toString()
                amount += total

            }

            override fun onCancelled(error: DatabaseError) {
                mUtils.mToast(context, error.message)
            }
        })
        bind.remove.setOnClickListener(this)
        bind.quantity.onItemSelectedListener = this
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