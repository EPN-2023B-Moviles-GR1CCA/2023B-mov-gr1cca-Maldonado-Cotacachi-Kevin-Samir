package com.example.uber_cliente_proyecto.Providers

import android.net.Uri
import android.util.Log
import com.example.uber_cliente_proyecto.Modelos.Conductor
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import java.io.File

class ConductorProvider {
    val db = Firebase.firestore.collection("Drivers")
    var storage = FirebaseStorage.getInstance().getReference().child("profile")

    fun create(driver: Conductor): Task<Void> {
        return db.document(driver.id!!).set(driver)
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

    fun getDriver(idDriver: String): Task<DocumentSnapshot> {
        return db.document(idDriver).get()
    }

    fun update(driver: Conductor): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["nombre"] = driver?.name!!
        map["apellido"] = driver?.apellido!!
        map["celular"] = driver?.celular!!
        map["marca"] = driver?.marca!!
        map["color"] = driver?.color!!
        map["numeroPlaca"] = driver?.numeroPlaca!!
        map["imagen"] = driver?.imagen!!
        return db.document(driver?.id!!).update(map)
    }
}