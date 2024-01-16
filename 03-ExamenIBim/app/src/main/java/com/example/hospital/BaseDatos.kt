package com.example.hospital

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Date


class BaseDatos(context: Context) : SQLiteOpenHelper(
    context, "Examen.db", null, 1

) {

    companion object {

       // val arrayHospital = arrayListOf<Hospital>()
        //val arrayPaciente = arrayListOf<Hospital>()
    }


    override fun onCreate(db: SQLiteDatabase?) {
        // Crear base de datos para la entidad Hospital
        val scriptSQLCrearTablaHospital =
            "CREATE TABLE t_hospital(" +
                    "codigoHospital INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "capacidad INTEGER NOT NULL," +
                    "ubicacion TEXT NOT NULL," +
                    "fechaFundacion TEXT NOT NULL," +
                    "esPublico INTEGER NOT NULL);"

        // Crear base de datos para la entidad Paciente
        val scriptSQLCrearTablaPaciente =
            "CREATE TABLE t_paciente(" +
                    "idPaciente INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "edad INTEGER NOT NULL," +
                    "genero TEXT NOT NULL," +
                    "fechaAdmision TEXT NOT NULL," +
                    "peso REAL NOT NULL," +
                    "tieneAlergias INTEGER NOT NULL);"

        db?.execSQL(scriptSQLCrearTablaHospital)
        db?.execSQL(scriptSQLCrearTablaPaciente)
    }

    //Listar

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Manejar actualizaciones de la base de datos si es necesario
    }
}