package com.example.hospital

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
class Paciente (

    var idPacient: Int?,
    var name: String?,
    var age: Int?,
    var dateAdmission: String?,
    var weight: Int?,
    var alergias: Boolean?,
    var codHospital : Int,
) : Parcelable {


    companion object {
        private const val TAG = "Paciente"
        private var lastUsedId = 0

        // Método para generar un nuevo ID único para un paciente
        fun generateNewId(): Int {
            lastUsedId++
            return lastUsedId
        }

        @JvmField
        val CREATOR: Parcelable.Creator<Paciente> = object : Parcelable.Creator<Paciente> {
            override fun createFromParcel(parcel: Parcel): Paciente {
                return Paciente(parcel)
            }

            override fun newArray(size: Int): Array<Paciente?> {
                return arrayOfNulls(size)
            }
        }
    }
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(idPacient)
        parcel.writeString(name)
        parcel.writeValue(age)
        parcel.writeString(dateAdmission)
        parcel.writeValue(weight)
        parcel.writeValue(alergias)
        parcel.writeInt(codHospital)
    }

    override fun describeContents(): Int {
        return 0
    }
    // Constructor vacío necesario para Firestore
    constructor() : this(null, null, null, "", 0, null, 0)

    //CREAR

    // Método para insertar un paciente en Firestore
    fun crearPaciente(callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val paciente = hashMapOf(
            "idPaciente" to this.idPacient,
            "nombre" to this.name,
            "edad" to this.age,
            "fechaAdmision" to this.dateAdmission,
            "peso" to this.weight,
            "tieneAlergias" to this.alergias,
            "codHospital" to this.codHospital
        )

        db.collection("pacientes")
            .add(paciente)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Paciente agregado con ID: ${documentReference.id}")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error al agregar paciente", e)
                callback(false)
            }
    }

    // Método para obtener todos los pacientes desde Firestore
    fun obtenerTodosLosPacientes(codHospital: Int, callback: (ArrayList<Paciente>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val lista = ArrayList<Paciente>()

        db.collection("pacientes")
            .whereEqualTo("codHospital", codHospital)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val paciente = Paciente(
                        (document["idPaciente"] as? Long)?.toInt() ?: 0,
                        document["nombre"] as? String,
                        (document["edad"] as? Long)?.toInt() ?: 0,
                        document["fechaAdmision"] as? String ?: "",
                        (document["peso"] as? Long)?.toInt() ?: 0,
                        document["tieneAlergias"] as? Boolean,
                        (document["codHospital"] as? Long)?.toInt() ?: 0
                    )

                    lista.add(paciente)
                }
                callback(lista)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error al obtener pacientes", exception)
                callback(ArrayList()) // Devuelve una lista vacía en caso de error
            }
    }

    // Método para actualizar un paciente en Firestore
    fun updatePaciente(
        idPaciente: Int,
        nuevoNombre: String,
        nuevaEdad: Int,
        nuevaFechaAdmision: String,
        nuevoPeso: Int,
        tieneAlergias: Boolean,
        nuevoCodHospital: Int,
        callback: (Boolean) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()

        // Crear un mapa con los nuevos valores del paciente
        val pacienteMap = hashMapOf(
            "nombre" to nuevoNombre,
            "edad" to nuevaEdad,
            "fechaAdmision" to nuevaFechaAdmision,
            "peso" to nuevoPeso,
            "tieneAlergias" to tieneAlergias,
            "codHospital" to nuevoCodHospital
        )

        // Convertir el mapa a Map<String, Any>
        val pacienteMapAny: Map<String, Any> = pacienteMap

        db.collection("pacientes")
            .document(idPaciente.toString())
            .update(pacienteMapAny)
            .addOnSuccessListener {
                Log.d(TAG, "Paciente actualizado en Firestore con ID: $idPaciente")
                callback(true)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error al actualizar paciente en Firestore", exception)
                callback(false)
            }
    }


    // Método para eliminar un paciente de Firestore
    fun deletePaciente(idPaciente: String, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("pacientes")
            .whereEqualTo("idPaciente", idPaciente)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // No se encontró ningún paciente con el ID especificado
                    Log.d(TAG, "No se encontró ningún paciente con ID: $idPaciente")
                    callback(false)
                    return@addOnSuccessListener
                }

                // Eliminar el paciente encontrado
                val pacienteDocument = querySnapshot.documents[0]
                pacienteDocument.reference
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "Paciente eliminado de Firestore con ID: $idPaciente")
                        callback(true)
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error al eliminar paciente de Firestore", exception)
                        callback(false)
                    }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error al buscar paciente en Firestore", exception)
                callback(false)
            }
    }

    override fun toString(): String {
        val alergiasString = if (alergias == true) "Sí" else "No"
        return "Nombre: $name\nEdad: $age\nFecha de Admisión: $dateAdmission\nPeso: $weight\n¿Alergias?: $alergiasString\nCódigo de Hospital: $codHospital"
    }






}

