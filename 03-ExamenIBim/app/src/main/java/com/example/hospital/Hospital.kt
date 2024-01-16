package com.example.hospital

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
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
    val context: Context

) {
    // Método toString() para facilitar la visualización de datos
    override fun toString(): String {
       // return "Hospital( Id=$codigoHospital, Nombre='$name', Capacidad=$capacityPatient, Ubication='$ubication', Fundacion=$formattedDate, isPublic=$isPublic)"
        return "Codigo='$codigoHospital'\nNombre='$name'\nCapacidad=$capacityPatient\nUbicación='$ubication'\nFundación=$dateFoundation\n¿Es público?=$isPublic"

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
         //cursor = db.rawQuery("SELECT * FROM t_hospital", null)
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

     //Funcion Delete
     /*fun deleteHospital (id: Int): Int {
         val dbHelper: BaseDatos = BaseDatos(this.context)
         val db: SQLiteDatabase = dbHelper.writableDatabase

         return db.delete("t_hospital", "codigoHospital=?", arrayOf(id.toString()))
     }

      */
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





 }