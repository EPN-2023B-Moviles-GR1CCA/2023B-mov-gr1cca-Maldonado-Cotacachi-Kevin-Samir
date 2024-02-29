package com.example.whatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar

class GroupChatActivity : AppCompatActivity() {

    private lateinit var mToolbar : Toolbar
    private lateinit var sendMessageButton: ImageButton
    private lateinit var userMessageInput : EditText
    private lateinit var mScrollView: ScrollView
    private lateinit var displayTextMessage :TextView
    private lateinit var currentGroupName: String
    private lateinit var currentUserID : String
    private lateinit var currentUserName : String
    private lateinit var currentDate : String
    private lateinit var currentTime : String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var UserRef: DatabaseReference
    private lateinit var GroupNameRef: DatabaseReference
    private lateinit var GroupMessageKeyRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)

        currentGroupName = intent.getStringExtra("groupName").toString()
        Toast.makeText(this,currentGroupName,Toast.LENGTH_SHORT).show()

        mAuth = FirebaseAuth.getInstance()
        currentUserID = mAuth.currentUser?.uid ?: ""
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users")
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName)

        initializeFields()

        GetUserInfo()
        sendMessageButton.setOnClickListener{
            SaveMessageInfoToDataBase()

            userMessageInput.setText("")
        }
    }

    private fun SaveMessageInfoToDataBase() {
        val message = userMessageInput.text.toString()
        val messageKEY = GroupNameRef.push().key
        val childKey = messageKEY ?: "defaultKey" // Si messageKEY es nulo, asigna "defaultKey" en su lugar

        if(TextUtils.isEmpty(message)){
            Toast.makeText(this,"PLease write message firs",Toast.LENGTH_SHORT).show()

        }else{
            val calForFate = Calendar.getInstance()
            val currentDateFormat = SimpleDateFormat("MMM dd, yyyy")
            currentDate = currentDateFormat.format(calForFate.time)

            val calForTime = Calendar.getInstance()
            val currentTimeFormat = SimpleDateFormat("hh:mm a ")


            currentTime = currentTimeFormat.format(calForTime.time)

            val groupMessageKey = HashMap<String, Any>()


            GroupNameRef.updateChildren(groupMessageKey)

            GroupMessageKeyRef = GroupNameRef.child(childKey)

            val messageInfoMap = HashMap<String, Any>()
                messageInfoMap.put("name",currentUserName)
                messageInfoMap.put("message",message)
                messageInfoMap.put("date",currentDate)
                messageInfoMap.put("time",currentTime)
            GroupMessageKeyRef.updateChildren(messageInfoMap)



        }
    }

    private fun GetUserInfo() {
        UserRef.child(currentUserID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("name").getValue().toString()

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Ocurrió un error al leer la base de datos
            }
        })
    }

    override fun onStart() {
        super.onStart()
        GroupNameRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                if(dataSnapshot.exists()){
                    DisplayMessages(dataSnapshot)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Implementa la lógica para cuando se modifica un hijo
                if(dataSnapshot.exists()){
                    DisplayMessages(dataSnapshot)
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                // Implementa la lógica para cuando se elimina un hijo
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Implementa la lógica para cuando se mueve un hijo
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja el caso de error en la operación
            }
        })

    }
    private fun DisplayMessages(dataSnapshot : DataSnapshot){
        val iterator = dataSnapshot.children.iterator()

        while (iterator.hasNext()) {

            //val chatDate = iterator.next() as DataSnapshot
            val chatDate = (iterator.next() as DataSnapshot).getValue(String::class.java)
            val chatMessage = (iterator.next() as DataSnapshot).getValue(String::class.java)
            val chatName = (iterator.next() as DataSnapshot).getValue(String::class.java)
            val chatTime = (iterator.next() as DataSnapshot).getValue(String::class.java)

            displayTextMessage.append(chatName +" ::\n"+ chatMessage + "\n"+ chatTime+" "+ chatDate + "\n\n")
        }
    }
    private fun initializeFields() {
        mToolbar = findViewById(R.id.group_chat_bar_layout)
        setSupportActionBar(mToolbar)
        supportActionBar?.setTitle(currentGroupName)

        sendMessageButton = findViewById(R.id.send_message_button)
        userMessageInput = findViewById(R.id.input_group_message)
        displayTextMessage = findViewById(R.id.group_chat_text_display)
        mScrollView = findViewById(R.id.my_scroll_view)
    }
}