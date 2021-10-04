package com.thedramaticcolumnist.app.ui.Distributor

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.thedramaticcolumnist.app.Model.DistributorListModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.VendorsLayoutBinding

class DistributorViewHolder(
    private val context: Context,
    private val bind: VendorsLayoutBinding,
    private val listener: DistributorListAdapter.onClickListner,
) : RecyclerView.ViewHolder(bind.root), View.OnClickListener {
    private lateinit var items: DistributorListModel

    fun bind(data: DistributorListModel) {
        items = data

        //setData
        bind.name.text = items.details.name
        Glide.with(context)
            .load(items.details.profile_image)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.ic_error)
            .into(bind.image)
        bind.card.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card -> {
                listener.onVendorSelect(items.id)
            }
        }
    }
}
