package com.gong.firebasesample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            startFirebaseSignIn()
        } else {
            startMainActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQ_CODE_FIREBASE_SIGN_IN -> {
                when(resultCode) {
                    Activity.RESULT_OK -> {
                        startMainActivity()
                    }
                    Activity.RESULT_CANCELED -> {
                        finish()
                    }
                }
            }
        }
    }

    private fun startMainActivity() {
        MainActivity.startActivity(this)
        finish()
    }

    private fun startFirebaseSignIn() {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build() ,
            AuthUI.IdpConfig.PhoneBuilder().build() ,
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.AppTheme)
                .setLogo(R.drawable.ic_launcher_foreground)
                .setAvailableProviders(providers)
                .build() ,
            REQ_CODE_FIREBASE_SIGN_IN
        )
    }

    companion object {
        const val REQ_CODE_FIREBASE_SIGN_IN = 100
    }

}