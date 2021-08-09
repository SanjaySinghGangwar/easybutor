package com.thedramaticcolumnist.app.ui.orderDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.thedramaticcolumnist.app.Database.mDatabase.myOrder
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.OrderDetailBinding
import com.thedramaticcolumnist.app.databinding.OrderDetailLayoutBinding
import com.thedramaticcolumnist.app.mViewHolder.OrderDetailViewHolder


class OrderDetail : Fragment() {

    private var _binding: OrderDetailBinding? = null
    private val bind get() = _binding!!
    val args: OrderDetailArgs by navArgs()
    lateinit var recyclerAdapter: FirebaseRecyclerAdapter<ProductModel, OrderDetailViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = OrderDetailBinding.inflate(inflater, container, false)
        return  bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents()
        showCartData()
    }


    private fun showCartData() {

        val option: FirebaseRecyclerOptions<ProductModel> =
            FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(myOrder!!.child(args.id), ProductModel::class.java)
                .build()
        recyclerAdapter =
            object : FirebaseRecyclerAdapter<ProductModel, OrderDetailViewHolder>(option) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): OrderDetailViewHolder {
                    val binding: OrderDetailLayoutBinding =
                        OrderDetailLayoutBinding.inflate(LayoutInflater.from(parent.context),
                            parent,
                            false)
                    return OrderDetailViewHolder(requireContext(), binding)
                }

                override fun onBindViewHolder(
                    holder: OrderDetailViewHolder,
                    position: Int,
                    model: ProductModel,
                ) {
                    holder.bind(model)
                }
            }

        bind.recycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()

    }

    private fun initAllComponents() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}