package com.example.uber_conductor

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uber_conductor.Adapter.HistoriesAdapter
import com.example.uber_conductor.Modelos.HistorialViaje
import com.example.uber_conductor.Providers.HistorialProvider
import com.example.uber_conductor.databinding.ActivityHistorialBinding

class Historial : AppCompatActivity() {
    private lateinit var binding: ActivityHistorialBinding
    private var historyProvider = HistorialProvider()
    private var histories = ArrayList<HistorialViaje>()
    private lateinit var adapter: HistoriesAdapter

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
                        var history = d.toObject(HistorialViaje::class.java)
                        history?.id = d.id
                        histories.add(history!!)
                    }

                    adapter = HistoriesAdapter(this@Historial, histories)
                    binding.recyclerViewHistories.adapter = adapter
                }
            }

        }
    }
}