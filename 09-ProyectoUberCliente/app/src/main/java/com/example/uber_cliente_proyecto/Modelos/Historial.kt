package com.example.uber_cliente_proyecto.Modelos

import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

data class Historial (
    var id: String? = null,
    val idClient: String? = null,
    val idDriver: String? = null,
    val origen: String? = null,
    val destino: String? = null,
    val calificacionClient: Double? = null,
    val calificacionDriver: Double? = null,
    val tiempo: Int? = null,
    val km: Double? = null,
    val origenLat: Double? = null,
    val origenLng: Double? = null,
    val destinoLat: Double? = null,
    val destinoLng: Double? = null,
    val precio: Double? = null,
    val timestamp: Long? = null,
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Reserva>(json)
    }
}