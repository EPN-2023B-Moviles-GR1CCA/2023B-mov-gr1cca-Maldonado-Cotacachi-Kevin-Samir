package com.example.hospital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class ActualizarfalloHospital : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizarfallo_hospital)

        // Obtener datos del hospital seleccionado
        val hospitalSeleccionado = intent.getSerializableExtra("hospitalSeleccionado") as? Hospital

        // Verificar si los datos se recuperaron correctamente
        Log.d("Actualizar_Hospital", "Nombre del hospital seleccionado: ${hospitalSeleccionado?.name}")

        // Rellenar los campos con los datos del hospitalSeleccionado
        findViewById<EditText>(R.id.input_nombreActualizar).setText(hospitalSeleccionado?.name)
        findViewById<EditText>(R.id.input_capacidadActualizar).setText(hospitalSeleccionado?.capacityPatient.toString())
        findViewById<EditText>(R.id.input_ubicacionActualizar).setText(hospitalSeleccionado?.ubication)
        findViewById<EditText>(R.id.input_fundacionActualizar).setText(hospitalSeleccionado?.dateFoundation)
        findViewById<EditText>(R.id.input_esPublicoActualizar).setText(hospitalSeleccionado?.isPublic.toString())

        val botonActualizarHospital = findViewById<Button>(R.id.btn_ActualizarHospital)

        botonActualizarHospital.setOnClickListener {
            // Obtener los nuevos datos ingresados por el usuario
            val nombreHospital = findViewById<EditText>(R.id.input_nombre).text.toString()
            val capacidadHospital = findViewById<EditText>(R.id.input_capacidad).text.toString().toInt()
            val ubicacionHospital = findViewById<EditText>(R.id.input_ubicacion).text.toString()
            val fechaFundacionHospital = findViewById<EditText>(R.id.input_fundacion).text.toString()
            val inputEstadoHospital = findViewById<EditText>(R.id.input_estado).text.toString()
            val esPublicoHospital = inputEstadoHospital.equals("true", ignoreCase = true)

            // Actualizar el objeto hospitalSeleccionado con los nuevos datos
            hospitalSeleccionado?.name = nombreHospital
            hospitalSeleccionado?.capacityPatient = capacidadHospital
            hospitalSeleccionado?.ubication = ubicacionHospital
            hospitalSeleccionado?.dateFoundation = fechaFundacionHospital
            hospitalSeleccionado?.isPublic = esPublicoHospital

            // Llamar al método de actualización en la base de datos
            try{
                Log.d("Actualizar_Hospital", "Antes de actualizar el hospital en la base de datos")
                val resultado = hospitalSeleccionado?.updateHospital()
                Log.d("Actualizar_Hospital", "Después de actualizar el hospital en la base de datos")

                if (resultado != null && resultado > 0) {
                    // Mostrar mensaje de éxito
                    Toast.makeText(this, "Hospital actualizado con éxito", Toast.LENGTH_SHORT).show()

                    // Llamar al método para actualizar la lista en la actividad principal
                    val intent = Intent()
                    setResult(RESULT_OK, intent)

                    // Cierra la actividad de actualización
                    finish()
                } else {
                    // Mostrar mensaje de error
                    Toast.makeText(this, "Error al actualizar el hospital", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Capturar y registrar cualquier excepción
                Log.e("Actualizar_Hospital", "Error al actualizar el hospital", e)
                Toast.makeText(this, "Error inesperado al actualizar el hospital", Toast.LENGTH_SHORT).show()
            }
        }

    }



}