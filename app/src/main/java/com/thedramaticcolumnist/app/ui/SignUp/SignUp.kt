package com.thedramaticcolumnist.app.ui.SignUp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.isValidText
import com.thedramaticcolumnist.app.databinding.SignUpBinding
import com.thedramaticcolumnist.app.ui.HomeScreen

class SignUp : AppCompatActivity(), View.OnClickListener {

    private lateinit var bind: SignUpBinding
    private lateinit var auth: FirebaseAuth

    private val TAG: String = "SIGN UP"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = SignUpBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initAllComponents()
    }

    private fun initAllComponents() {
        auth = Firebase.auth
        bind.signUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signUp -> {
                if (isValidText(bind.name.text.toString().trim(),
                        bind.email) && isValidText(bind.email.text.toString().trim(),
                        bind.email) && isValidText(bind.password.text.toString().trim(),
                        bind.password)
                ) {
                    bind.progressBar.visibility = VISIBLE
                    auth.createUserWithEmailAndPassword(bind.email.text.toString().trim(),
                        bind.password.text.toString().trim())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                FirebaseDatabase.getInstance().reference.
                                child(applicationContext.getString(R.string.app_name))
                                    .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                                    .child("name")
                                    .setValue(bind.name.text.toString().trim())
                                    .addOnSuccessListener {
                                        bind.progressBar.visibility = GONE
                                        intent = Intent(this@SignUp, HomeScreen::class.java)
                                        startActivity(intent)
                                        finish()
                                    }

                            } else {
                                bind.progressBar.visibility = GONE
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext,
                                    "Authentication failed." + task.exception?.message,
                                    Toast.LENGTH_SHORT).show()

                            }
                        }
                }
            }
        }
    }
}