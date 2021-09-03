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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.smarteist.autoimageslider.SliderView
import com.thedramaticcolumnist.app.Database.mDatabase.addToCard
import com.thedramaticcolumnist.app.Database.mDatabase.addToWishList
import com.thedramaticcolumnist.app.Database.mDatabase.productDatabase
import com.thedramaticcolumnist.app.Model.ProductDetailsModel
import com.thedramaticcolumnist.app.Model.SliderData
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.calculateDiscount
import com.thedramaticcolumnist.app.Utils.mUtils.mLog
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.adapter.SliderAdapterProducts
import com.thedramaticcolumnist.app.databinding.ProductDetailBinding
import java.util.*
import kotlin.collections.ArrayList

class ProductDetail : Fragment(), View.OnClickListener {

    private var _binding: ProductDetailBinding? = null
    private val bind get() = _binding!!


    private lateinit var splitString: ArrayList<String>


    val args: ProductDetailArgs by navArgs()
    var orderDetails: HashMap<String, String> = HashMap<String, String>()
    var quantity: String = "0"
    var images: String = ""


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
            sliderDataArrayList
        )

        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderView.setSliderAdapter(adapter)
        sliderView.scrollTimeInSec = 3
        sliderView.isAutoCycle = true
        sliderView.startAutoCycle()
    }

    private fun initAllComponent() {
        bind.mrp.paintFlags = bind.mrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        bind.addToCart.setOnClickListener(this)
        bind.wishlist.setOnClickListener(this)
        bind.buyNow.setOnClickListener(this)
    }


    private fun fetchProductDetail() {
        productDatabase.child(args.productID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.getValue(ProductDetailsModel::class.java)!!

                    bind.longDescription.text = data.long_description
                    bind.price.text = "Selling ₹ ${data.price}"
                    bind.name.text = data.product_name
                    bind.shortDescription.text = data.short_description
                    bind.mrp.text = "MPR ₹ ${data.mrp}"
                    bind.discount.text = calculateDiscount(data.price, data.mrp)

                    images = dataSnapshot.child("image").value.toString()
                    stringToArray(images.substring(1, images.length - 1));
                    setUpSlider()

                    quantity = data.quantity
                    (activity as AppCompatActivity).supportActionBar?.title = data.product_name
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
                if (quantity != "0") {
                    addToCard(requireContext(), args.productID)
                    view?.findNavController()?.navigate(R.id.productDetail_to_cart)
                }
            }
            R.id.addToCart -> {
                if (quantity != "0") {
                    addToCard(requireContext(), args.productID)
                }

            }
            R.id.wishlist -> {
                if (quantity.toInt() > 0) {
                    addToWishList(requireContext(), args.productID)
                } else {
                    mToast(requireContext(), "Item out of stock")
                }

            }
        }
    }

    /* private fun addToWishList() {
         mWishList?.orderByChild("id")?.equalTo(args.productID)
             ?.addValueEventListener(object : ValueEventListener {
                 override fun onDataChange(snapshot: DataSnapshot) {
                     if (!snapshot.exists()) {
                         val timestamp =
                             SimpleDateFormat("yyyyMMddHHmmssmsms")
                                 .format(Date()) + Random().nextInt(1000000)
                         orderDetails["id"] = args.productID
                         orderDetails["quantity"] = "1"

                         mWishList?.child(timestamp)?.setValue(orderDetails)
                             ?.addOnSuccessListener {
                                 mToast(requireContext(), "Added to wishlist")
                             }?.addOnFailureListener {
                                 mToast(requireContext(), it.message.toString())
                             }
                     }
                 }

                 override fun onCancelled(error: DatabaseError) {
                     mToast(requireContext(), error.message)
                 }

             })
     }

     private fun addToCard() {
         myCart?.orderByChild("id")?.equalTo(args.productID)
             ?.addValueEventListener(object : ValueEventListener {
                 override fun onDataChange(snapshot: DataSnapshot) {
                     if (!snapshot.exists()) {
                         val timestamp = SimpleDateFormat("yyyyMMddHHmmssmsms")
                             .format(Date()) + Random().nextInt(1000000)
                         orderDetails["id"] = args.productID
                         orderDetails["quantity"] = "1"

                         myCart?.child(timestamp)?.setValue(orderDetails)
                             ?.addOnSuccessListener {
                                 mToast(requireContext(), "Added to cart")
                             }
                     }
                 }

                 override fun onCancelled(error: DatabaseError) {
                     mToast(requireContext(), error.message)
                 }

             })

     }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}