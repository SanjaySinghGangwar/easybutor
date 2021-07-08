package com.thedramaticcolumnist.app.ui.WishList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.thedramaticcolumnist.app.databinding.WishlistFragmentBinding

class WishlistFragment : Fragment() {

    private lateinit var wishlistViewModel: WishlistViewModel
    private var _binding: WishlistFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        wishlistViewModel =
            ViewModelProvider(this).get(WishlistViewModel::class.java)

        _binding = WishlistFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        wishlistViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}