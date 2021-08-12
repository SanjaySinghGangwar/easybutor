package com.thedramaticcolumnist.app.mViewHolder

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.Database.mDatabase
import com.thedramaticcolumnist.app.Database.mDatabase.uID
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils
import com.thedramaticcolumnist.app.Utils.mUtils.mLog
import com.thedramaticcolumnist.app.databinding.ComfirmOrderLayoutBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ComfirmOrderViewHolder(
    private val context: Context,
    private val itemBinding: ComfirmOrderLayoutBinding,
    var listChild: HashMap<String, HashMap<String, String>>,
    private val listener: ItemListener,
    private val address:String,
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var items: ProductModel
    var card: CardView = itemBinding.card

    interface ItemListener {
        fun onClicked(uid:  HashMap<String, HashMap<String, String>>)
        fun createOrder(orderHash:HashMap<String, String> )
    }
    fun bind(item: ProductModel) {
        this.items = item
        itemBinding.quantity.text = items.quantity
        var amount = 0
        mDatabase.productDatabase.child(item.id.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var hashMap: HashMap<String, String> = HashMap<String, String>()
                hashMap["product_id"] = items.id.toString()
                if (snapshot.hasChild("product_name")) {
                    itemBinding.name.text =
                        snapshot.child("product_name").value.toString()
                    hashMap["product_name"] = snapshot.child("product_name").value.toString()
                }
                if (snapshot.hasChild("short_description")) {
                    itemBinding.shortDescription.text =
                        snapshot.child("short_description").value.toString()
                    hashMap["short_description"] =
                        snapshot.child("short_description").value.toString()
                }
                if (snapshot.hasChild("seller")) {
                    hashMap["seller"] = snapshot.child("seller").value.toString()
                }
                if (snapshot.hasChild("price") && snapshot.hasChild("quantity")) {
                    val price = snapshot.child("price").value.toString()
                    val quan = items.quantity?.toInt()
                    val total = (price.toInt() * quan!!)
                    itemBinding.price.text = total.toString()
                    amount += total

                    hashMap["price"] = price
                    hashMap["quantity"] = quan.toString()
                    hashMap["amount"] = amount.toString()
                }
                if (snapshot.hasChild("token") ) {
                    hashMap["token"]= snapshot.child("token").value.toString()
                }
                if (snapshot.hasChild("image_one")) {
                    Glide.with(context)
                        .load(snapshot.child("image_one").value.toString())
                        .placeholder(R.drawable.ic_person)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(itemBinding.image);
                    hashMap["image_one"] = snapshot.child("image_one").value.toString()
                }

                val timestamp =
                    SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) + Random().nextInt(
                        1000000)
                listChild[timestamp] = hashMap
                hashMap["buyer"]=uID
                hashMap["orderId"]=uID+timestamp
                hashMap["date"]=SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())
                hashMap["address"]=address
               listener. createOrder(hashMap)

            }


            override fun onCancelled(error: DatabaseError) {
                mUtils.mToast(context, error.message)
            }
        })

        listener.onClicked(listChild)
    }


}