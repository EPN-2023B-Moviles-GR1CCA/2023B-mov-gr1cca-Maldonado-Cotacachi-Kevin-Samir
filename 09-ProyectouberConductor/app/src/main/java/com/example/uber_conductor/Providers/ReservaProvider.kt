package com.example.uber_conductor.Providers

import android.util.Log
import com.example.uber_conductor.Modelos.Reserva
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReservaProvider {
    val db = Firebase.firestore.collection("Reservas")
    val authProvider = AuthProvider()

    fun create(reserva: Reserva): Task<Void> {
        return db.document(authProvider.getId()).set(reserva).addOnFailureListener {
            Log.d("FIRESTORE", "ERROR: ${it.message}")
        }
    }

    fun getBooking(): Query {
        return db.whereEqualTo("idDriver", authProvider.getId())
    }

    fun updateStatus(idClient: String, status: String): Task<Void> {
        return db.document(idClient).update("status", status).addOnFailureListener { exception ->
            Log.d("FIRESTORE", "ERROR: ${exception.message}")
        }
    }
}