package com.example.whatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onStart() {
        super.onStart()

        currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null){
            sendUserToMainActivity()
        }
    }

    private fun sendUserToMainActivity(){
        val loginIntent = Intent(this, MainActivity::class.java)
        startActivity(loginIntent)
        //finish() // Finaliza LoginActivity para que no se pueda volver atrás después de iniciar sesión

    }
}