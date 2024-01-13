package com.example.a03_exam

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class Hospital (
    var codigoHospital: Int,
    var name: String?,
    var capacityPatient: Int,
    var ubication: String,
    var dateFoundation: Date,
    var isPublic: Boolean?
    ) {
        // Método toString() para facilitar la visualización de datos
        override fun toString(): String {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
            val formattedDate = dateFormat.format(dateFoundation)

            return "Hospital( Id=$codigoHospital, Nombre='$name', Capacidad=$capacityPatient, Ubication='$ubication', Fundacion=$formattedDate, isPublic=$isPublic)"
        }
}

