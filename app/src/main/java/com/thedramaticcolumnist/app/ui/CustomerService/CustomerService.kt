package com.thedramaticcolumnist.app.ui.CustomerService

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.databinding.CustomerFragmentBinding
import com.thedramaticcolumnist.app.databinding.FragmentHomeBinding
import com.thedramaticcolumnist.app.ui.home.HomeViewModel

class CustomerService : Fragment() {

    private lateinit var customerViewModel: CustomerViewModel
    private var _binding: CustomerFragmentBinding? = null
    private val binding get() = _binding!!


    companion object {
        fun newInstance() = CustomerService()
    }

    private lateinit var viewModel: CustomerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        customerViewModel =
            ViewModelProvider(this).get(CustomerViewModel::class.java)

        _binding = CustomerFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textView
        customerViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}