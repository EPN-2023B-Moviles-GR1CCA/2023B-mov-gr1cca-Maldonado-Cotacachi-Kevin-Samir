package com.example.uber_conductor.Providers

import android.util.Log
import com.example.uber_conductor.Modelos.HistorialViaje
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistorialProvider {
    val db = Firebase.firestore.collection("Historial")
    val authProvider = AuthProvider()

    fun create(history: HistorialViaje): Task<DocumentReference> {
        return db.add(history).addOnFailureListener {
            Log.d("FIRESTORE", "ERROR: ${it.message}")
        }
    }

    fun getHistoryById(id: String): Task<DocumentSnapshot> {
        return db.document(id).get()
    }

    fun getLastHistory(): Query { // CONSULTA COMPUESTA - INDICE
        return db.whereEqualTo("idDriver", authProvider.getId()).orderBy("timestamp", Query.Direction.DESCENDING).limit(1)
    }

    fun getHistories(): Query { // CONSULTA COMPUESTA - INDICE
        return db.whereEqualTo("idDriver", authProvider.getId()).orderBy("timestamp", Query.Direction.DESCENDING)
    }

    fun getBooking(): Query {
        return db.whereEqualTo("idDriver", authProvider.getId())
    }

    fun updateCalificationToClient(id: String, calification: Float): Task<Void> {
        return db.document(id).update("calificationToClient", calification).addOnFailureListener { exception ->
            Log.d("FIRESTORE", "ERROR: ${exception.message}")
        }
    }

}