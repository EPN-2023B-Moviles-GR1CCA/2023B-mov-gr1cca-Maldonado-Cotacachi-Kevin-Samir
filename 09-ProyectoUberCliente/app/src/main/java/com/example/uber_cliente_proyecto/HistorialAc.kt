package com.example.uber_cliente_proyecto

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uber_cliente_proyecto.Adapter.HistorialAdapter
import com.example.uber_cliente_proyecto.Modelos.Historial
import com.example.uber_cliente_proyecto.Providers.HistorialProvider
import com.example.uber_cliente_proyecto.databinding.ActivityHistorialBinding

class HistorialAc : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialBinding
    private var historyProvider = HistorialProvider()
    private var histories = ArrayList<Historial>()
    private lateinit var adapter: HistorialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewHistories.layoutManager = linearLayoutManager

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Historial de viajes"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setTitleTextColor(Color.WHITE)

        getHistories()
    }

    private fun getHistories() {
        histories.clear()

        historyProvider.getHistories().get().addOnSuccessListener { query ->

            if (query != null) {
                if (query.documents.size > 0) {
                    val documents = query.documents

                    for (d in documents) {
                        var history = d.toObject(Historial::class.java)
                        history?.id = d.id
                        histories.add(history!!)
                    }

                    adapter = HistorialAdapter(this@HistorialAc, histories)
                    binding.recyclerViewHistories.adapter = adapter
                }
            }

        }
    }
}