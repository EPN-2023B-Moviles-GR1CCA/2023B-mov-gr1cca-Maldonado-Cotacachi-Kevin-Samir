package com.example.hospital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class RegistroPaciente : AppCompatActivity() {

    private val paciente: Paciente by lazy {
        Paciente(
            null,
            "",
            0,
            "",
            0,
            null,
            0,
            this
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_paciente)

        //crear
        val botonGuardarPaciente = findViewById<Button>(R.id.btn_crearPaciente)
        botonGuardarPaciente
            .setOnClickListener {
                Log.d("RegistroPaciente", "Botón Guardar Presionado")

                val nombrePaciente = findViewById<EditText>(R.id.inputNombrePaciente).text.toString()
                val edadPaciente = findViewById<EditText>(R.id.input_edadPaciente).text.toString().toInt()
                val fechaAdmisionPaciente =
                    findViewById<EditText>(R.id.input_FechaAdmisionPaciente).text.toString()
                val pesoPaciente = findViewById<EditText>(R.id.input_pesoPaciente).text.toString().toInt()
                val inputAlergiasPaciente =
                    findViewById<EditText>(R.id.input_alergiasPaciente).text.toString()
                val tieneAlergiasPaciente = inputAlergiasPaciente.equals("true", ignoreCase = true)
                val codHospitalPaciente =
                    findViewById<EditText>(R.id.input_codHospital).text.toString().toInt()

                val nuevoPaciente = Paciente(
                    null,
                    nombrePaciente,
                    edadPaciente,
                    fechaAdmisionPaciente,
                    pesoPaciente,
                    tieneAlergiasPaciente,
                    codHospitalPaciente,
                    this
                )
                try {
                    Log.d("RegistroPaciente", "Antes de crear el paciente.")
                    val resultado = nuevoPaciente.crearPaciente()
                    Log.d("RegistroPaciente", "Después de crear el paciente. ID del paciente: $resultado")

                    if (resultado != -1L) {
                        Log.d("RegistroPaciente", "Paciente creado con éxito. ID del paciente: $resultado")
                        mostrarSnackbar("Paciente creado con éxito")
                        // Crear un Intent para devolver datos a la actividad principal

                        val intent = Intent()
                        intent.putExtra("nombre", nombrePaciente)
                        intent.putExtra("edad", edadPaciente)
                        intent.putExtra("fechaAdmision", fechaAdmisionPaciente)
                        intent.putExtra("peso", pesoPaciente)
                        intent.putExtra("tieneAlergias", tieneAlergiasPaciente)
                        intent.putExtra("codHospital", codHospitalPaciente)
                        setResult(RESULT_OK, intent)

                        finish()  // Cierra la actividad actual y vuelve a la actividad anterior

                    } else {
                        mostrarSnackbar("Error al crear el paciente")
                        // Puedes manejar el error de alguna manera si es necesario
                    }

                } catch (e: Exception) {
                    Log.e("RegistroPaciente", "Error al guardar en la base de datos: ${e.message}")
                    mostrarSnackbar("Error al guardar en la base de datos: ${e.message}")
                    e.printStackTrace()

                }
            }
    }



    fun mostrarSnackbar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.cl_registroPaciente),
                texto,
                Snackbar.LENGTH_LONG
            )
            .show()
    }
}