package com.thedramaticcolumnist.app.ui.Distributor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.Database.mDatabase.mDistributors
import com.thedramaticcolumnist.app.Model.DistributorListDetails
import com.thedramaticcolumnist.app.Model.DistributorListModel
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.DistributorsBinding

class Distributors : Fragment(), DistributorListAdapter.onClickListner {

    private var _binding: DistributorsBinding? = null
    private val bind get() = _binding!!
    private var distributorList: ArrayList<DistributorListModel> = ArrayList()
    private var distributorListAdapter: DistributorListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DistributorsBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents()
        getDataFromServer()
    }

    private fun getDataFromServer() {
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
                if (distributorList.size > 0) {
                    distributorListAdapter!!.setItems(distributorList)
                    bind.temp.visibility = View.INVISIBLE
                    bind.recycler.visibility = View.VISIBLE
                } else {
                    bind.temp.visibility = View.VISIBLE
                    bind.recycler.visibility = View.INVISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                mToast(requireContext(), error.message)
            }
        })
    }

    private fun initAllComponents() {
        bind.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        bind.recycler.hasFixedSize()
        distributorListAdapter = DistributorListAdapter(requireContext(), this)
        bind.recycler.adapter = distributorListAdapter
    }

    override fun onVendorSelect(id: String) {
        val action = DistributorsDirections.distributorToProductsBySeller(id)
        view?.findNavController()?.navigate(action)
    }

}