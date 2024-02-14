package com.example.whatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var mToolbar: Toolbar
    private lateinit var myViewPager : ViewPager
    private lateinit var myTabLayout : TabLayout
    private lateinit var myTabsAccessorAdapter: TabsAccessorAdapter
    private var currentUser: FirebaseUser? = null
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        mToolbar = findViewById(R.id.main_page_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.title = "WhatsApp"

        myViewPager = findViewById(R.id.main_tabs_pager)
        myTabsAccessorAdapter = TabsAccessorAdapter(supportFragmentManager)
        myViewPager.setAdapter(myTabsAccessorAdapter)

        myTabLayout = findViewById(R.id.main_tabs)
        myTabLayout.setupWithViewPager(myViewPager)

    }

    override fun onStart() {
        super.onStart()

        currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser == null){
            sendUserToLoginActivity()
        }
    }

    private fun sendUserToLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        //finish() // Finaliza MainActivity para que no se pueda volver atrás después del inicio de sesión
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

       // return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.options_menu,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        super.onOptionsItemSelected(item)
        if(item.itemId == R.id.main_logout_option){
            mAuth.signOut()
            sendUserToLoginActivity()
        }
        if(item.itemId == R.id.main_settings_option){

        }
        if(item.itemId == R.id.main_find_friends_option){

        }
        return true
    }


}