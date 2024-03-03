package com.example.hospital

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


 class Hospital (
     var codigoHospital: Int?,
     var name: String?,
     var capacityPatient: Int?,
     var ubication: String?,
     var dateFoundation: String?,
     var isPublic: Boolean?,
     val context: Context?,
     ) : Parcelable {

     companion object {
         private const val TAG = "Hospital"
         private var lastUsedId = 0

         // Método para generar un nuevo ID único para un hospital
         fun generateNewId(): Int {
             lastUsedId++
             return lastUsedId
         }

         @JvmField
         val CREATOR: Parcelable.Creator<Hospital> = object : Parcelable.Creator<Hospital> {
             override fun createFromParcel(parcel: Parcel): Hospital {
                 return Hospital(parcel)
             }

             override fun newArray(size: Int): Array<Hospital?> {
                 return arrayOfNulls(size)
             }
         }

     }
     override fun toString(): String {
         // Construir una cadena con la información del hospital
         return "Hospital: $name\nCapacidad: $capacityPatient\nUbicación: $ubication\nFecha de Fundación: $dateFoundation\nEs Público: ${if (isPublic == true) "Sí" else "No"}"
     }
     // Implementación de Parcelable
     constructor(parcel: Parcel) : this(
         parcel.readInt(),
         parcel.readString(),
         parcel.readInt(),
         parcel.readString(),
         parcel.readString(),
         parcel.readByte() != 0.toByte(),
         null
     )

     override fun writeToParcel(parcel: Parcel, flags: Int) {
         parcel.writeInt(codigoHospital ?: -1)
         parcel.writeString(name)
         parcel.writeInt(capacityPatient ?: -1)
         parcel.writeString(ubication)
         parcel.writeString(dateFoundation)
         parcel.writeByte(if (isPublic == true) 1 else 0)
     }

     override fun describeContents(): Int {
         return 0
     }


     // Constructor vacío necesario para Firestore
     constructor() : this(null, null, null, null, null, null,null)


     // Método para insertar un hospital en Firestore
     fun crearHospital(callback: (Boolean) -> Unit) {
         val db = FirebaseFirestore.getInstance()
         val hospital = hashMapOf(
             "codigoHospital" to this.codigoHospital,
             "nombre" to this.name,
             "capacidad" to this.capacityPatient,
             "ubicacion" to this.ubication,
             "fechaFundacion" to this.dateFoundation,
             "esPublico" to this.isPublic
         )

         db.collection("hospitales")
             .add(hospital)
             .addOnSuccessListener { documentReference ->
                 Log.d(TAG, "Hospital agregado con ID: ${documentReference.id}")
                 callback(true)
             }
             .addOnFailureListener { e ->
                 Log.w(TAG, "Error al agregar hospital", e)
                 callback(false)
             }
     }


     // Función para obtener todos los hospitales desde Firestore
     /*fun obtenerTodosLosHospitales(callback: (ArrayList<Hospital>) -> Unit) {
         val db = FirebaseFirestore.getInstance()
         db.collection("hospitales")
             .get()
             .addOnSuccessListener { result ->
                 val lista = ArrayList<Hospital>()
                 for (document in result) {
                     val hospital = Hospital(
                         document["codigoHospital"] as Int?,
                         document["nombre"] as String?,
                         document["capacidad"] as Int?,
                         document["ubicacion"] as String?,
                         document["fechaFundacion"] as String?,
                         document["esPublico"] as Boolean?,
                         context
                     )
                     lista.add(hospital)
                 }
                 callback(lista)
             }
             .addOnFailureListener { exception ->
                 Log.w(TAG, "Error al obtener hospitales", exception)
                 callback(ArrayList())
             }
     }*/

     // Función para obtener todos los hospitales desde Firestore
     fun obtenerTodosLosHospitales(callback: (ArrayList<Hospital>) -> Unit) {
         val db = FirebaseFirestore.getInstance()
         db.collection("hospitales")
             .get()
             .addOnSuccessListener { result ->
                 val lista = ArrayList<Hospital>()
                 for (document in result) {
                     val codigoHospital = document["codigoHospital"] as? Int
                     val nombre = document["nombre"] as String?
                     val capacidad = (document["capacidad"] as Long).toInt()
                     val ubicacion = document["ubicacion"] as String?
                     val fechaFundacion = document["fechaFundacion"] as String?
                     val esPublico = document["esPublico"] as Boolean?

                     val hospital = Hospital(
                         codigoHospital,
                         nombre,
                         capacidad,
                         ubicacion,
                         fechaFundacion,
                         esPublico,
                         context
                     )
                     lista.add(hospital)
                 }
                 callback(lista)
             }
             .addOnFailureListener { exception ->
                 Log.w(TAG, "Error al obtener hospitales", exception)
                 callback(ArrayList())
             }
     }


     // Función para eliminar un hospital desde Firestore
     fun deleteHospital(id: Int, callback: (Boolean) -> Unit) {
         val db = FirebaseFirestore.getInstance()

         db.collection("hospitales")
             .whereEqualTo("codigoHospital", id)
             .get()
             .addOnSuccessListener { result ->
                 if (!result.isEmpty) {
                     for (document in result) {
                         db.collection("hospitales")
                             .document(document.id)
                             .delete()
                             .addOnSuccessListener {
                                 Log.d(TAG, "Hospital eliminado con ID: ${document.id}")
                                 callback(true)
                             }
                             .addOnFailureListener { exception ->
                                 Log.w(TAG, "Error al eliminar hospital", exception)
                                 callback(false)
                             }
                         return@addOnSuccessListener
                     }
                 } else {
                     Log.d(TAG, "No se encontró ningún hospital con ID: $id")
                     callback(false)
                 }
             }
             .addOnFailureListener { exception ->
                 Log.w(TAG, "Error al buscar hospital", exception)
                 callback(false)
             }
     }

     // Función para actualizar un hospital en Firestore
     fun updateHospital(
         nuevoNombre: String,
         nuevaCapacidad: Int,
         nuevaUbicacion: String,
         nuevaFechaFundacion: String,
         esPublico: Boolean,
         callback: (Boolean) -> Unit
     ) {
         val db = FirebaseFirestore.getInstance()
         db.collection("hospitales")
             .whereEqualTo("codigoHospital", codigoHospital)
             .get()
             .addOnSuccessListener { result ->
                 if (!result.isEmpty) {
                     for (document in result) {
                         val data = hashMapOf(
                             "nombre" to nuevoNombre,
                             "capacidad" to nuevaCapacidad,
                             "ubicacion" to nuevaUbicacion,
                             "fechaFundacion" to nuevaFechaFundacion,
                             "esPublico" to esPublico
                         )

                         db.collection("hospitales")
                             .document(document.id)
                             .update(data as Map<String, Any>)
                             .addOnSuccessListener {
                                 Log.d(TAG, "Hospital actualizado con ID: ${document.id}")
                                 callback(true)
                             }
                             .addOnFailureListener { exception ->
                                 Log.w(TAG, "Error al actualizar hospital", exception)
                                 callback(false)
                             }
                         return@addOnSuccessListener
                     }
                 } else {
                     Log.d(TAG, "No se encontró ningún hospital con ID: $codigoHospital")
                     callback(false)
                 }
             }
             .addOnFailureListener { exception ->
                 Log.w(TAG, "Error al buscar hospital", exception)
                 callback(false)
             }
     }

     // Función para obtener un hospital por su ID desde Firestore
     fun getHospitalById(id: Int, callback: (Hospital?) -> Unit) {
         val db = FirebaseFirestore.getInstance()

         db.collection("hospitales")
             .whereEqualTo("codigoHospital", id)
             .get()
             .addOnSuccessListener { result ->
                 if (!result.isEmpty) {
                     for (document in result) {
                         val hospital = Hospital(
                             document["codigoHospital"] as Int?,
                             document["nombre"] as String?,
                             document["capacidad"] as Int?,
                             document["ubicacion"] as String?,
                             document["fechaFundacion"] as String?,
                             document["esPublico"] as Boolean?,
                             this.context
                         )
                         callback(hospital)
                         return@addOnSuccessListener
                     }
                 }
                 callback(null)
             }
             .addOnFailureListener { exception ->
                 Log.w(TAG, "Error al obtener hospital por ID", exception)
                 callback(null)
             }
     }

 }