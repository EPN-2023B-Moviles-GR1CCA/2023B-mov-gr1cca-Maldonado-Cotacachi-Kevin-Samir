package com.example.a01appmovileskm

class BEntrenador (

    //modelo
    var id: Int,
    var nombre: String?,
    var descripcion: String?
    ){

    override fun toString(): String {
        return "${nombre} - ${descripcion}"
    }
}