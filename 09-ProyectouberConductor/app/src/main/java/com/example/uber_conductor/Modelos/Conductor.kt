package com.example.uber_conductor.Modelos

import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

class Conductor(
    val id: String? = null,
    val nombre: String ? = null,
    val apellido: String ? = null,
    val correo: String ? = null,
    val celular: String ? = null,
    var imagen: String ? = null,
    val numeroPlaca: String ? = null,
    val color: String ? = null,
    val marca: String ? = null,
) {


    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Cliente>(json)
    }
}