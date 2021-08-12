package com.thedramaticcolumnist.app.ui.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.thedramaticcolumnist.app.Database.mDatabase.mDatabase
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.isValidText
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.ActivityLoginBinding
import com.thedramaticcolumnist.app.ui.HomeScreen
import com.thedramaticcolumnist.app.ui.SignUp.SignUp

class Login : AppCompatActivity(), OnClickListener {

    private lateinit var bind: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var token: String? = null

    private val TAG: String = "LOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initAllComponents()
        getToken()
    }

    private fun initAllComponents() {
        auth = Firebase.auth
        bind.signUp.setOnClickListener(this)
        bind.login.setOnClickListener(this)
        bind.forgetPassword.setOnClickListener(this)
    }
    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            token = task.result
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signUp -> {
                intent = Intent(this@Login, SignUp::class.java)
                startActivity(intent)
            }
            R.id.login -> {
                if (isValidText(bind.email.text.toString().trim(),
                        bind.email) && isValidText(bind.password.text.toString().trim(),
                        bind.password)
                ) {
                    bind.progressBar.visibility = VISIBLE
                    auth.signInWithEmailAndPassword(bind.email.text.toString().trim(),
                        bind.password.text.toString().trim())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                mDatabase.
                                child(applicationContext.getString(R.string.app_name))
                                    .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                                    .child("token")
                                    .setValue(token)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "signInWithEmail:success")
                                        bind.progressBar.visibility = GONE
                                        intent = Intent(this@Login, HomeScreen::class.java)
                                        startActivity(intent)
                                        finish()
                                    }

                            } else {
                                bind.progressBar.visibility = GONE
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, task.exception?.message,
                                    Toast.LENGTH_SHORT).show()
                            }
                        }

                }
            }
            R.id.forgetPassword -> {
                if (isValidText(bind.email.text.toString().trim(), bind.email)
                ) {
                    bind.progressBar.visibility = VISIBLE
                    auth.sendPasswordResetEmail(bind.email.text.toString().trim())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                bind.progressBar.visibility = GONE
                                mToast(this, "Reset link send successfully")
                            } else {
                                bind.progressBar.visibility = GONE
                                Toast.makeText(baseContext, task.exception?.message,
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }
}
