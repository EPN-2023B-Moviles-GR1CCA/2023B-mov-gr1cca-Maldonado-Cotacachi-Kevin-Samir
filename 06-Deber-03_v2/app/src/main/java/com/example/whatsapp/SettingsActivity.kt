package com.example.whatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class SettingsActivity : AppCompatActivity() {
    private lateinit var UpdateAccountSettings: Button
    private lateinit var userName : EditText
    private lateinit var userStatus : EditText
    private lateinit var userProfileImage: CircleImageView
    private lateinit var currentUserID : String
    private lateinit var mAuth : FirebaseAuth
    private lateinit var RootRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        mAuth = FirebaseAuth.getInstance()
        currentUserID = mAuth.currentUser?.uid ?: ""
        RootRef = FirebaseDatabase.getInstance().getReference()
        initializeFields();

        //userName.setVisibility(View.INVISIBLE)
        userName.visibility = View.INVISIBLE


        UpdateAccountSettings.setOnClickListener {
            UpdateSettings()
        }

        RetrieveUserInfo()

    }

    private fun RetrieveUserInfo() {
        val context = this
        RootRef.child("Users").child(currentUserID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("image")) {
                        // El usuario existe en la base de datos
                        val retrieverUserName = dataSnapshot.child("name").getValue().toString()
                        val retrieverStatus = dataSnapshot.child("status").getValue().toString()
                        val retrieverProfileImage = dataSnapshot.child("image").getValue().toString()

                        userName.setText(retrieverUserName)
                        userStatus.setText(retrieverStatus)

                    } else if (dataSnapshot.exists() && dataSnapshot.hasChild("name")) {
                        // Usuario existe pero no tiene imagen
                        val retrieverUserName = dataSnapshot.child("name").getValue().toString()
                        val retrieverStatus = dataSnapshot.child("status").getValue().toString()

                        userName.setText(retrieverUserName)
                        userStatus.setText(retrieverStatus)

                    } else {
                        //userName.setVisibility(View.VISIBLE) //More common in Java
                        userName.visibility = View.VISIBLE // Mode Kotlin

                        Toast.makeText(context, "Please set and update your information", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Ocurrió un error al leer la base de datos
                }
            })

    }

    private fun UpdateSettings() {
        val setUserName = userName.text.toString()
        val setStatus = userStatus.text.toString()
        if(TextUtils.isEmpty(setUserName)){
            Toast.makeText(this,"Please write your user name first..",Toast.LENGTH_SHORT).show()

        }
        if(TextUtils.isEmpty(setStatus)){
            Toast.makeText(this,"Please write your status..",Toast.LENGTH_SHORT).show()

        }else{
            val profileMap = HashMap<String, String>()
                profileMap["uid"] = currentUserID
                profileMap["name"] = setUserName
                profileMap["status"] = setStatus
            RootRef.child("Users").child(currentUserID).setValue(profileMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendUserToMainActivity()
                        Toast.makeText(this, "Profile Updated Successfully..", Toast.LENGTH_SHORT).show()
                    }else{
                        val message = task.exception?.message ?: "Unknown error"
                        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                    }

                }

        }

    }

    private fun initializeFields() {
        UpdateAccountSettings = findViewById(R.id.update_settings_button)
        userName = findViewById(R.id.set_user_name)
        userStatus = findViewById(R.id.set_profile_status)
        userProfileImage = findViewById(R.id.set_profile_image)

    }
    private fun sendUserToMainActivity(){
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(mainIntent)
        finish() // Finaliza LoginActivity para que no se pueda volver atrás después de iniciar sesión
    }
}