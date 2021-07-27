package com.thedramaticcolumnist.app.ui.address

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.databinding.ShowAddressBinding

class AddressViewHolder(
    private val context: Context,
    private val bind: ShowAddressBinding,
) : RecyclerView.ViewHolder(bind.root) {

    private lateinit var items: ProductModel
    var card = bind.card
    var delete = bind.delete
    var edit = bind.edit


    fun bind(data: ProductModel) {
        this.items = data
        bind.name.text = items.full_name
        bind.phone.text = items.phone
        bind.address.text =
            items.house_address + items.road_address + "\n" + items.city + items.state + "\n" + items.pinCode

    }
}