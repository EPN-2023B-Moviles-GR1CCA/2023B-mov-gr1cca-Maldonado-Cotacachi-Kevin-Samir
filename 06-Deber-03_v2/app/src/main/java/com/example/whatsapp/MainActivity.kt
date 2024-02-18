package com.example.whatsapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var mToolbar: Toolbar
    private lateinit var myViewPager : ViewPager
    private lateinit var myTabLayout : TabLayout
    private lateinit var myTabsAccessorAdapter: TabsAccessorAdapter
    private var currentUser: FirebaseUser? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var RootRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        RootRef = FirebaseDatabase.getInstance().getReference()

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
        }else{
            VerifyUserExistence()
        }
    }

    private fun VerifyUserExistence() {
        val currentUserID: String? = mAuth.currentUser?.uid
        val context = this
        if (currentUserID != null) {
            RootRef.child("Users").child(currentUserID).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot)
                {
                    if ((dataSnapshot.child("name").exists())) {
                        // El usuario existe en la base de datos
                        Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show()
                    } else {
                        // El usuario no existe en la base de datos
                        sendUserToSettingsActivity()

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Ocurrió un error al leer la base de datos
                    // Manejar el error aquí
                }
            })
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //Solo era esta linea de abajo
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
            sendUserToSettingsActivity()
        }
        if(item.itemId == R.id.main_create_group_option){
            RequestNewGroup()
        }
        if(item.itemId == R.id.main_find_friends_option){

        }

        return true
    }

    private fun RequestNewGroup() {
        val builder = AlertDialog.Builder(this,R.style.AlertDialog)
        builder.setTitle("Enter Group Name:")
        val groupNameField = EditText(this)
        groupNameField.setHint("e.g Coding Cafe")
        builder.setView(groupNameField)

        builder.setPositiveButton("Create") { dialogInterface, i ->
            // Aquí colocarías el código que quieres ejecutar cuando se haga clic en el botón "Create"
            val groupName = groupNameField.text.toString()
            if(TextUtils.isEmpty(groupName)){
                Toast.makeText(this, "Please write Group Name...", Toast.LENGTH_SHORT).show()
            }
            else{
                CreateNewGroup(groupName)
            }
        }

        builder.setNegativeButton("Cancel") { dialogInterface, i ->
            // Aquí colocarías el código que quieres ejecutar cuando se haga clic en el botón "Create"
           dialogInterface.cancel()
        }

        builder.show()
    }

    private fun CreateNewGroup(groupName: String) {
        RootRef.child("Groups").child(groupName).setValue("")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, groupName + "group is Created Successfuly", Toast.LENGTH_SHORT).show()
                }else{
                    val message = task.exception?.message ?: "Unknown error"
                    Toast.makeText(this, groupName+ "group wasn't Created -> " + "Error: $message", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun sendUserToLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(loginIntent)
        finish() // Finaliza MainActivity para que no se pueda volver atrás después del inicio de sesión
    }
    private fun sendUserToSettingsActivity() {
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(settingsIntent)
        finish() // Finaliza MainActivity para que no se pueda volver atrás después del inicio de sesión
    }


}