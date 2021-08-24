package com.thedramaticcolumnist.app.ui.BottomSheets.EditProfile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.thedramaticcolumnist.app.Database.mDatabase.mDatabase
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.PermissionUtil
import com.thedramaticcolumnist.app.Utils.mUtils.isValidText
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.EditProfileBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class EditProfile(private val name: String, private val phone: String, private val image: String?) :
    BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: EditProfileBinding? = null
    private val bind get() = _binding!!

    var hashMap: HashMap<String, String> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = EditProfileBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
    }

    private fun initAllComponent() {
        bind.name.setText(name)
        if (phone != "Phone") {
            bind.phone.setText(phone)
        }

        Glide.with(requireContext())
            .load(image!!)
            .placeholder(R.drawable.ic_person)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.ic_error)
            .into(bind.image)
        bind.update.setOnClickListener(this)
        bind.image.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.update -> {
                if (isValidText(bind.name.text.toString().trim(), bind.name)) {
                    bind.progressBar.visibility = VISIBLE
                    bind.update.visibility = GONE
                    hashMap["name"] = bind.name.text.toString().trim()
                    hashMap["phone"] = bind.phone.text.toString().trim()
                    if (hashMap["profile_image"].isNullOrEmpty()) {
                        hashMap["profile_image"] = image.toString()
                    }
                    mDatabase.child(getString(R.string.app_name))
                        .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                        .setValue(hashMap).addOnSuccessListener {
                            bind.progressBar.visibility = GONE
                            bind.update.visibility = VISIBLE
                            mToast(requireContext(), "Updated")
                            dismiss()
                        }.addOnFailureListener {
                            bind.progressBar.visibility = GONE
                            bind.update.visibility = VISIBLE
                            mToast(requireContext(), "Failed to update")
                        }
                }
            }
            R.id.image -> {
                if (PermissionUtil.verifyPermissions(
                        requireContext(),
                        PermissionUtil.getGalleryPermissions()
                    )
                ) {
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, 100)

                } else PermissionUtil.requestPermission(
                    PermissionUtil.getGalleryPermissions(),
                    requireActivity()
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    Glide.with(requireContext())
                        .load(data!!.data)
                        .placeholder(R.drawable.ic_person)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_error)
                        .into(bind.image)
                    saveProductImage(data.data)
                }

            }
        }
    }

    fun saveProductImage(image: Uri?) {
        bind.progressBar.visibility = VISIBLE
        bind.update.visibility = GONE
        val TAG = "FIREBASE STORAGE"
        var url = ""
        val random = Random()
        val timestamp =
            SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) + random.nextInt(1000000)

        val profileStorageRef =
            FirebaseStorage.getInstance().reference.child("ProfilePics/$timestamp")

        profileStorageRef.putFile(image!!).addOnCompleteListener { uploadTask ->
            if (uploadTask.isSuccessful) {
                profileStorageRef.downloadUrl.addOnSuccessListener {
                    hashMap["profile_image"] = it.toString()
                    bind.progressBar.visibility = GONE
                    bind.update.visibility = VISIBLE
                }
            } else {
                bind.progressBar.visibility = GONE
                bind.update.visibility = VISIBLE
                mToast(requireContext(), uploadTask.exception?.message.toString())
                Log.i(TAG, "saveImage: ERROR" + uploadTask.exception?.message)
            }
        }

    }
}