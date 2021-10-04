package com.thedramaticcolumnist.app.ui.Search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.thedramaticcolumnist.app.Database.mDatabase.mDistributors
import com.thedramaticcolumnist.app.Database.mDatabase.productDatabase
import com.thedramaticcolumnist.app.Model.DistributorListDetails
import com.thedramaticcolumnist.app.Model.DistributorListModel
import com.thedramaticcolumnist.app.Model.ProductListModel
import com.thedramaticcolumnist.app.Model.ProductdetailModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mLog
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.SearchBinding
import com.thedramaticcolumnist.app.ui.Distributor.DistributorListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class Search : Fragment(), CompoundButton.OnCheckedChangeListener,
    TextWatcher, DistributorListAdapter.onClickListner, ProductsListAdapter.onClickListner {

    private var _binding: SearchBinding? = null
    private val bind get() = _binding!!
    var searchedDistributor = ""
    var searchedproducts = ""

    private var distributorList: ArrayList<DistributorListModel> = ArrayList()
    private var distributorResult: ArrayList<DistributorListModel> = ArrayList()
    private var distributorListAdapter: DistributorListAdapter? = null

    private var productsList: ArrayList<ProductListModel> = ArrayList()
    private var productsResult: ArrayList<ProductListModel> = ArrayList()
    private var productsListAdapter: ProductsListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = SearchBinding.inflate(inflater, container, false)
        return bind.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents()
        getDataFromServer()

    }

    private fun getDataFromServer() {
        CoroutineScope(IO).launch {
            getProductList()
            getVendorList()
        }
    }

    private fun getVendorList() {
        mDistributors.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                distributorList.clear()

                for (data in snapshot.children) {
                    distributorList.add(
                        DistributorListModel(
                            data.ref.key.toString(),
                            data.getValue(DistributorListDetails::class.java)!!
                        )
                    )
                }
                mLog("vendor :: " + Gson().toJson(distributorList))
            }

            override fun onCancelled(error: DatabaseError) {
                mToast(requireContext(), error.message)
            }
        })
    }

    private fun getProductList() {
        productDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productsList.clear()

                for (data in snapshot.children) {
                    productsList.add(
                        ProductListModel(
                            data.ref.key.toString(),
                            data.getValue(ProductdetailModel::class.java)!!
                        )
                    )
                }
                mLog("Products :: " + Gson().toJson(productsList))
            }

            override fun onCancelled(error: DatabaseError) {
                mToast(requireContext(), error.message)
            }
        })
    }

    private fun initAllComponents() {

        bind.recyclerProducts.visibility = VISIBLE

        bind.recyclerDistributor.layoutManager = GridLayoutManager(requireContext(), 2)
        bind.recyclerDistributor.hasFixedSize()
        distributorListAdapter = DistributorListAdapter(requireContext(), this)
        bind.recyclerDistributor.adapter = distributorListAdapter

        bind.recyclerProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        bind.recyclerProducts.hasFixedSize()
        productsListAdapter = ProductsListAdapter(requireContext(), this)
        bind.recyclerProducts.adapter = productsListAdapter

        bind.search.isEnabled = true
        bind.searchSwitch.setOnCheckedChangeListener(this)
        bind.name.addTextChangedListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        //initRecycler()

    }

    /*  private fun initRecycler() {
          val option: FirebaseRecyclerOptions<ProductModel> =
              FirebaseRecyclerOptions.Builder<ProductModel>()
                  .setQuery(productDatabase.orderByChild("product_name"), ProductModel::class.java)
                  .build()
          val recyclerAdapter =
              object : FirebaseRecyclerAdapter<ProductModel, ProductsViewHolder>(option) {
                  override fun onCreateViewHolder(
                      parent: ViewGroup,
                      viewType: Int,
                  ): ProductsViewHolder {
                      val binding: ProductLayoutBinding =
                          ProductLayoutBinding.inflate(
                              LayoutInflater.from(parent.context),
                              parent,
                              false
                          )
                      return ProductsViewHolder(requireContext(), binding)
                  }

                  override fun onBindViewHolder(
                      holder: ProductsViewHolder,
                      position: Int,
                      model: ProductModel,
                  ) {
                      holder.bind(model)
                      holder.card.setOnClickListener {
                          val action =
                              SearchDirections.searchToProductDetail(getRef(position).key.toString())
                          view?.findNavController()?.navigate(action)
                      }

                  }


              }

          bind.recycler.adapter = recyclerAdapter
          recyclerAdapter.startListening()
      }*/


    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.searchSwitch -> {
                if (isChecked) {
                    bind.searchSwitch.text = "Search by seller"
                    bind.recyclerDistributor.visibility = VISIBLE
                    bind.recyclerProducts.visibility = GONE
                } else {
                    bind.searchSwitch.text = "Search by product"
                    bind.recyclerProducts.visibility = VISIBLE
                    bind.recyclerDistributor.visibility = GONE

                }
            }
        }
    }


    //search
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (bind.searchSwitch.isChecked) {
            searchedDistributor = s.toString().toLowerCase().trim()
            distributorResult.clear()

            for (i in distributorList.indices) {
                if (distributorList[i].details.name != null) {
                    if (distributorList[i].details.name.toLowerCase()
                            .contains(searchedDistributor)
                    ) {
                        distributorResult.add(distributorList[i])
                    }
                }
            }
            distributorListAdapter?.setItems(distributorResult)
            mLog("search distributor :: " + Gson().toJson(distributorResult))
        } else {
            searchedproducts = s.toString().toLowerCase().trim { it <= ' ' }
            productsResult.clear()

            for (i in productsList.indices) {
                if (productsList[i].detail.product_name != null) {
                    if (productsList[i].detail.product_name.toLowerCase()
                            .contains(searchedproducts)
                    ) {
                        productsResult.add(productsList[i])
                    }
                }
            }
            productsListAdapter?.setItems(productsResult)
            mLog("search products :: " + Gson().toJson(productsResult))
        }

    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun onItemSelect(id: String) {
        val action = SearchDirections.searchToProductDetail(id)
        view?.findNavController()?.navigate(action)
    }

    override fun onVendorSelect(id: String) {
        val action = SearchDirections.searchToProductsBySeller(id)
        view?.findNavController()?.navigate(action)
    }

}