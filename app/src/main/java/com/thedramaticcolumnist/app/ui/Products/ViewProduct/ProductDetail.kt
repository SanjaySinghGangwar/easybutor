package com.thedramaticcolumnist.app.ui.Products.ViewProduct

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.*
import com.smarteist.autoimageslider.SliderView
import com.thedramaticcolumnist.app.Database.mDatabase.mWishList
import com.thedramaticcolumnist.app.Database.mDatabase.myCart
import com.thedramaticcolumnist.app.Model.SliderData
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mLog
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.ProductDetailBinding
import com.thedramaticcolumnist.app.adapter.SliderAdapter
import com.thedramaticcolumnist.app.adapter.SliderAdapterProducts
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class ProductDetail : Fragment(), View.OnClickListener {

    private lateinit var splitString: ArrayList<String>
    var images: String = ""
    val args: ProductDetailArgs by navArgs()


    lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    private var _binding: ProductDetailBinding? = null
    private val bind get() = _binding!!


    var arrayList = ArrayList<String>()
    var orderDetails: HashMap<String, String> = HashMap<String, String>()

    var quantity: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ProductDetailBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        fetchProductDetail()
        setUpToolbar()

    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).supportActionBar?.title = ""
    }

    private fun setUpSlider() {
        val sliderDataArrayList: ArrayList<SliderData> = ArrayList()
        val sliderView: SliderView = bind.slider
        for (i in splitString.indices) {
            sliderDataArrayList.add(SliderData(splitString[i]))
        }

        val adapter = SliderAdapterProducts(
            requireContext(),
            sliderDataArrayList)

        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderView.setSliderAdapter(adapter)
        sliderView.scrollTimeInSec = 3
        sliderView.isAutoCycle = true
        sliderView.startAutoCycle()
    }

    private fun initAllComponent() {
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("Products")
            .child(args.productID)

        bind.addToCart.setOnClickListener(this)
        bind.wishlist.setOnClickListener(this)
        bind.buyNow.setOnClickListener(this)

        bind.mrp.paintFlags = bind.mrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }


    private fun fetchProductDetail() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild("category")) {

                }
                if (dataSnapshot.hasChild("image")) {
                    images = dataSnapshot.child("image").value.toString()
                    stringToArray(images.substring(1, images.length - 1));
                    setUpSlider()
                }

                if (dataSnapshot.hasChild("long_description")) {
                    bind.longDescription.text =
                        dataSnapshot.child("long_description").value.toString()
                }
                if (dataSnapshot.hasChild("price")) {
                    bind.price.text = "Selling ₹ " + dataSnapshot.child("price").value.toString()
                }
                if (dataSnapshot.hasChild("product_name")) {
                    bind.name.text = dataSnapshot.child("product_name").value.toString()
                    (activity as AppCompatActivity).supportActionBar?.title =
                        dataSnapshot.child("product_name").value.toString()

                }
                if (dataSnapshot.hasChild("seller")) {
                }
                if (dataSnapshot.hasChild("short_description")) {
                    bind.shortDescription.text =
                        dataSnapshot.child("short_description").value.toString()
                }

                if (dataSnapshot.hasChild("quantity")) {
                    quantity =
                        dataSnapshot.child("quantity").value.toString()
                }

                if (dataSnapshot.hasChild("mrp")) {
                    bind.mrp.text =
                        "MPR ₹ " + dataSnapshot.child("mrp").value.toString()
                }

                if (dataSnapshot.hasChild("mrp") && dataSnapshot.hasChild("price")) {
                    val price = dataSnapshot.child("price").value.toString()
                    val mrp = dataSnapshot.child("mrp").value.toString()
                    bind.discount.text =
                        100.minus((price.toFloat() / mrp.toFloat()) * 100).roundToInt()
                            .toString() + " %"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                mToast(
                    context?.applicationContext!!,
                    error.toException().message.toString()
                )
            }
        })
    }

    private fun stringToArray(images: String) {
        try {
            splitString = images.split(",") as ArrayList<String>
        } catch (e: Exception) {
            mLog(e.message.toString())
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buyNow -> {
                addToCard(true)
            }
            R.id.addToCart -> {
                addToCard(false)
            }
            R.id.wishlist -> {
                if (quantity.toInt() > 0) {
                    mWishList?.orderByChild("id")?.equalTo(args.productID)
                        ?.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    mToast(requireContext(), "Already added to wishlist")
                                } else {
                                    val timestamp =
                                        SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) + Random().nextInt(
                                            1000000)
                                    orderDetails["id"] = args.productID
                                    orderDetails["quantity"] = "1"

                                    mWishList?.child(timestamp)?.setValue(orderDetails)
                                        ?.addOnSuccessListener {
                                            mToast(requireContext(), "Added to wishlist")
                                        }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })
                } else {
                    mToast(requireContext(), "Item out of stock")
                }

            }
        }
    }

    private fun addToCard(flags: Boolean) {
        if (quantity.toInt() > 0) {
            myCart?.orderByChild("id")?.equalTo(args.productID)
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            mToast(requireContext(), "Already added to cart")
                            if(flags){
                                view?.findNavController()?.navigate(R.id.productDetail_to_cart)
                            }
                        } else {
                            val timestamp =
                                SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) + Random().nextInt(
                                    1000000)
                            orderDetails["id"] = args.productID
                            orderDetails["quantity"] = "1"

                            myCart?.child(timestamp)?.setValue(orderDetails)
                                ?.addOnSuccessListener {
                                    mToast(requireContext(), "Added to cart")
                                    if(flags){
                                        view?.findNavController()?.navigate(R.id.productDetail_to_cart)
                                    }
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        } else {
            mToast(requireContext(), "Item out of stock")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}