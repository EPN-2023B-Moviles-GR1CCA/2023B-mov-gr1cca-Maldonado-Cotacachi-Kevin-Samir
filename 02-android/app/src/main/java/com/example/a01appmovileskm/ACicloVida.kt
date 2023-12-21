package com.example.a01appmovileskm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar

class ACicloVida : AppCompatActivity() {


    var textoGlobal = ""

    fun mostrarSnackbar (texto:String){
        textoGlobal = textoGlobal +" "+ texto
        Snackbar
            .make(
                findViewById(R.id.cl_ciclo_vida), //view
                textoGlobal, // texto
                Snackbar.LENGTH_INDEFINITE //Tiempo
            )
            .show()

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aciclo_vida)
        mostrarSnackbar("OnCreate")
    }

    override fun onStart(){
        super.onStart()
        mostrarSnackbar("OnStart")
    }

    override fun onResume(){
        super.onResume()
        mostrarSnackbar("onResume")
    }

    override fun onRestart() {
        super.onRestart()
        mostrarSnackbar("onRestart")
    }

    override fun onPause() {
        super.onPause()
        mostrarSnackbar("onPause")
    }

    override fun onStop() {
        super.onStop()
        mostrarSnackbar("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        mostrarSnackbar("onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {

            //Guardar las variables
            //Primitivo
            putString("textoGuardado", textoGlobal)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //Recuperar las variables

        //Primitivo
        val textoRecuperado:String? = savedInstanceState
            .getString("textoGuardado")

        // val textoRecuperdo: Int? = savedInstanceState.getInt("xx")
        if (textoRecuperado!= null){
            mostrarSnackbar(textoRecuperado)
            textoGlobal = textoRecuperado
        }



    }
}