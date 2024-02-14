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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private var currentUser: FirebaseUser? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var loadingBar: ProgressDialog
    private lateinit var LoginButton : Button
    private lateinit var PhoneLoginButton: Button
    private lateinit var UserEmail : EditText
    private lateinit var UserPassword : EditText
    private lateinit var NeedNewAccoutnLink : TextView
    private lateinit var ForgetPasswordLink : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        initializeFields();

        NeedNewAccoutnLink.setOnClickListener {
            sendUserToRegisterActivity()
        }

        LoginButton.setOnClickListener {
            AllowUserToLogin()
        }
    }

    private fun AllowUserToLogin() {
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

            loadingBar.setTitle("Sign in")
            loadingBar.setMessage("Please wait...")
            loadingBar.setCanceledOnTouchOutside(true)
            loadingBar.show()

            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendUserToMainActivity()
                    Toast.makeText(this, "Logged in Successul..", Toast.LENGTH_SHORT).show()
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
        LoginButton = findViewById(R.id.login_button)
        PhoneLoginButton = findViewById(R.id.phone_login_button)
        UserEmail = findViewById(R.id.login_email)
        UserPassword = findViewById(R.id.login_password)
        NeedNewAccoutnLink =findViewById(R.id.need_new_account_link)
        ForgetPasswordLink =findViewById(R.id.forget_password_link)
        loadingBar = ProgressDialog(this)
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

    private fun sendUserToRegisterActivity(){
        val registerIntent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
        //finish() // Finaliza LoginActivity para que no se pueda volver atrás después de iniciar sesión
    }

}