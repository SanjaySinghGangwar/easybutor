package com.thedramaticcolumnist.app.ui.BottomSheets.LogOut

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.databinding.LogoutBinding
import com.thedramaticcolumnist.app.ui.SplashScreen


class Logout : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: LogoutBinding? = null
    private val bind get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = LogoutBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
    }

    private fun initAllComponent() {
        bind.cancel.setOnClickListener(this)
        bind.ok.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ok -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireContext(), SplashScreen::class.java)
                startActivity(intent)
            }
            R.id.cancel -> {
                dismiss()
            }

        }
    }
}