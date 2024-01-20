package com.example.hospital

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

    private val hospital: Hospital by lazy {
        Hospital(null,"", 0, "", "", null, this)
    }
    private var adaptador: ArrayAdapter<String>? = null

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

            val padHospital = Hospital(
                null,
                nombreHospital,
                capacidadHospital,
                ubicacionHospital,
                fechaFundacionHospital,
                esPublicoHospital, this
            )

            val resultado = padHospital.crearHospital()
            try {

                if (resultado != -1L) {
                    Log.d("RegistroHospital", "Hospital creado con éxito. ID del hospital: $resultado")
                    mostrarSnackbar("Hospital creado con éxito")
                    // Crear un Intent para devolver datos a la actividad principal

                    val intent = Intent()
                    intent.putExtra("nombre", nombreHospital)
                    intent.putExtra("capacidad", capacidadHospital)
                    intent.putExtra("ubicacion", ubicacionHospital)
                    intent.putExtra("fechaFundacion", fechaFundacionHospital)
                    intent.putExtra("esPublico", esPublicoHospital)
                    setResult(RESULT_OK, intent)

                    finish()  // Cierra la actividad actual y vuelve a la actividad anterior

                } else {
                    mostrarSnackbar("Error al crear el hospital")
                    // Puedes manejar el error de alguna manera si es necesario
                }

            } catch (e: Exception) {
                Log.e("RegistroHospital", "Error al guardar en la base de datos: ${e.message}")
                mostrarSnackbar("Error al guardar en la base de datos: ${e.message}")
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