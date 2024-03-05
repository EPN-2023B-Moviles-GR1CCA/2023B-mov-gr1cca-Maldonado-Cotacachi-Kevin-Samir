package com.example.uber_cliente_proyecto.Providers

import android.net.Uri
import android.util.Log
import com.example.uber_cliente_proyecto.Modelos.Cliente
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import java.io.File

class ClienteProvider {
    val db = Firebase.firestore.collection("Clientes")
    var storage = FirebaseStorage.getInstance().getReference().child("profile")

    fun create(cliente: Cliente): Task<Void> {
        return db.document(cliente.id!!).set(cliente)
    }
    fun getClientById(id: String): Task<DocumentSnapshot> {
        return db.document(id).get()
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

    fun getImageUrl(): Task<Uri> {
        return storage.downloadUrl
    }

    fun createToken(idClient: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result // TOKEN DE NOTIFICACIONES
                updateToken(idClient, token)
            }
        }
    }

    fun updateToken(idClient: String, token: String): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["token"] = token
        return db.document(idClient).update(map)
    }

    fun update(client: Cliente): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["nombre"] = client?.nombre!!
        map["apellido"] = client?.apellido!!
        map["celular"] = client?.telefono!!
        map["imagen"] = client?.imagen!!
        return db.document(client?.id!!).update(map)
    }

}