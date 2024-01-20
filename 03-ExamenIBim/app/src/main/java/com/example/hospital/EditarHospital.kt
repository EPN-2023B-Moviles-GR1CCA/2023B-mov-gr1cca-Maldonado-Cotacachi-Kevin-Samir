package com.example.hospital

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EditarHospital : AppCompatActivity() {

   // private var pacienteActualizado: Hospital? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_hospital)


        // Intenta obtener el hospital seleccionado desde la base de datos
        val codigoHospital = MainActivity.idSeleccionado
       // var hospital = Hospital(null, "", 0, "", "", null, this)
        Log.d("LeerPaciente", "ID del hospital seleccionado: $codigoHospital")
        var hospital = Hospital(null, "", 1, "", "", null, this).getHospitalById(codigoHospital)

        // Asumiendo que estos son los IDs de tus EditText en activity_leer_paciente.xml
        var nombreHospital  = findViewById<EditText>(R.id.input_nameUpdate)
        // Rellenar los EditText con la información actual del paciente
        Log.d("LeerPaciente", "Nombre del hospital antes de obtener datos: ${hospital.name}")
        nombreHospital.setText(hospital.name.toString())

        var capacidadHospital   = findViewById<EditText>(R.id.input_capacidadUpdate)
        capacidadHospital.setText(hospital.capacityPatient.toString())

        var ubicacionHospital   = findViewById<EditText>(R.id.input_ubicUpdate)
        ubicacionHospital.setText(hospital.ubication.toString())

        var fechaFundacionHospital  = findViewById<EditText>(R.id.input_fundacionUpdate)
        fechaFundacionHospital.setText(hospital.dateFoundation.toString())

        var inputEstadoHospital = findViewById<EditText>(R.id.input_estadoUpdate)
        //Esta correcto si lo hago asi sabiendo que en mi modelo isPublic es booleano?
        inputEstadoHospital.setText(hospital.isPublic.toString())


        val botonActualizarYregresarMain = findViewById<Button>(R.id.btn_regresarMain)

        botonActualizarYregresarMain.setOnClickListener {
            // Obtener los nuevos datos ingresados por el usuario
            val nuevoNombreH = nombreHospital.text.toString()
            Log.d("LeerPaciente", "Nuevo nombre del hospital: $nuevoNombreH")

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

            // Llamar a la función updateHospital para aplicar los cambios
            val resultado = hospital.actualizarHospital()

            // Después de la actualización y obtener los nuevos datos
            if (resultado > 0) {
                Toast.makeText(this, "REGISTRO ACTUALIZADO", Toast.LENGTH_LONG).show()

                // Limpiar los campos
                // Aquí puedes implementar tu función cleanEditText si la tienes

                // Llamar a la función showListViewHospitales en MainActivity para actualizar la lista
                /*val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

                 */
            } else {
                Toast.makeText(this, "ERROR AL ACTUALIZAR REGISTRO", Toast.LENGTH_LONG).show()
            }

            //OJO CUANDO PONGO AQUI SI FUNCIONA EL REGRESO DE PANTALLA AL MAIN.
            // Crear un Intent para regresar a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // Cerrar la actividad actual
            finish()


        }
    }
}