package com.example.uber_conductor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.example.uber_conductor.Modelos.Cliente
import com.example.uber_conductor.Modelos.HistorialViaje
import com.example.uber_conductor.Providers.ClienteProvider
import com.example.uber_conductor.Providers.HistorialProvider
import com.example.uber_conductor.databinding.ActivityDetalleHistorialBinding
import com.example.uber_conductor.utils.RelativeTime

class DetalleHistorial : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleHistorialBinding
    private var historyProvider = HistorialProvider()
    private var clientProvider = ClienteProvider()
    private var extraId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleHistorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        extraId = intent.getStringExtra("id")!!
        getHistory()

        binding.imageViewBack.setOnClickListener { finish() }
    }

    private fun getHistory() {
        historyProvider.getHistoryById(extraId).addOnSuccessListener { document ->

            if (document.exists()) {
                val history = document.toObject(HistorialViaje::class.java)
                binding.textViewOrigin.text = history?.origin
                binding.textViewDestination.text = history?.destination
                binding.textViewDate.text = RelativeTime.getTimeAgo(history?.timestamp!!, this@DetalleHistorial)
                binding.textViewPrice.text = "${String.format("%.1f", history?.price)}$"
                binding.textViewMyCalification.text = "${history?.calificationToDriver}"
                binding.textViewClientCalification.text = "${history?.calificationToClient}"
                binding.textViewTimeAndDistance.text = "${history?.time} Min - ${String.format("%.1f", history?.km)} Km"
                getClientInfo(history?.idClient!!)
            }

        }
    }

    private fun getClientInfo(id: String) {
        clientProvider.getClientById(id).addOnSuccessListener { document ->
            if (document.exists()) {
                val client = document.toObject(Cliente::class.java)
                binding.textViewEmail.text = client?.correo
                binding.textViewName.text = "${client?.nombre} ${client?.apellido}"
                if (client?.imagen != null) {
                    if (client?.imagen != "") {
                        Glide.with(this).load(client?.imagen).into(binding.circleImageProfile)
                    }
                }
            }
        }
    }
}