package com.thedramaticcolumnist.app.ui.YourAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.YourAccountFragmentBinding
import com.thedramaticcolumnist.app.ui.BottomSheets.EditProfile.EditProfile
import com.thedramaticcolumnist.app.ui.BottomSheets.LogOut.Logout

class YourAccount : Fragment(), View.OnClickListener {

    private var imageUrl: String = ""
    private lateinit var yourAccountViewModel: YourAccountViewModel
    private var _binding: YourAccountFragmentBinding? = null

    private val bind get() = _binding!!

    companion object {
        fun newInstance() = YourAccount()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        yourAccountViewModel =
            ViewModelProvider(this).get(YourAccountViewModel::class.java)

        _binding = YourAccountFragmentBinding.inflate(inflater, container, false)
        val root: View = bind.root

        val textView: TextView = bind.textView
        yourAccountViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        fetchProfileData()
    }

    private fun fetchProfileData() {
        FirebaseDatabase.getInstance().reference
            .child(getString(R.string.app_name))
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    bind.edit.visibility = VISIBLE
                    if (snapshot.hasChild("name")) {
                        bind.name.text =
                            snapshot.child("name").value.toString()
                    }
                    if (snapshot.hasChild("phone")) {
                        bind.phone.text =
                            snapshot.child("phone").value.toString()
                    }
                    if (snapshot.hasChild("profile_image")) {
                        imageUrl = snapshot.child("profile_image").value.toString()
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_person)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(bind.image);
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    bind.edit.visibility = VISIBLE
                    mToast(requireContext(), error.message)
                }
            })
    }

    private fun initAllComponent() {
        bind.edit.setOnClickListener(this)
        bind.logout.setOnClickListener(this)
        bind.address.setOnClickListener(this)
        bind.wishlist.setOnClickListener(this)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edit -> {
                val edit =
                    EditProfile(bind.name.text.toString(),
                        bind.phone.text.toString(),
                        imageUrl)

                edit.show(parentFragmentManager, "")
            }
            R.id.logout -> {
                val logout = Logout()
                logout.show(parentFragmentManager, "")
            }
            R.id.address -> {
                view?.findNavController()?.navigate(R.id.account_to_address)
            }
            R.id.wishlist -> {
                view?.findNavController()?.navigate(R.id.account_to_wishlist)
            }
        }
    }
}