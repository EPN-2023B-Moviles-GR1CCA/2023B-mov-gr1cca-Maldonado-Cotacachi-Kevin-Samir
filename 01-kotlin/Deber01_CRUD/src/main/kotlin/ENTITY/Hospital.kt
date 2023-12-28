import java.io.Serializable
import java.time.LocalDate
//import java.util.* //To date

data class Hospital(
    val nombre: String,
    val capacidadTotal: Int,
    val ubicacion: String,
    val fechaFundacion: LocalDate,
    val serviciosDisponibles: List<String>,
    val esPublico: Boolean
) : Serializable {
    override fun toString(): String {
        return "Hospital: $nombre, Capacidad: $capacidadTotal, Ubicación: $ubicacion, Fundación: $fechaFundacion, Servicios: $serviciosDisponibles, Público: $esPublico"
    }
}
