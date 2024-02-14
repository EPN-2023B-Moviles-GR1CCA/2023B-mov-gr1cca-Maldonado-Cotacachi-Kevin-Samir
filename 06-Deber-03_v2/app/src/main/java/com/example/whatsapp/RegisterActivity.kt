package com.example.whatsapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.newSingleThreadContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var CreateAccountButton : Button
    private lateinit var UserEmail : EditText
    private lateinit var UserPassword : EditText
    private lateinit var AlreadyHaveAccountLink : TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var loadingBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth =FirebaseAuth.getInstance()

        initializeFields();

        AlreadyHaveAccountLink.setOnClickListener{
            sendUserToLoginActivity()
        }

        CreateAccountButton.setOnClickListener {
            createNewAccount()
        }

    }

    private fun createNewAccount() {
        val email = UserEmail.text.toString()
        val password = UserPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email..", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password..", Toast.LENGTH_SHORT).show()
            return
        } else{

            loadingBar.setTitle("Creating New Account")
            loadingBar.setMessage("Please wait, while we are creating account for you..")
            loadingBar.setCanceledOnTouchOutside(true)
            loadingBar.show()

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendUserToLoginActivity()
                        Toast.makeText(this, "Account Created Successfully..", Toast.LENGTH_SHORT).show()
                        loadingBar.dismiss()
                    } else {
                        val message = task.exception?.message ?: "Unknown error"
                        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                        loadingBar.dismiss()

                    }
                }
        }


    }


    private fun initializeFields(){
        CreateAccountButton = findViewById(R.id.register_button)
        UserEmail = findViewById(R.id.register_email)
        UserPassword = findViewById(R.id.register_password)
        AlreadyHaveAccountLink =findViewById(R.id.already_have_account_link)
        loadingBar =  ProgressDialog(this)
    }
    private fun sendUserToLoginActivity(){
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        //finish() // Finaliza LoginActivity para que no se pueda volver atrás después de iniciar sesión
    }
}