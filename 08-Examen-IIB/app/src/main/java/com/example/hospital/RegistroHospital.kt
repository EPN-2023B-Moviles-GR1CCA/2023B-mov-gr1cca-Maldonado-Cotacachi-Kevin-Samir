package com.example.hospital

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar

class RegistroHospital : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_hospital)

        //crear
        val botonGuardarHospital = findViewById<Button>(R.id.btn_guardarHospital)

        botonGuardarHospital.setOnClickListener {
            Log.d("RegistroHospital", "Botón Guardar Presionado")  // Log de prueba

            val nombreHospital = findViewById<EditText>(R.id.input_nombre).text.toString()
            val capacidadHospital = findViewById<EditText>(R.id.input_capacidad).text.toString().toInt()
            val ubicacionHospital = findViewById<EditText>(R.id.input_ubicacion).text.toString()
            val fechaFundacionHospital = findViewById<EditText>(R.id.input_fundacion).text.toString()
            val inputEstadoHospital = findViewById<EditText>(R.id.input_estado).text.toString()
            val esPublicoHospital = inputEstadoHospital.equals("true", ignoreCase = true)

            val nuevoId = Hospital.generateNewId()
            val padHospital = Hospital(
                nuevoId,
                nombreHospital,
                capacidadHospital,
                ubicacionHospital,
                fechaFundacionHospital,
                esPublicoHospital, this
            )

            padHospital.crearHospital { isSuccess ->
                if (isSuccess) {
                    Log.d("RegistroHospital", "Hospital creado con éxito")
                    mostrarSnackbar("Hospital creado con éxito")
                    // Puedes manejar otros casos de éxito aquí si es necesario
                    // Obtener la lista actualizada de hospitales
                    Hospital().obtenerTodosLosHospitales { listaHospitales ->
                        // Agregar el nuevo hospital a la lista
                        listaHospitales.add(padHospital)
                        // Enviar la lista de hospitales de vuelta a MainActivity
                        val intent = Intent()
                        intent.putExtra("listaHospitales", listaHospitales)
                        setResult(Activity.RESULT_OK, intent)
                        finish()  // Cierra la actividad actual y vuelve a la actividad anterior
                    }

                } else {
                    Log.e("RegistroHospital", "Error al crear el hospital")
                    mostrarSnackbar("Error al crear el hospital")
                    // Puedes manejar otros casos de error aquí si es necesario
                }
            }
        }
    }//Fin OnCreate

    fun mostrarSnackbar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.cl_RegistroHospital),
                texto,
                Snackbar.LENGTH_LONG
            )
            .show()
    }
}