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

    //Listar hospitales

    /*
    fun showListViewHospitales(){
        val hospital_ = Hospital("",
            null,"",
            "",null,this
        )
        val listView = findViewById<ListView>(R.id.lv_listaHospitales)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            hospital_.obtenerTodosLosHospitales()
        )
        //Actulizar la interfaz del Hospital es decir el listview
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        registerForContextMenu(listView)
    }
    */


/*
    fun showListViewHospitales() {

        val hospital = Hospital("", 0, "", "", null, this)

        val listView = findViewById<ListView>(R.id.lv_listaHospitales)

        val hospitales = hospital.obtenerTodosLosHospitales()

        // Utilizar un adaptador personalizado si es necesario
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            hospitales.map { it.toString() }
        )

        listView.adapter = adaptador
        registerForContextMenu(listView)
    }*/



    /*
    fun showListViewHospitales() {

        val hospital = Hospital("", 0, "", "", null, this)

        val listView = findViewById<ListView>(R.id.lv_listaHospitales)

        val hospitales = hospital.obtenerTodosLosHospitales()

        if (adaptador == null) {
            // Crear el adaptador solo si aún no existe
            adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                hospitales.map { it.toString() }
            )
            listView.adapter = adaptador
            registerForContextMenu(listView)

        } else {
            // Actualizar datos del adaptador existente
            adaptador?.clear()
            adaptador?.addAll(hospitales.map { it.toString() })
            adaptador?.notifyDataSetChanged()
        }
    }

     */


    private fun volverAActividadPrincipal() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        //finish()  // Cierra la actividad actual para que no se acumulen en la pila de actividades
    }

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