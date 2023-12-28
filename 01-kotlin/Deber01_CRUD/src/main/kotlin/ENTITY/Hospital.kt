package ENTITY

import java.io.Serializable
import java.time.LocalDate
//import java.util.* //To date

data class Hospital(
    val name: String,
    val capacityPatient: Int,
    val ubication: String,
    val dateFundation: LocalDate,
   // val services: List<String>,
    val isPublic: Boolean
) : Serializable {
    override fun toString(): String {
        return "Hospital: $name, Capacidad: $capacityPatient, " +
                "Ubicación: $ubication, Fundación: $dateFundation, Público: $isPublic"
    }
}
