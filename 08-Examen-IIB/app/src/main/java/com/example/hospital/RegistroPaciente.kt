package com.example.hospital

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class RegistroPaciente : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_paciente)

        //crear
        val botonGuardarPaciente = findViewById<Button>(R.id.btn_crearPaciente)
        botonGuardarPaciente
            .setOnClickListener {
                Log.d("RegistroPaciente", "Botón Guardar Presionado")

                val nombrePaciente = findViewById<EditText>(R.id.inputNombrePaciente).text.toString()
                val edadPaciente = findViewById<EditText>(R.id.input_edadPaciente).text.toString().toIntOrNull()
                val fechaAdmisionPaciente = findViewById<EditText>(R.id.input_FechaAdmisionPaciente).text.toString()
                val pesoPaciente = findViewById<EditText>(R.id.input_pesoPaciente).text.toString().toIntOrNull()
                val inputAlergiasPaciente = findViewById<EditText>(R.id.input_alergiasPaciente).text.toString()
                val tieneAlergiasPaciente = inputAlergiasPaciente.equals("true", ignoreCase = true)
                val codHospitalPaciente = findViewById<EditText>(R.id.input_codHospital).text.toString().toIntOrNull()


                if (nombrePaciente.isBlank() || edadPaciente == null || pesoPaciente == null || codHospitalPaciente == null) {
                    mostrarSnackbar("Por favor, complete todos los campos correctamente.")
                    return@setOnClickListener
                }

                val nuevoPaciente = Paciente(
                    Paciente.generateNewId(), // Usando el nuevo método para generar un ID único
                    nombrePaciente,
                    edadPaciente,
                    fechaAdmisionPaciente,
                    pesoPaciente,
                    tieneAlergiasPaciente,
                    codHospitalPaciente,
                )

                nuevoPaciente.crearPaciente { isSuccess ->
                    if (isSuccess) {
                        Log.d("RegistroPaciente", "Paciente creado con éxito")
                        Paciente().obtenerTodosLosPacientes(codHospitalPaciente) { listaPacientes ->

                            listaPacientes.add(nuevoPaciente)

                            // Enviar la lista de pacientes de vuelta a MainActivity
                            val intent = Intent()
                            intent.putExtra("listaPacientes", listaPacientes)
                            setResult(Activity.RESULT_OK, intent)
                            finish()  // Cierra la actividad actual y vuelve a la actividad anterior
                        }
                    } else {
                        Log.e("RegistroPaciente", "Error al crear el paciente")
                        mostrarSnackbar("Error al crear el paciente")
                        // Puedes manejar otros casos de error aquí si es necesario
                    }
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