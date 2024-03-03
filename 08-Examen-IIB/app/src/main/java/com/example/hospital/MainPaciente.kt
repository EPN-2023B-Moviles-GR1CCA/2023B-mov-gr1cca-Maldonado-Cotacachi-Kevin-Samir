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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog

class MainPaciente : AppCompatActivity() {

    companion object{
        var idSeleccionado = 0
    }

    private var adaptador: ArrayAdapter<String>? = null
    private lateinit var listView: ListView
    private var pacientes: MutableList<Paciente> = mutableListOf()
    private var idHospital: Int = -1
    // Definir una constante para el código de edición
    private val TU_CODIGO_DE_EDICION = 1 // Puedes elegir cualquier número que desees
    private val iniciarRegistroPacienteActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Actualizar el ListView con los datos del nuevo paciente
                Log.d("MainPaciente", "Cantidad de pacientes obtenidos: ${pacientes.size}")
                showListViewPacientes()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_paciente)

        listView = findViewById(R.id.lv_listaPatient)
        registerForContextMenu(listView)
        // Obtener el ID y nombre del hospital del Intent
        idHospital = intent.getIntExtra("idHospital", 1)

        val nombreHospital = intent.getStringExtra("nombreHospital") ?: "Nombre no encontrado"

        // Mostrar la información del hospital en el TextView
        val txtHospitalPadre = findViewById<TextView>(R.id.txt_HospitalPadre)
        txtHospitalPadre.text = "CODIGO DE HOSPITAL: ${idHospital} "+"\nNOMBRE: ${nombreHospital}"
        // Llamamos a showListViewPacientes() para mostrar la lista al iniciar la actividad
        showListViewPacientes()

        val btnVolverMain = findViewById<Button>(R.id.btn_VolverHospital)
        btnVolverMain
            .setOnClickListener {
                irActividad(MainActivity::class.java)
            }
        // Botón para ir a la actividad RegistroPaciente
        val btnCrearPaciente = findViewById<Button>(R.id.btn_RegistrarPaciente)
        btnCrearPaciente.setOnClickListener {
            irActividadRegistroPaciente(idHospital, nombreHospital)
        }
    }

    //agregue esto
    // Dentro de MainPaciente.kt
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TU_CODIGO_DE_EDICION && resultCode == RESULT_OK) {
            val idHospitalActualizado = data?.getIntExtra("idHospital", -1) ?: -1
            Log.d("MainPaciente", "onActivityResult - idHospitalActualizado: $idHospitalActualizado")
            // Verificar si se realizó una actualización exitosa
            if (data?.getBooleanExtra("actualizacionRealizada", false) == true) {
                // Actualizar la lista de pacientes después de la actualización
                showListViewPacientes()
            }
        }
    }


    // Función para abrir la actividad de actualización de paciente
    private fun abrirActividadActualizarPaciente(paciente: Paciente) {
        val intent = Intent(this, EditarPaciente::class.java)
        // Puedes pasar la información del paciente a la actividad de actualización usando extras en el Intent
        intent.putExtra("idPaciente", paciente.idPacient)
        intent.putExtra("nombrePaciente", paciente.name)
        intent.putExtra("edadPaciente", paciente.age)
        intent.putExtra("fechaAdmisionPaciente", paciente.dateAdmission)
        intent.putExtra("pesoPaciente", paciente.weight)
        intent.putExtra("alergiasPaciente", paciente.alergias)
        intent.putExtra("codHospitalPaciente", paciente.codHospital)
        startActivityForResult(intent, TU_CODIGO_DE_EDICION)

    }
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        // Llenamos las opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_paciente, menu)
        // Obtener el id del ArrayListSeleccionado
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        idSeleccionado = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editarPaciente -> {
                // Obtener el paciente seleccionado
                val pacienteSeleccionado = pacientes[idSeleccionado]
                // Mostrar un Toast indicando que se seleccionó la opción de editar
                Toast.makeText(
                    this,
                    "Seleccionaste Editar para el paciente: $pacienteSeleccionado",
                    Toast.LENGTH_SHORT
                ).show()
                // Llamar a la función para abrir la actividad de actualización con la información del paciente
                abrirActividadActualizarPaciente(pacienteSeleccionado)
                return true
            }
            R.id.mi_eliminarPaciente -> {
                // Mostrar un Toast indicando que se seleccionó la opción de eliminar
                Toast.makeText(
                    this,
                    "Seleccionaste Eliminar para el paciente en la posición: $idSeleccionado",
                    Toast.LENGTH_SHORT
                ).show()
                // Llamar a la función para abrir el diálogo de elimiación
                abrirDialogoEliminar(idSeleccionado, pacientes[idSeleccionado])
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // En tu MainActivity o MainPaciente, agregar una función genérica para eliminar registros
    fun abrirDialogoEliminar(idSeleccionado: Int, registro: Any) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Desea eliminar este Paciente?")
        builder.setPositiveButton("SI") { _, _ ->
            // Verificar que idSeleccionado esté dentro del rango válido
            if (idSeleccionado >= 0) {
                when (registro) {
                    is Paciente -> {
                        registro.deletePaciente(idSeleccionado.toString()) { eliminado ->
                            if (eliminado) {
                                // Eliminar el elemento del adaptador
                                adaptador?.remove(registro.toString())
                                // Notificar al adaptador que los datos han cambiado
                                adaptador?.notifyDataSetChanged()
                                // Actualizar la lista
                                showListViewPacientes()
                                Toast.makeText(this, "Paciente eliminado correctamente", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Error al eliminar paciente", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else -> {
                        Toast.makeText(this, "Tipo de registro no válido", Toast.LENGTH_SHORT).show()
                    }
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



    private fun showListViewPacientes() {
        Log.d("MainPaciente", "showListViewPacientes() llamado")
        val paciente = Paciente(null, "", 0, "", 0, null, idHospital)
        paciente.obtenerTodosLosPacientes(idHospital) { listaPacientes ->
            if (listaPacientes.isNotEmpty()) {
                Log.d("MainPaciente", "Consulta a la base de datos exitosa. Número de pacientes: ${listaPacientes.size}")
                // No necesitas imprimir cada paciente en el log, ya que el número de pacientes ya proporciona suficiente información
            } else {
                Log.d("MainPaciente", "La consulta a la base de datos no retornó resultados.")
            }
            if (adaptador == null) {
                adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    listaPacientes.map { it.toString() }
                )
                listView.adapter = adaptador
                registerForContextMenu(listView)
                if (listView.adapter == null) {
                    Log.d("MainPaciente", "¡El adaptador es nulo!")
                }
            } else {
                adaptador?.clear()
                adaptador?.addAll(listaPacientes.map { it.toString() })
            }
            adaptador?.notifyDataSetChanged()
        }
    }


    fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)

    }
    private fun irActividadRegistroPaciente(idHospital: Int, nombreHospital: String) {
       // Log.d("MainPaciente", "Creando Intent para leer hospital. ID: ${hospitalSeleccionado.codigoHospital}, Nombre: ${hospitalSeleccionado.name}")
        val intent = Intent(this, RegistroPaciente::class.java)
        intent.putExtra("idHospital", idHospital)
        intent.putExtra("nombreHospital", nombreHospital)
        // Iniciar la actividad y esperar resultados usando el nuevo método
        iniciarRegistroPacienteActivityResult.launch(intent)
    }
}