package com.example.hospital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EditarPaciente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_paciente)

        // Obtener la información del paciente de los extras del Intent
        val idPaciente = intent.getIntExtra("idPaciente", -1)
        val nombrePaciente = intent.getStringExtra("nombrePaciente") ?: ""
        val edadPaciente = intent.getIntExtra("edadPaciente", 0)
        val fechaAdmisionPaciente = intent.getStringExtra("fechaAdmisionPaciente") ?: ""
        val pesoPaciente = intent.getIntExtra("pesoPaciente", 0)
        val alergiasPaciente = intent.getStringExtra("alergiasPaciente") ?: ""
        val codHospitalPaciente = intent.getIntExtra("codHospitalPaciente", 0)

        // Vincular los EditText en la interfaz de usuario a variables
        val editTextNombre = findViewById<EditText>(R.id.input_nombreUpdate)
        val editTextEdad = findViewById<EditText>(R.id.input_edadUpdate)
        val editTextFechaAdmision = findViewById<EditText>(R.id.input_fechaAdmisionUpdate)
        val editTextPeso = findViewById<EditText>(R.id.input_pesoUpdate)
        val editTextAlergias = findViewById<EditText>(R.id.input_alergiasUpdate)
        val editTextCodHospital = findViewById<EditText>(R.id.input_codHospitalUpdate)

        // Configurar los campos con la información del paciente
        editTextNombre.setText(nombrePaciente)
        editTextEdad.setText(edadPaciente.toString())
        editTextFechaAdmision.setText(fechaAdmisionPaciente)
        editTextPeso.setText(pesoPaciente.toString())
        editTextAlergias.setText(alergiasPaciente)
        editTextCodHospital.setText(codHospitalPaciente.toString())

        // Configurar el botón de actualización
        val btnActualizar = findViewById<Button>(R.id.btn_updatePaciente)
        btnActualizar.setOnClickListener {
            // Obtener los nuevos valores de los campos
            val nuevoNombre = editTextNombre.text.toString()
            val nuevaEdad = editTextEdad.text.toString().toIntOrNull() ?: 0
            val nuevaFechaAdmision = editTextFechaAdmision.text.toString()
            val nuevoPeso = editTextPeso.text.toString().toIntOrNull() ?: 0
            val nuevasAlergias = editTextAlergias.text.toString()
            // Convertir el String a Boolean
            val tieneAlergias = nuevasAlergias == "1" //true


            val nuevoCodHospital = editTextCodHospital.text.toString().toIntOrNull() ?: 0

            // Validar que los campos requeridos no estén vacíos
            if (nuevoNombre.isNotEmpty() && nuevaFechaAdmision.isNotEmpty() && nuevasAlergias.isNotEmpty()) {
                // Crear un objeto Paciente con los nuevos valores
                val pacienteActualizado = Paciente(
                    idPaciente,
                    nuevoNombre,
                    nuevaEdad,
                    nuevaFechaAdmision,
                    nuevoPeso,
                    tieneAlergias,
                    nuevoCodHospital,
                    this
                )

                // Actualizar el paciente en la base de datos
                val resultado = pacienteActualizado.updatePaciente(
                    idPaciente,
                    nuevoNombre,
                    nuevaEdad,
                    nuevaFechaAdmision,
                    nuevoPeso,
                    tieneAlergias,
                    nuevoCodHospital
                )

                if (resultado > 0) {
                    Toast.makeText(this, "Paciente actualizado correctamente", Toast.LENGTH_SHORT).show()

                    // Puedes agregar lógica adicional aquí, como regresar a la actividad principal
                    // o realizar otras acciones después de la actualización
                    // Por ejemplo, regresar a la actividad MainPaciente:

                    //HICE ESTO ULTIMO
                    //val intent = Intent(this, MainPaciente::class.java)
                    //startActivity(intent)
                    val intent = Intent()
                    intent.putExtra("idHospital", nuevoCodHospital)  // Ajusta esto según tus necesidades
                    intent.putExtra("actualizacionRealizada", true)
                    setResult(RESULT_OK, intent)

                    finish()
                } else {
                    Toast.makeText(this, "Error al actualizar el paciente", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos requeridos", Toast.LENGTH_SHORT).show()
            }
        }

    }
}