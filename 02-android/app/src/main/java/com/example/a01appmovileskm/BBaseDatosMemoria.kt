package com.example.a01appmovileskm

class BBaseDatosMemoria {
    //COMPAIN OBJECT

    companion object{
        val arregloBEntrenador = arrayListOf<BEntrenador>()

        init {
            arregloBEntrenador
                .add(
                    BEntrenador(1, "Kevin", "a@a,com")
                )
            arregloBEntrenador
                .add(
                    BEntrenador(2,"Raquel", "b@b.com")
                )
            arregloBEntrenador
                .add(
                    BEntrenador(2,"Esther", "c@c.com")
                )
        }


    }

}