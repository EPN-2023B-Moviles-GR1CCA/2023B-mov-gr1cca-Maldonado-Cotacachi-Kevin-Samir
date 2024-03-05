package com.example.uber_conductor.Modelos

import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

class Reserva (
    val id: String? = null,
    val idClient: String? = null,
    val idDriver: String? = null,
    val origin: String? = null,
    val destination: String? = null,
    val status: String? = null,
    val time: Double? = null,
    val km: Double? = null,
    val originLat: Double? = null,
    val originLng: Double? = null,
    val destinationLat: Double? = null,
    val destinationLng: Double? = null,
    val price: Double? = null,
){
        public fun toJson() = klaxon.toJsonString(this)

        companion object {
            public fun fromJson(json: String) = klaxon.parse<Reserva>(json)
        }
    }
