package com.example.hospital

import java.text.SimpleDateFormat
import java.util.Date
class Paciente (

    var idPacient: Int,
    var name: String?,
    var age: Int?,
    var gender: String?,
    var dateAdmission: Date,
    var weight: Double,
    var haveAllergies: Boolean?,
) {
    // Método toString() para facilitar la visualización de datos
    override fun toString(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = dateFormat.format(dateAdmission)

        return "Paciente(Id=$idPacient,name='$name', age=$age, gender='$gender', dateAdmission=$formattedDate, weight=$weight, haveAllergies=$haveAllergies)"

    }

}

