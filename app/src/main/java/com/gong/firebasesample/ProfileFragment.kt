package com.gong.firebasesample

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val firebaseUser: FirebaseUser by lazy {
        FirebaseAuth.getInstance().currentUser ?: error("")
    }

    private val storageRef =
        FirebaseStorage.getInstance().reference.child("${firebaseUser.email}.jpg")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initProfile()
    }

    private fun initView() {
        img_profile.setOnClickListener {
            TedImagePicker.with(context!!)
                .start { uri -> uploadProfileImage(uri) }
        }

        tv_profile_name.text = firebaseUser.displayName
    }

    private fun initProfile() {
        showLoading()
        storageRef.downloadUrl
            .setImage()
    }

    private fun showProfileImage(uri: Uri) {
        Glide.with(img_profile)
            .load(uri)
            .circleCrop()
            .into(img_profile)
    }

    private fun uploadProfileImage(uri: Uri) {
        showLoading()
        storageRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageRef.downloadUrl
            }
            .setImage()

    }

    private fun showLoading() {
        progress.isVisible = true
    }

    private fun hideLoading() {
        progress.isVisible = false
    }

    private fun Task<Uri>.setImage() {
        addOnCompleteListener {
            hideLoading()
        }
        addOnSuccessListener {
            showProfileImage(it)
        }
        addOnFailureListener {
            firebaseUser.photoUrl?.let{
                showProfileImage(it)
            }
        }
    }
}