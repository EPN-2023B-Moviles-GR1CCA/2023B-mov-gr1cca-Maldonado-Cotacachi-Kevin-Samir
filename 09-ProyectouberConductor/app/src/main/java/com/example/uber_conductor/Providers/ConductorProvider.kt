package com.example.uber_conductor.Providers

import android.net.Uri
import android.util.Log
import com.example.uber_conductor.Modelos.Conductor
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import java.io.File

class ConductorProvider {
    val db = Firebase.firestore.collection("conductores")
    var storage = FirebaseStorage.getInstance().getReference().child("profile")

    fun create(conductor: Conductor): Task<Void> {
        return db.document(conductor.id!!).set(conductor)
    }

    fun uploadImage(id: String, file: File): StorageTask<UploadTask.TaskSnapshot> {
        var fromFile = Uri.fromFile(file)
        val ref = storage.child("$id.jpg")
        storage = ref
        val uploadTask = ref.putFile(fromFile)

        return uploadTask.addOnFailureListener {
            Log.d("STORAGE", "ERROR: ${it.message}")
        }
    }

    fun createToken(idDriver: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result // TOKEN DE NOTIFICACIONES
                updateToken(idDriver, token)
            }
        }
    }

    fun updateToken(idDriver: String, token: String): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["token"] = token
        return db.document(idDriver).update(map)
    }

    fun getImageUrl(): Task<Uri> {
        return storage.downloadUrl
    }

    fun getDriver(idDriver: String): Task<DocumentSnapshot> {
        return db.document(idDriver).get()
    }

    fun update(conductor: Conductor): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["nombre"] = conductor?.nombre!!
        map["apellido"] = conductor?.apellido!!
        map["celular"] = conductor?.celular!!
        map["Marca"] = conductor?.marca!!
        map["color"] = conductor?.color!!
        map["placa"] = conductor?.numeroPlaca!!
        map["imagen"] = conductor?.imagen!!
        return db.document(conductor?.id!!).update(map)
    }
}