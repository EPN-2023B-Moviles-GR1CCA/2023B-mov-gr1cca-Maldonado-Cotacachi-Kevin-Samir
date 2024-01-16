package com.example.hospital

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



    private var adaptador: ArrayAdapter<String>? = null
    private lateinit var listView: ListView

    private val iniciarRegistroActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Actualizar el ListView con los datos del nuevo hospital
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

    var idSeleccionado = 0

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
            R.id.mi_editarHospital ->{
                val hospitalSeleccionado = adaptador?.getItem(idSeleccionado)
                // Mostrar un Toast indicando que se seleccionó la opción de editar
                Toast.makeText(this, "Seleccionaste Editar para el hospital: $hospitalSeleccionado", Toast.LENGTH_SHORT).show()

                //Me enviara a una nueva activdad para acutalizar los datos o a la misma actividad registro
                return true
            } R.id.mi_EliminarHospital ->{
                // Mostrar un Toast indicando que se seleccionó la opción de eliminar
                Toast.makeText(this, "Seleccionaste Eliminar para el hospital en la posición: $idSeleccionado", Toast.LENGTH_SHORT).show()
                //Agregar un dialogo aqui


                abrirDialogoEliminar()
                return true
            }
            R.id.mi_leerHospitalverPacientes ->{
                // Mostrar un Toast indicando que se seleccionó la opción de leer
                Toast.makeText(this, "Seleccionaste Leer para el hospital en la posición: $idSeleccionado", Toast.LENGTH_SHORT).show()

                //Me llevara a otra activddad
                return true
            }else -> super.onContextItemSelected(item)

        }
    }



    //Para mostrar la lista de hospitales
    private fun showListViewHospitales() {
        val hospital = Hospital(null,"", 0, "", "", null, this)
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

        }
        adaptador?.notifyDataSetChanged()

    }

    //Un dialogo para eliminar un registro de la lIsta
    fun abrirDialogoEliminar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Desea eliminar este estudiante?")

        builder.setPositiveButton("SI") { _, _ ->
            // Verificar que idSeleccionado esté dentro del rango válido
            if (idSeleccionado >= 0) {
                val hospitalSeleccionado = adaptador?.getItem(idSeleccionado)
                // Agregar un mensaje de registro para verificar el tamaño de la lista antes de la eliminación
                Log.d("EliminarRegistro", "Tamaño de la lista antes de la eliminación: ${adaptador?.count ?: 0}")

                // Imprimir mensajes de registro para depuración
                Log.d("EliminarRegistro", "ID seleccionado: $idSeleccionado")
                Log.d("EliminarRegistro", "Lista actual de hospitales: ${adaptador?.count ?: 0}")

                val padre = Hospital(null, "", null, "","", null,this)

                val resultado = padre.deleteHospital(idSeleccionado)

                    if (resultado > 0) {
                        Log.d("EliminarRegistro", "Registro eliminado de la Base de Datos")

                        // Elimina el elemento del adaptador
                        adaptador?.remove(hospitalSeleccionado?.toString())
                        // Notifica al adaptador que los datos han cambiado
                        adaptador?.notifyDataSetChanged()

                        Toast.makeText(this, "REGISTRO ELIMINADO+++++++++++", Toast.LENGTH_LONG).show()

                        runOnUiThread {

                            //Para actualizar la lista y ya no me aparezca en la vista listview

                            showListViewHospitales()
                            // Verificar si la lista se actualizó correctamente
                            val sizeBeforeUpdate = adaptador?.count ?: 0
                            val sizeAfterUpdate = adaptador?.count ?: 0
                            Log.d("ActualizarLista", "Tamaño de la lista antes de la actualización: $sizeBeforeUpdate, después: $sizeAfterUpdate")

                        }

                    } else {

                        Toast.makeText(this, "ERROR AL ELIMINAR REGISTRO*****", Toast.LENGTH_LONG).show()
                    }

            } else {
                // Manejar el caso en que idSeleccionado no es válido
                Toast.makeText(this, "Selección no válida", Toast.LENGTH_LONG).show()
            }
        }

        builder.setNegativeButton("NO") { _, _ ->
            // Manejar el caso en que el usuario selecciona "NO"
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