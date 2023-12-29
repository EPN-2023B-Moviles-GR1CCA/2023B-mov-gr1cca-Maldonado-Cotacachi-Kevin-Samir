package ENTITY

import java.io.Serializable
import java.time.LocalDate
//import java.util.* //To date

data class Hospital(
    val name: String,
    var capacityPatient: Int,
    var ubication: String,
    val dateFundation: LocalDate,
   // val services: List<String>,
    var isPublic: Boolean
) : Serializable {
    override fun toString(): String {
        return "Hospital: $name, Capacidad: $capacityPatient, " +
                "Ubicación: $ubication, Fundación: $dateFundation, Público: $isPublic"
    }
}
