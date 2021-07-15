package com.thedramaticcolumnist.app.ui.Products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.databinding.ProductLayoutBinding
import com.thedramaticcolumnist.app.databinding.ProductsFragmentBinding

class ProductsFragment : Fragment() {

    private lateinit var productsAccountViewModel: ProductsViewModel
    private var _binding: ProductsFragmentBinding? = null
    private val bind get() = _binding!!
    private lateinit var myRef: DatabaseReference
    lateinit var database: FirebaseDatabase


    companion object {
        fun newInstance() = ProductsFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        productsAccountViewModel =
            ViewModelProvider(this).get(ProductsViewModel::class.java)

        _binding = ProductsFragmentBinding.inflate(inflater, container, false)
        val root: View = bind.root

        val textView: TextView = bind.textView
        productsAccountViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAllComponents()
    }

    private fun initAllComponents() {
        database = FirebaseDatabase.getInstance()
        myRef = database.reference.child("Products")
        bind.recycler.layoutManager = GridLayoutManager(requireContext(), 2)


    }

    fun loadUrl(url: String) {
        val settings: WebSettings = bind.webView.getSettings()
        settings.domStorageEnabled = true

        bind.webView.requestFocus();
        bind.webView.settings.lightTouchEnabled = true;
        bind.webView.settings.javaScriptEnabled = true;
        bind.webView.settings.setGeolocationEnabled(true);
        bind.webView.isSoundEffectsEnabled = true;
        bind.webView.settings.setAppCacheEnabled(true);
        bind.webView.loadUrl(url);
        bind.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                bind.progressBar.visibility = View.VISIBLE
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                bind.progressBar.visibility = View.GONE
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        initRecycler()

    }

    private fun initRecycler() {
        val option: FirebaseRecyclerOptions<ProductModel> =
            FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(myRef.orderByChild("product_name"), ProductModel::class.java)
                .build()
        val recyclerAdapter =
            object : FirebaseRecyclerAdapter<ProductModel, ProductsViewHolder>(option) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): ProductsViewHolder {
                    val binding: ProductLayoutBinding =
                        ProductLayoutBinding.inflate(LayoutInflater.from(parent.context),
                            parent,
                            false)
                    return ProductsViewHolder(requireContext(), binding)
                }

                override fun onBindViewHolder(
                    holder: ProductsViewHolder,
                    position: Int,
                    model: ProductModel,
                ) {
                    bind.progressBar.visibility = GONE
                    holder.bind(model)
                    holder.card.setOnClickListener {
                        //mToast(requireContext(), getRef(position).key.toString())
                        val action = ProductsFragmentDirections.productsToProductDetail(getRef(position).key.toString())
                        view?.findNavController()?.navigate(action)
                    }

                }


            }

        bind.recycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()
    }

}