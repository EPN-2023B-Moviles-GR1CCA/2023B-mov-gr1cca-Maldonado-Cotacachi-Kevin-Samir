package com.example.hospital

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

 class Hospital (
    var codigoHospital: Int?,
    var name: String?,
    var capacityPatient: Int?,
    var ubication: String,
    var dateFoundation: String,
    var isPublic: Boolean?,
    val context: Context,

) : Serializable{
    // Método toString() para facilitar la visualización de datos
    override fun toString(): String {
       // return "Hospital( Id=$codigoHospital, Nombre='$name', Capacidad=$capacityPatient, Ubication='$ubication', Fundacion=$formattedDate, isPublic=$isPublic)"
        return "Codigo: $codigoHospital\nNombre: $name\nCapacidad: $capacityPatient\nUbicación: $ubication\nFundación: $dateFoundation\nEstado público?: $isPublic"

    }

     fun crearHospital(): Long {
        val dbHelper: BaseDatos = BaseDatos(this.context)
        val db: SQLiteDatabase = dbHelper.writableDatabase

        val valoresAGuardar : ContentValues= ContentValues()

        valoresAGuardar.put("codigoHospital", codigoHospital)  // Nuevo campo
        valoresAGuardar.put("nombre",name )
        valoresAGuardar.put("capacidad", capacityPatient)
        valoresAGuardar.put("ubicacion", ubication)
        valoresAGuardar.put("fechaFundacion", dateFoundation)
        valoresAGuardar.put("esPublico", isPublic)

        return try {
            db.insert("t_hospital", null, valoresAGuardar)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("BaseDatos", "Error al insertar en la base de datos: ${e.message}")
            -1  // Retornar un valor que indique error
        } finally {
            // Asegurarse de cerrar la conexión a la base de datos
            db.close()
        }


    }


     // Función para obtener todos los hospitales
     fun obtenerTodosLosHospitales(): ArrayList<Hospital> {
         val dbHelper: BaseDatos = BaseDatos(this.context)
         val db: SQLiteDatabase = dbHelper.readableDatabase

         val lista = ArrayList<Hospital>()
         var hospital: Hospital
         //var cursor: Cursor? = null
         var cursor: Cursor? = db.rawQuery("SELECT * FROM t_hospital", null)

        // Agrega este código para imprimir los nombres de las columnas
         if (cursor != null) {
             val columnNames = cursor.columnNames
             Log.d("ColumnNames", "Column names: ${columnNames.joinToString()}")
         }
         if (cursor != null && cursor.moveToFirst()) {
             do {

                 hospital = Hospital(null,"", 0, "", "", null, context)


                 hospital.codigoHospital = cursor.getString(0).toInt()
                 hospital.name = cursor.getString(1)
                 hospital.capacityPatient = cursor.getString(2).toInt()
                 hospital.ubication = cursor.getString(3)
                 hospital.dateFoundation = cursor.getString(4)
                 hospital.isPublic = cursor.getString(5) == "1"

                 lista.add(hospital)

             } while (cursor.moveToNext())
         }

         cursor?.close()
         return lista
     }


     fun deleteHospital(id: Int): Int {
         val dbHelper: BaseDatos = BaseDatos(this.context)
         val db: SQLiteDatabase = dbHelper.writableDatabase

         try {
             // Obtener el hospital a partir de su posición en la lista
             val hospital = obtenerTodosLosHospitales()[id]
             val codigoHospital = hospital.codigoHospital

             val whereClause = "codigoHospital = ?"
             val whereArgs = arrayOf(codigoHospital.toString())
             // Devuelve la cantidad de filas afectadas (debería ser 1 si se elimina correctamente)
             val rowsDeleted = db.delete("t_hospital", whereClause, whereArgs)

             return if (rowsDeleted > 0) {
                 Log.d("EliminarRegistro", "Registro eliminado de la Base de Datos con ID: $codigoHospital")
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

     fun updateHospital(
         nuevoNombre: String,
         nuevaCapacidad: Int,
         nuevaUbicacion: String,
         nuevaFechaFundacion: String,
         esPublico: Boolean
     ): Int {
         val dbHelper: BaseDatos = BaseDatos(this.context)
         val db: SQLiteDatabase = dbHelper.writableDatabase

         try {
             // Crear un objeto ContentValues para almacenar los nuevos valores
             val values = ContentValues()
             values.put("nombre", nuevoNombre)
             values.put("capacidad", nuevaCapacidad)
             values.put("ubicacion", nuevaUbicacion)
             values.put("fechaFundacion", nuevaFechaFundacion)
             values.put("esPublico", esPublico)

             // Actualizar el registro en la base de datos
             val whereClause = "codigoHospital = ?"
             val whereArgs = arrayOf(codigoHospital.toString())
             val rowsAffected = db.update("t_hospital", values, whereClause, whereArgs)

             if (rowsAffected > 0) {
                 Log.d("ActualizarRegistro", "Registro actualizado en la Base de Datos con ID: $codigoHospital")
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




     // Obtener un hospital por su ID
     fun getHospitalById(id: Int): Hospital {
         val dbHelper: BaseDatos = BaseDatos(this.context)
         val db: SQLiteDatabase = dbHelper.writableDatabase

         // Crear un objeto Hospital para almacenar la información
        // var hospital = Hospital(null, "Raquel", 12, "Mas lejos de Guamani", "11-10-1987", false, this.context)
         var hospital = Hospital(null, "", 0, "", "", null, this.context)
         var cursor: Cursor? = null


         try {
             // Utilizar consulta parametrizada para prevenir inyección SQL
             val query = "SELECT * FROM t_hospital WHERE codigoHospital = ?"
             cursor = db.rawQuery(query, arrayOf(id.toString()))

             // Verificar si se encontraron resultados y leer los datos
             if (cursor.moveToFirst()) {
                 hospital = Hospital(
                     cursor.getString(0).toInt(),
                     cursor.getString(1),
                     cursor.getString(2).toInt(),
                     cursor.getString(3),
                     cursor.getString(4),
                     cursor.getString(5) == "1",
                     this.context
                 )
             }
         } finally {
             // Cerrar el cursor en un bloque finally
             cursor?.close()
         }
         return hospital
     }



     // Función Update en la clase Hospital
     fun actualizarHospital(): Int {
         val dbHelper: BaseDatos = BaseDatos(this.context)
         val db: SQLiteDatabase = dbHelper.writableDatabase
         val values: ContentValues = ContentValues()

         values.put("nombre", this.name)
         values.put("capacidad", this.capacityPatient)
         values.put("ubicacion", this.ubication)
         values.put("fechaFundacion", this.dateFoundation)
         values.put("esPublico", this.isPublic)

         return db.update("t_hospital", values, "codigoHospital="+this.codigoHospital, null)
     }




 }