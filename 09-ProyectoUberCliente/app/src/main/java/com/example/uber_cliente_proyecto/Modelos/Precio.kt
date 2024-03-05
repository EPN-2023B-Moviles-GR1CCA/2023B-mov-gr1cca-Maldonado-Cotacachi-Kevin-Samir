package com.example.uber_cliente_proyecto.Modelos

import com.beust.klaxon.Klaxon


private val klaxon = Klaxon()
class Precio(
    val km: Double? = null,
    val min: Double? = null,
    val minValor: Double? = null,
    val diferencia: Double? = null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Precio>(json)
    }
}