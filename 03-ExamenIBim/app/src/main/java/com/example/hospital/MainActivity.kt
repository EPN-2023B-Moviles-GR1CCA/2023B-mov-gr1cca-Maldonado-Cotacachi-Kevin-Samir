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

    /*
    private val actualizarHospitalActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {
                // Obtener el hospital actualizado del Intent
                // val hospitalActualizado: Hospital? = data?.getParcelableExtra("hospitalActualizado")

                Log.d("MainActivity", "actualizarHospitalActivityResult ejecutándose")

                // Actualizar la lista solo si se recibió el hospital actualizado
               // if (hospitalActualizado != null) {
                    // Puedes utilizar el hospital actualizado según sea necesario
                    // Aquí puedes hacer lo que necesites con el hospitalActualizado
                Log.d("MainActivity", "Lista de hospitales actualizada")
                showListViewHospitales()
               // }
            }
        }
     */

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

        Log.d("MainActivity", "onActivityResult - requestCode: $requestCode, resultCode: $resultCode")

        if (requestCode == TU_CODIGO_DE_EDICION_HOSPITAL && resultCode == RESULT_OK) {
            val idHospitalActualizado = data?.getIntExtra("idHospital", -1) ?: -1
            Log.d("MainPaciente", "onActivityResult - idHospitalActualizado: $idHospitalActualizado")

            // Actualizar la lista de hospitales
            if (data?.getBooleanExtra("actualizacionRealizada", false) == true) {
                showListViewHospitales()
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
               // val hospitalSeleccionado = adaptador?.getItem(idSeleccionado)
                // Mostrar un Toast indicando que se seleccionó la opción de editar
                val hospitalSeleccionado = hospitales[idSeleccionado]

                Toast.makeText(this, "Seleccionaste Editar para el hospital: $hospitalSeleccionado", Toast.LENGTH_SHORT).show()

                // Crear un Intent para abrir MainPaciente y pasar el ID y nombre del hospital
                val intent = Intent(this, MainPaciente::class.java)
                intent.putExtra("idHospital", hospitalSeleccionado.codigoHospital)
                intent.putExtra("nombreHospital", hospitalSeleccionado.name)
                Log.d("MainActivity", "ID del hospital seleccionado: ${hospitalSeleccionado.codigoHospital}, Nombre: ${hospitalSeleccionado.name}")

                startActivity(intent)

                return true
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

               // irActivida2(EditarHospital::class.java)


                return true
            }else -> super.onContextItemSelected(item)

        }
    }

    private fun abrirActividadEditarHospital(hospital: Hospital) {
        val intent = Intent(this, EditarHospital::class.java)
        // Puedes agregar aquí todos los extras necesarios para la edición del hospital
        intent.putExtra("idHospital", hospital.codigoHospital)
        intent.putExtra("nombreHospital", hospital.name)
        intent.putExtra("capacidad", hospital.capacityPatient)
        intent.putExtra("ubicacion", hospital.ubication)
        intent.putExtra("fechaFundacion", hospital.dateFoundation)
        intent.putExtra("EsPublico", hospital.isPublic)
        // Agrega otros extras según sea necesario

        startActivityForResult(intent, TU_CODIGO_DE_EDICION_HOSPITAL)
    }

    //Para mostrar la lista de hospitales
    private fun showListViewHospitales() {
        val hospital = Hospital(null,"", 0, "", "", null, this)
         hospitales = hospital.obtenerTodosLosHospitales()

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

    /*fun irActividadActualizar(clase: Class<*>) {
        val intent = Intent(this, clase)
        // Iniciar la actividad y esperar resultados usando el nuevo método
        actualizarHospitalActivityResult.launch(intent)
        //startActivity(intent)
    }*/

    fun irActivida2(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)

    }

}