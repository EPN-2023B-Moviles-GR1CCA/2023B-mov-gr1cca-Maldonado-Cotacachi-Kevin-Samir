package ENTITY

import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

data class Patient(
    val name: String,
    val age: Int,
    val genero: String,
    val disease: String,
    val dateAdmission: LocalDate,
    val height: BigDecimal,
    val haveAllergies: Boolean
) : Serializable {
    override fun toString(): String {
        return "Paciente: $name, Edad: $age, Género: $genero, Enfermedad: " +
                "$disease, Ingreso: $dateAdmission, Altura: $height, Alergias: $haveAllergies"
    }
}
