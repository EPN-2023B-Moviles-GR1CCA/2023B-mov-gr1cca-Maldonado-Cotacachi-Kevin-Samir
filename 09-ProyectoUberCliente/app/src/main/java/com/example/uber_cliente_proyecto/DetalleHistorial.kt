package com.example.uber_cliente_proyecto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.uber_cliente_proyecto.Modelos.Historial
import com.example.uber_cliente_proyecto.Providers.ConductorProvider
import com.example.uber_cliente_proyecto.Providers.HistorialProvider
import com.example.uber_cliente_proyecto.databinding.ActivityDetalleHistorialBinding

class DetalleHistorial : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleHistorialBinding
    private var historyProvider = HistorialProvider()
    private var driverProvider = ConductorProvider()
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
                val history = document.toObject(Historial::class.java)
                binding.textViewOrigin.text = history?.origen
                binding.textViewDestination.text = history?.destino
                //binding.textViewDate.text = RelativeTime.getTimeAgo(history?.timestamp!!, this@DetalleHistorial)
                binding.textViewPrice.text = "${String.format("%.1f", history?.precio)}$"
                binding.textViewMyCalification.text = "${history?.calificacionDriver}"
                binding.textViewClientCalification.text = "${history?.calificacionClient}"
                binding.textViewTimeAndDistance.text = "${history?.tiempo} Min - ${String.format("%.1f", history?.km)} Km"
                getDriverInfo(history?.idDriver!!)
            }

        }
    }

    private fun getDriverInfo(id: String) {
        /*driverProvider.getDriver(id).addOnSuccessListener { document ->
            if (document.exists()) {
                val driver = document.toObject(Driver::class.java)
                binding.textViewEmail.text = driver?.email
                binding.textViewName.text = "${driver?.name} ${driver?.lastname}"
                if (driver?.image != null) {
                    if (driver?.image != "") {
                        Glide.with(this).load(driver?.image).into(binding.circleImageProfile)
                    }
                }
            }
        }*/
    }
}