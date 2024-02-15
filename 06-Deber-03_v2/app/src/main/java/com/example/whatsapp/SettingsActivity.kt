package com.example.whatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import de.hdodenhof.circleimageview.CircleImageView

class SettingsActivity : AppCompatActivity() {
    private lateinit var UpdateAccountSettings: Button
    private lateinit var userName : EditText
    private lateinit var userStatus : EditText
    private lateinit var userProfileImage: CircleImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initializeFields();


    }

    private fun initializeFields() {
        UpdateAccountSettings = findViewById(R.id.update_settings_button)
        userName = findViewById(R.id.set_user_name)
        userStatus = findViewById(R.id.set_profile_status)
        userProfileImage = findViewById(R.id.set_profile_image)

    }
}