import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

data class Paciente(
    val nombre: String,
    val edad: Int,
    val genero: String,
    val enfermedad: String,
    val fechaIngreso: LocalDate,
    val altura: BigDecimal,
    val tieneAlergias: Boolean
) : Serializable {
    override fun toString(): String {
        return "Paciente: $nombre, Edad: $edad, Género: $genero, Enfermedad: $enfermedad, Ingreso: $fechaIngreso, Altura: $altura, Alergias: $tieneAlergias"
    }
}
