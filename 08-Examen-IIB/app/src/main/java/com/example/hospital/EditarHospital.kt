package com.example.hospital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EditarHospital : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_hospital)

        //val codigoHospital = MainActivity.idSeleccionado
        val codigoHospital = intent.getIntExtra("idHospital", -1)

        Log.d("LeerPaciente", "ID del hospital seleccionado: $codigoHospital")
        var hospital = Hospital(null, "", 1, "", "", null, this).getHospitalById(codigoHospital)

        // Asumiendo que estos son los IDs de tus EditText en activity_leer_paciente.xml
        val nombreHospital = findViewById<EditText>(R.id.input_nameUpdate)
        val capacidadHospital = findViewById<EditText>(R.id.input_capacidadUpdate)
        val ubicacionHospital = findViewById<EditText>(R.id.input_ubicUpdate)
        val fechaFundacionHospital = findViewById<EditText>(R.id.input_fundacionUpdate)
        val inputEstadoHospital = findViewById<EditText>(R.id.input_estadoUpdate)

        // Rellenar los EditText con la información actual del hospital
        nombreHospital.setText(hospital.name.toString())
        capacidadHospital.setText(hospital.capacityPatient.toString())
        ubicacionHospital.setText(hospital.ubication.toString())
        fechaFundacionHospital.setText(hospital.dateFoundation.toString())
        inputEstadoHospital.setText(hospital.isPublic.toString())

        val botonActualizarYregresarMain = findViewById<Button>(R.id.btn_regresarMain)

        botonActualizarYregresarMain.setOnClickListener {
            // Obtener los nuevos datos ingresados por el usuario
            val nuevoNombreH = nombreHospital.text.toString()
            val nuevaCapacidadH = capacidadHospital.text.toString().toIntOrNull() ?: 0
            val nuevaUbiH = ubicacionHospital.text.toString()
            val nuevaFechaH = fechaFundacionHospital.text.toString()
            val nuevoEstadoH = inputEstadoHospital.text.toString()

            // Actualizar los datos del hospital
            hospital.name = nuevoNombreH
            hospital.capacityPatient = nuevaCapacidadH
            hospital.ubication = nuevaUbiH
            hospital.dateFoundation = nuevaFechaH
            hospital.isPublic = nuevoEstadoH.toBoolean()

            val resultado = hospital.updateHospital(
                nuevoNombreH,
                nuevaCapacidadH,
                nuevaUbiH,
                nuevaFechaH,
                nuevoEstadoH.toBoolean()
            )

            // Después de la actualización y obtener los nuevos datos
            if (resultado > 0) {
                Toast.makeText(this, "REGISTRO ACTUALIZADO", Toast.LENGTH_LONG).show()

                // Crear un Intent para regresar a MainActivity
                val intent = Intent()
                intent.putExtra("idHospital", hospital.codigoHospital)
                intent.putExtra("actualizacionRealizada", true)
                setResult(RESULT_OK, intent)

                // Cerrar la actividad actual
                finish()
            } else {
                Toast.makeText(this, "ERROR AL ACTUALIZAR REGISTRO", Toast.LENGTH_LONG).show()
            }

        }


    }
}