package com.example.hospital

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Date


class BaseDatos(context: Context) : SQLiteOpenHelper(
    context, "Examen.db", null, 3

) {

    companion object {
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
                    "fechaAdmision TEXT NOT NULL," +
                    "peso INTEGER NOT NULL," +
                    "tieneAlergias INTEGER NOT NULL," +
                    "codHospital INTEGER NOT NULL," +  // Agregar la columna para la clave foránea
                    "FOREIGN KEY (codHospital) REFERENCES t_hospital(codigoHospital));"

        db?.execSQL(scriptSQLCrearTablaHospital)
        db?.execSQL(scriptSQLCrearTablaPaciente)
    }

    //Listar

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Manejar actualizaciones de la base de datos si es necesario

        db?.execSQL("DROP TABLE IF EXISTS t_paciente;")
        db?.execSQL("DROP TABLE IF EXISTS t_hospital;")

        onCreate(db)

    }
}