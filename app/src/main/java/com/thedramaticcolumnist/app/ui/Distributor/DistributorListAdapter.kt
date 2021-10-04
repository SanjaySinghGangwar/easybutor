package com.thedramaticcolumnist.app.ui.Distributor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thedramaticcolumnist.app.Model.DistributorListModel
import com.thedramaticcolumnist.app.databinding.VendorsLayoutBinding

class DistributorListAdapter(
    private val context: Context,
    private val listener: onClickListner
) : RecyclerView.Adapter<DistributorViewHolder>() {
    private val items = ArrayList<DistributorListModel>()

    interface onClickListner {
        fun onVendorSelect(id: String)
    }

    fun setItems(items: ArrayList<DistributorListModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DistributorViewHolder {
        val binding: VendorsLayoutBinding =
            VendorsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DistributorViewHolder(context, binding, listener)
    }

    override fun onBindViewHolder(holder: DistributorViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
