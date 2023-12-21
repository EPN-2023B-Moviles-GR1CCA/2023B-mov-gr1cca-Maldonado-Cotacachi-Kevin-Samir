package com.example.a01appmovileskm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val botonCicloVida = findViewById<Button>(R.id.btn_cliclo_vida)
        botonCicloVida
            .setOnClickListener{
                irActividad(ACicloVida::class.java)
            }

        val botonListView = findViewById<Button>(R.id.btn_ir_list_view)

        botonListView.setOnClickListener {
            irActividad(BListView::class.java)
        }
    }

    fun irActividad(
        clase: Class<*>
    ){
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}



