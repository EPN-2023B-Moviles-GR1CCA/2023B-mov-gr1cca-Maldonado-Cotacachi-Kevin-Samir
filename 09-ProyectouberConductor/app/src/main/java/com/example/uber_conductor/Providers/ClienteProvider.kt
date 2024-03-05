package com.example.uber_conductor.Providers

import com.example.uber_conductor.Modelos.Cliente
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ClienteProvider {
    val db = Firebase.firestore.collection("Clientes")

    fun create(cliente: Cliente): Task<Void> {
        return db.document(cliente.id!!).set(cliente)
    }

    fun getClientById(id: String): Task<DocumentSnapshot> {
        return db.document(id).get()
    }
}