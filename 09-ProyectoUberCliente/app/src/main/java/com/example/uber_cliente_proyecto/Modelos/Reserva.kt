package com.example.uber_cliente_proyecto.Modelos

import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()
class Reserva(
    val id: String? = null,
    val idClient: String? = null,
    val idDriver: String? = null,
    val origen: String? = null,
    val destino: String? = null,
    val estado: String? = null,
    val tiempo: Double? = null,
    val km: Double? = null,
    val origenLat: Double? = null,
    val origenLng: Double? = null,
    val destinoLat: Double? = null,
    val destinoLng: Double? = null,
    val precio: Double? = null,
) {

    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Reserva>(json)
    }
}