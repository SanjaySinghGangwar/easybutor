package com.thedramaticcolumnist.app.ui.Distributor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.thedramaticcolumnist.app.databinding.MyAddressBinding

class MyAddress : Fragment() {

    private lateinit var disbutorViewModel: MyAddressViewModel
    private var _binding: MyAddressBinding? = null
    private val bind get() = _binding!!


    companion object {
        fun newInstance() = MyAddress()
    }

    private lateinit var viewModel: MyAddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        disbutorViewModel =
            ViewModelProvider(this).get(MyAddressViewModel::class.java)

        _binding = MyAddressBinding.inflate(inflater, container, false)
        val root: View = bind.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()

    }

    private fun initAllComponent() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}