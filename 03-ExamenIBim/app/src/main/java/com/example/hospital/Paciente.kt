package com.example.hospital

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
class Paciente (

    var idPacient: Int?,
    var name: String?,
    var age: Int?,
    var dateAdmission: String,
    var weight: Int,
    var alergias: Boolean?,
    var codHospital : Int,
    val context: Context,
) : Serializable {


    //CREAR

    fun crearPaciente(): Long {
        val dbHelper: BaseDatos = BaseDatos(this.context)
        val db: SQLiteDatabase = dbHelper.writableDatabase

        val valoresAGuardar: ContentValues = ContentValues()

        // No incluimos el idPaciente ya que es incremental y se asigna automáticamente
        valoresAGuardar.put("nombre", name)
        valoresAGuardar.put("edad", age)
        valoresAGuardar.put("fechaAdmision", dateAdmission)
        valoresAGuardar.put("peso", weight)
        valoresAGuardar.put("tieneAlergias", alergias)
        valoresAGuardar.put("codHospital", codHospital)

        return try {
            Log.d("BaseDatos", "Antes de insertar en la base de datos.")
            val resultado =  db.insert("t_paciente", null, valoresAGuardar)
            Log.d("BaseDatos", "Después de insertar en la base de datos. ID del paciente: $resultado")
            resultado
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("BaseDatos", "Error al insertar en la base de datos: ${e.message}")
            Log.e("BaseDatos", "Error al insertar en la base de datos: ${e}")

            -1  // Retornar un valor que indique error
        } finally {
            // Asegurarse de cerrar la conexión a la base de datos
            db.close()
        }
    }

    // Función para obtener todos los pacientes
    fun obtenerTodosLosPacientes(codHospital: Int): ArrayList<Paciente> {
        val dbHelper: BaseDatos = BaseDatos(this.context)
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val lista = ArrayList<Paciente>()
        var paciente: Paciente
        try {
            Log.d("MainPaciente", "Valor de idHospital: $codHospital")

            val cursor: Cursor? = db.rawQuery("SELECT * FROM t_paciente WHERE codHospital = ?", arrayOf(codHospital.toString()))

            // Agrega este código para imprimir los nombres de las columnas
            if (cursor != null) {
                val columnNames = cursor.columnNames
                Log.d("ColumnNames", "Column names: ${columnNames.joinToString()}")
            }

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    paciente = Paciente(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getInt(5) == 1,
                        cursor.getInt(6),
                        this.context
                    )

                    lista.add(paciente)

                } while (cursor.moveToNext())
            }

            cursor?.close()
        } catch (e: Exception) {
            // Registra cualquier excepción que ocurra
            e.printStackTrace()
            Log.e("MainPaciente", "Error al obtener pacientes: ${e.message}")
        } finally {
            // Cierra la base de datos al finalizar
            db.close()
        }
        return lista
    }

    fun updatePaciente(
        idPaciente: Int,
        nuevoNombre: String,
        nuevaEdad: Int,
        nuevaFechaAdmision: String,
        nuevoPeso: Int,
        tieneAlergias: Boolean,
        nuevoCodHospital: Int
    ): Int {
        val dbHelper: BaseDatos = BaseDatos(this.context)
        val db: SQLiteDatabase = dbHelper.writableDatabase

        try {
            // Crear un objeto ContentValues para almacenar los nuevos valores
            val values = ContentValues()
            values.put("nombre", nuevoNombre)
            values.put("edad", nuevaEdad)
            values.put("fechaAdmision", nuevaFechaAdmision)
            values.put("peso", nuevoPeso)
            values.put("tieneAlergias", tieneAlergias)
            values.put("codHospital", nuevoCodHospital)

            // Actualizar el registro en la base de datos
            val rowsAffected = db.update("t_paciente", values, "idPaciente = ?", arrayOf(idPaciente.toString()))

            if (rowsAffected > 0) {
                Log.d("ActualizarRegistro", "Registro actualizado en la Base de Datos con ID: $idPaciente")
            } else {
                Log.e("ActualizarRegistro", "Error al actualizar el registro en la Base de Datos")
            }

            return rowsAffected

        } catch (e: Exception) {
            Log.e("ActualizarRegistro", "Error al actualizar el registro en la Base de Datos", e)
            return -1
        } finally {
            // Asegúrate de cerrar la base de datos después de usarla
            db.close()
        }
    }


    fun deletePaciente(idPaciente: Int, idHospital: Int): Int {
        val dbHelper: BaseDatos = BaseDatos(this.context)
        val db: SQLiteDatabase = dbHelper.writableDatabase

        try {
            // Obtener el paciente a partir de su posición en la lista
            val paciente = obtenerTodosLosPacientes(idHospital)[idPaciente]
            val idPaciente = paciente.idPacient

            val whereClause = "idPaciente = ? AND codHospital = ?"
            val whereArgs = arrayOf(idPaciente.toString(), idHospital.toString())

            //val whereClause = "idPaciente = ?"
            //val whereArgs = arrayOf(idPaciente.toString())

            // Devuelve la cantidad de filas afectadas (debería ser 1 si se elimina correctamente)
            val rowsDeleted = db.delete("t_paciente", whereClause, whereArgs)

            return if (rowsDeleted > 0) {
                Log.d("EliminarRegistro", "Registro eliminado de la Base de Datos con ID: $idPaciente")
                rowsDeleted
            } else {
                Log.e("EliminarRegistro", "Error al eliminar el registro de la Base de Datos")
                -1
            }

        } catch (e: Exception) {
            Log.e("EliminarRegistro", "Error al eliminar el registro de la Base de Datos", e)
            return -1
        } finally {
            // Asegúrate de cerrar la base de datos después de usarla
            db.close()
        }
    }


    // Método toString() para facilitar la visualización de datos
    override fun toString(): String {
        val salida=
            "ID: ${idPacient}\n" +
                    "Nombre: ${name}\n" +
                    "Edad: ${age}\n " +
                    "Fecha de ingreso: ${dateAdmission}\n" +
                    "Peso: ${weight}\n " +
                    "Alergias: ${alergias} \n"+
                    "Codigo Hospital: ${codHospital}"

        return salida
    }
}

