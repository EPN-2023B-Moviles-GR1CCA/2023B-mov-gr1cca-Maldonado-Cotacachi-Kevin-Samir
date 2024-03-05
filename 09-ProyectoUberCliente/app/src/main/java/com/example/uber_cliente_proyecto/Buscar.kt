package com.example.uber_cliente_proyecto


import com.google.firebase.firestore.DocumentSnapshot
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.WindowManager
import android.widget.Toast

import com.example.uber_cliente_proyecto.Modelos.Conductor
import com.example.uber_cliente_proyecto.Modelos.FCMBody
import com.example.uber_cliente_proyecto.Modelos.FCMResponse
import com.example.uber_cliente_proyecto.Modelos.Reserva
import com.example.uber_cliente_proyecto.Providers.AuthProvider
import com.example.uber_cliente_proyecto.Providers.BookingProvider
import com.example.uber_cliente_proyecto.Providers.ConductorProvider
import com.example.uber_cliente_proyecto.Providers.GeoProvider
import com.example.uber_cliente_proyecto.Providers.NotificacionProvider
import com.example.uber_cliente_proyecto.databinding.ActivityBuscarBinding
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ListenerRegistration
import org.imperiumlabs.geofirestore.callbacks.GeoQueryEventListener

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar : AppCompatActivity() {
    private var listenerBooking: ListenerRegistration? = null
    private lateinit var binding: ActivityBuscarBinding
    private var extraOriginName = ""
    private var extraDestinationName = ""
    private var extraOriginLat = 0.0
    private var extraOriginLng = 0.0
    private var extraDestinationLat = 0.0
    private var extraDestinationLng = 0.0
    private var extraTime = 0.0
    private var extraDistance = 0.0
    private var originLatLng: LatLng? = null
    private var destinationLatLng: LatLng? = null

    private val geoProvider = GeoProvider()
    private val authProvider = AuthProvider()
    private val bookingProvider = BookingProvider()
    private val notificationProvider = NotificacionProvider()
    private val driverProvider = ConductorProvider()


    // BUSQUEDA DEL CONDUCTOR
    private var radius = 0.2
    private var idDriver = ""
    private var driver: Conductor? = null
    private var isDriverFound = false
    private var driverLatLng: LatLng? = null
    private var limitRadius = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // EXTRAS
        extraOriginName = intent.getStringExtra("origin")!!
        extraDestinationName = intent.getStringExtra("destination")!!
        extraOriginLat = intent.getDoubleExtra("origin_lat", 0.0)
        extraOriginLng = intent.getDoubleExtra("origin_lng", 0.0)
        extraDestinationLat = intent.getDoubleExtra("destination_lat", 0.0)
        extraDestinationLng = intent.getDoubleExtra("destination_lng", 0.0)
        extraTime = intent.getDoubleExtra("time", 0.0)
        extraDistance = intent.getDoubleExtra("distance", 0.0)
        originLatLng = LatLng(extraOriginLat, extraOriginLng)
        destinationLatLng = LatLng(extraDestinationLat, extraDestinationLng)

        getClosestDriver()
        checkIfDriverAccept()
    }


    private fun sendNotification() {
        val map = HashMap<String, String>()
        map["title"] = "SOLICITUD DE VIAJE"
        map["body"] = "Un cliente está solicitando un viaje a ${String.format("%.1f", extraDistance)}km y ${String.format("%.1f", extraTime)}Min"
        map["idBooking"] = authProvider.getId()

        if (driver != null) {
            val body = FCMBody(
                to = driver!!.token!!, // Accede al token solo si driver no es nulo
                priority = "high",
                ttl = "4500s",
                data = map
            )


            notificationProvider.sendNotification(body).enqueue(object : Callback<FCMResponse> {
                override fun onResponse(call: Call<FCMResponse>, response: Response<FCMResponse>) {
                    if (response.isSuccessful && response.body() != null && response.body()!!.success == 1) {
                        Toast.makeText(this@Buscar, "Se envió la notificación", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@Buscar, "No se pudo enviar la notificación", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<FCMResponse>, t: Throwable) {
                    Log.e("NOTIFICATION", "Error al enviar notificación: ${t.message}")
                    Toast.makeText(this@Buscar, "Hubo un error enviando la notificación", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Log.e("NOTIFICATION", "Driver es nulo")
            Toast.makeText(this@Buscar, "No se pudo enviar la notificación, conductor nulo", Toast.LENGTH_LONG).show()
        }
    }



    private fun checkIfDriverAccept() {
        listenerBooking = bookingProvider.getBooking().addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d("FIRESTORE", "ERROR: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val booking = snapshot.toObject(Reserva::class.java)

                if (booking?.estado == "accept") {
                    Toast.makeText(this@Buscar, "Viaje aceptado", Toast.LENGTH_SHORT).show()
                    listenerBooking?.remove()
                    goToMapTrip()
                }
                else if (booking?.estado == "cancel") {
                    Toast.makeText(this@Buscar, "Viaje cancelado", Toast.LENGTH_SHORT).show()
                    listenerBooking?.remove()
                    goToMap()
                }

            }
        }
    }

    private fun goToMapTrip() {
        val i = Intent(this, InformacionViaje::class.java)
        startActivity(i)
    }

    private fun goToMap() {
        val i = Intent(this, InformacionViaje::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun createBooking(idDriver: String) {

        val booking = Reserva(
            idClient = authProvider.getId(),
            idDriver = idDriver,
            estado = "create",
            destino = extraDestinationName,
            origen = extraOriginName,
            tiempo = extraTime,
            km = extraDistance,
            origenLat = extraOriginLat,
            origenLng = extraOriginLng,
            destinoLat = extraDestinationLat,
            destinoLng = extraDestinationLng
        )

        bookingProvider.create(booking).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this@Buscar, "Datos del viaje creados", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this@Buscar, "Error al crear los datos", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getDriverInfo() {

        driverProvider.getDriver(idDriver).addOnSuccessListener { document ->
            if (document.exists()) {
                driver = document.toObject(Conductor::class.java)
                sendNotification()
            }
        }

    }

    private fun getClosestDriver() {
        geoProvider.getNearbyDrivers(originLatLng!!, radius).addGeoQueryEventListener(object:
            GeoQueryEventListener {

            override fun onKeyEntered(documentID: String, location: GeoPoint) {
                if (!isDriverFound) {
                    isDriverFound = true
                    idDriver = documentID
                    getDriverInfo()
                    Log.d("FIRESTORE", "Conductor id: $idDriver")
                    driverLatLng = LatLng(location.latitude, location.longitude)
                    binding.textViewSearch.text = "CONDUCTOR ENCONTRADO\nESPERANDO RESPUESTA"
                    createBooking(documentID)
                }
            }

            override fun onKeyExited(documentID: String) {

            }

            override fun onKeyMoved(documentID: String, location: GeoPoint) {

            }

            override fun onGeoQueryError(exception: Exception) {

            }

            override fun onGeoQueryReady() { // TERMINA LA BUSQUEDA
                if (!isDriverFound) {
                    radius = radius + 0.2

                    if (radius > limitRadius) {
                        binding.textViewSearch.text = "NO SE ENCONTRO NINGUN CONDUCTOR"
                        return
                    }
                    else {
                        getClosestDriver()
                    }
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        listenerBooking?.remove()
    }
}