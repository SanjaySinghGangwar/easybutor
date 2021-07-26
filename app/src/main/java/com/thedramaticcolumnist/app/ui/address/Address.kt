package com.thedramaticcolumnist.app.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.thedramaticcolumnist.app.Database.mDatabase.mAddress
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.AddressBinding
import com.thedramaticcolumnist.app.databinding.ShowAddressBinding
import com.thedramaticcolumnist.app.ui.BottomSheets.addAddress.AddAddress


class Address : Fragment(), View.OnClickListener {
    private var _binding: AddressBinding? = null
    private val bind get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = AddressBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents()
    }

    private fun initAllComponents() {
        bind.addAddress.setOnClickListener(this)
        initRecycler()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addAddress -> {
                val add = AddAddress("")
                add.showNow(parentFragmentManager, "")
            }
        }
    }


    private fun initRecycler() {
        val option: FirebaseRecyclerOptions<ProductModel> =
            FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(mAddress!!.orderByChild("full_name"), ProductModel::class.java)
                .build()
        val recyclerAdapter =
            object : FirebaseRecyclerAdapter<ProductModel, AddressViewHolder>(option) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): AddressViewHolder {
                    val binding: ShowAddressBinding =
                        ShowAddressBinding.inflate(LayoutInflater.from(parent.context),
                            parent,
                            false)
                    return AddressViewHolder(requireContext(), binding)
                }

                override fun onBindViewHolder(
                    holder: AddressViewHolder,
                    position: Int,
                    model: ProductModel,
                ) {
                    holder.bind(model)
                    holder.delete.setOnClickListener { click ->
                        mAddress!!.child(getRef(position).key.toString()).removeValue()
                            .addOnSuccessListener {
                                mToast(requireContext(), "Removed")
                            }

                        notifyDataSetChanged()
                    }
                    holder.edit.setOnClickListener { click ->
                        val add = AddAddress(getRef(position).key.toString())
                        add.showNow(parentFragmentManager, "")
                    }

                }


            }

        bind.recycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()
    }
}