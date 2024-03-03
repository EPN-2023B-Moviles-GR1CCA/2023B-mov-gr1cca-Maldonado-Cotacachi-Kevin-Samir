package com.example.hospital

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    companion object{
        var idSeleccionado = 0
    }
    private var adaptador: ArrayAdapter<String>? = null
    private lateinit var listView: ListView
    private var hospitales: MutableList<Hospital> = mutableListOf()
    private val TU_CODIGO_DE_EDICION_HOSPITAL = 1 // Número único para la edición de hospitales

    private val iniciarRegistroActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Actualizar el ListView con los datos del nuevo hospital
                Log.d("MainActivity", "Cantidad de hospitales obtenidos: ${hospitales.size}")

                showListViewHospitales()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.lv_listaHospitales)
        registerForContextMenu(listView)

        // Llamamos a showListViewHospitales() para mostrar la lista al iniciar la actividad
        showListViewHospitales()

        //Encontrar una vista por id, R: para acceder a los recursos
        //Para ir a la actvidad 2: RegistroHospital
        val botonCrearHospital = findViewById<Button>(R.id.btn_crearHospital)
        botonCrearHospital
            .setOnClickListener{
                //Para ir a la actvidad 2: RegistroHospital
                irActividad(RegistroHospital::class.java)
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TU_CODIGO_DE_EDICION_HOSPITAL && resultCode == Activity.RESULT_OK) {
            // Obtener la lista de hospitales actualizada
            val listaHospitales = data?.getSerializableExtra("listaHospitales") as? ArrayList<Hospital>
            listaHospitales?.let {
                // Actualizar el ListView con la lista de hospitales
                hospitales.clear()
                hospitales.addAll(it)
                adaptador?.notifyDataSetChanged()
            }
        }
    }


    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //Llenamos las opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_hospital,menu)
        //Obtner el id del ArrayListSeleccionado
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        idSeleccionado = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.mi_LecturaHospital ->{
                val hospitalSeleccionado = hospitales.getOrNull(idSeleccionado)
                hospitalSeleccionado?.let {
                    val intent = Intent(this, MainPaciente::class.java)
                    intent.putExtra("idHospital", hospitalSeleccionado.codigoHospital)
                    intent.putExtra("nombreHospital", hospitalSeleccionado.name)
                    startActivity(intent)
                    return true
                } ?: run {
                    Toast.makeText(this, "No se pudo obtener el hospital seleccionado", Toast.LENGTH_SHORT).show()
                    return false
                }
            } R.id.mi_EliminarHospital ->{
                // Mostrar un Toast indicando que se seleccionó la opción de eliminar
                Toast.makeText(this, "Seleccionaste Eliminar para el hospital en la posición: $idSeleccionado", Toast.LENGTH_SHORT).show()
                //Agregar un dialogo aqui

                abrirDialogoEliminar()
                return true
            }
            R.id.mi_editarHospital ->{
                // Mostrar un Toast indicando que se seleccionó la opción de leer
                Toast.makeText(this, "Seleccionaste Leer para el hospital en la posición: $idSeleccionado", Toast.LENGTH_SHORT).show()
                // Obtener el hospital seleccionado
                val hospitalSeleccionado = hospitales[idSeleccionado]

                // Mostrar un Toast indicando que se seleccionó la opción de editar
                Toast.makeText(
                    this,
                    "Seleccionaste Editar para el hospital: $hospitalSeleccionado",
                    Toast.LENGTH_SHORT
                ).show()

                // Llamar a la función para abrir la actividad de edición con la información del hospital
                abrirActividadEditarHospital(hospitalSeleccionado)
                return true
            }else -> super.onContextItemSelected(item)

        }
    }

    private fun abrirActividadEditarHospital(hospital: Hospital) {
        val intent = Intent(this, EditarHospital::class.java)
        intent.putExtra("idHospital", hospital.codigoHospital)
        intent.putExtra("nombreHospital", hospital.name)
        intent.putExtra("capacidad", hospital.capacityPatient)
        intent.putExtra("ubicacion", hospital.ubication)
        intent.putExtra("fechaFundacion", hospital.dateFoundation)
        intent.putExtra("EsPublico", hospital.isPublic)
        startActivityForResult(intent, TU_CODIGO_DE_EDICION_HOSPITAL)
    }

    //Para mostrar la lista de hospitales
    private fun showListViewHospitales() {
        Hospital().obtenerTodosLosHospitales { listaHospitales ->
            hospitales = listaHospitales

            if (adaptador == null) {
                adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    hospitales.map { it.toString() }
                )
                listView.adapter = adaptador
                registerForContextMenu(listView)
            } else {
                adaptador?.clear()
                adaptador?.addAll(hospitales.map { it.toString() })
            }
            adaptador?.notifyDataSetChanged()
        }
    }


    private fun abrirDialogoEliminar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Desea eliminar este Hospital?")

        builder.setPositiveButton("SI") { _, _ ->
            if (idSeleccionado >= 0 && idSeleccionado < hospitales.size) {
                val hospitalSeleccionado = hospitales[idSeleccionado]

               // Toast.makeText(this, "ID del hospital a eliminar: ${hospitalSeleccionado.codigoHospital}", Toast.LENGTH_SHORT).show()

                Log.d("MainActivity", "Intentando eliminar hospital con ID: ${hospitalSeleccionado.codigoHospital}")
                Hospital().deleteHospital(hospitalSeleccionado.codigoHospital ?: -1) { eliminado ->
                    if (eliminado) {
                        adaptador?.remove(hospitalSeleccionado.toString())
                        adaptador?.notifyDataSetChanged()
                        Toast.makeText(this, "REGISTRO ELIMINADO+++++++++++", Toast.LENGTH_LONG).show()
                        showListViewHospitales()
                    } else {
                        Toast.makeText(this, "REGISTRO ELIMINADO+++++++++++", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Selección no válida", Toast.LENGTH_LONG).show()
            }
        }
        builder.setNegativeButton("NO") { _, _ ->
            Toast.makeText(this, "Operación cancelada", Toast.LENGTH_LONG).show()
        }
        val dialogo = builder.create()
        dialogo.show()
    }

    fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        // Iniciar la actividad y esperar resultados usando el nuevo método
        iniciarRegistroActivityResult.launch(intent)

    }
}