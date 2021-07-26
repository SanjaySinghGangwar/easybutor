package com.thedramaticcolumnist.app.ui.BottomSheets.addAddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.Database.mDatabase.mAddress
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.isValidText
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.AddAddressBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class AddAddress(private val id: String) : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: AddAddressBinding? = null
    private val bind get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = AddAddressBinding.inflate(inflater, container, false)
        return bind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (id.length > 1) {
            getDataFormServer()
        }
        initAllComponents()
    }

    private fun getDataFormServer() {
        mAddress!!.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("city")) {
                    bind.city.setText(snapshot.child("city").value.toString())
                }
                if (snapshot.hasChild("full_name")) {
                    bind.name.setText(snapshot.child("full_name").value.toString())
                }
                if (snapshot.hasChild("house_address")) {
                    bind.houseAdd.setText(snapshot.child("house_address").value.toString())
                }
                if (snapshot.hasChild("phone")) {
                    bind.phone.setText(snapshot.child("phone").value.toString())
                }
                if (snapshot.hasChild("pinCode")) {
                    bind.pinCode.setText(snapshot.child("pinCode").value.toString())
                }
                if (snapshot.hasChild("road_address")) {
                    bind.roadAddress.setText(snapshot.child("road_address").value.toString())
                }
                if (snapshot.hasChild("state")) {
                    bind.state.setText(snapshot.child("state").value.toString())
                }

            }

            override fun onCancelled(error: DatabaseError) {
                mToast(requireContext(), error.message)
            }
        })
    }

    private fun initAllComponents() {
        bind.add.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add -> {
                if (isValidText(bind.name.text.toString().trim(), bind.name) &&
                    isValidText(bind.phone.text.toString().trim(), bind.phone) &&
                    isValidText(bind.houseAdd.text.toString().trim(), bind.houseAdd) &&
                    isValidText(bind.roadAddress.text.toString().trim(), bind.roadAddress) &&
                    isValidText(bind.city.text.toString().trim(), bind.city) &&
                    isValidText(bind.state.text.toString().trim(), bind.state) &&
                    isValidText(bind.pinCode.text.toString().trim(), bind.pinCode)
                ) {
                    var message = "added"
                    var timestamp =
                        SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) +
                                Random().nextInt(1000000)

                    var hashMap: HashMap<String, String> = HashMap()
                    hashMap["full_name"] = bind.name.text.toString().trim()
                    hashMap["phone"] = bind.phone.text.toString().trim()
                    hashMap["house_address"] = bind.houseAdd.text.toString().trim()
                    hashMap["road_address"] = bind.roadAddress.text.toString().trim()
                    hashMap["city"] = bind.city.text.toString().trim()
                    hashMap["state"] = bind.state.text.toString().trim()
                    hashMap["pinCode"] = bind.pinCode.text.toString().trim()

                    if (id.length > 1) {
                        timestamp = id
                        message = "Updated"

                    }
                    mAddress?.child(timestamp)?.setValue(hashMap)
                        ?.addOnSuccessListener {
                            mToast(requireContext(), message)
                            dismiss()
                        }
                }
            }
        }
    }
}