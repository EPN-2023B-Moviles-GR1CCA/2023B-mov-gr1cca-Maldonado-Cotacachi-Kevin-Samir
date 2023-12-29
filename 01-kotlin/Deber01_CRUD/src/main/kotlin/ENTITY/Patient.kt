package ENTITY

import java.io.Serializable
import java.time.LocalDate

data class Patient(
    var name: String,
    var age: Int,
    var gender: String,
    var disease: String,
    var dateAdmission: LocalDate,
    var weight: Double,
    var haveAllergies: Boolean
) : Serializable {
    override fun toString(): String {
        return "Paciente: $name, Edad: $age, Género: $gender, " +
                "Enfermedad: $disease, Ingreso: $dateAdmission, Peso: $weight, Alergias: $haveAllergies"
    }
}
