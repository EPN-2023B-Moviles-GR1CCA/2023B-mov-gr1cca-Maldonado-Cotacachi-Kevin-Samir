package com.example.uber_cliente_proyecto.Fragmentos

import android.Manifest
import com.bumptech.glide.Glide
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat

import com.example.uber_cliente_proyecto.Modelos.Conductor
import com.example.uber_cliente_proyecto.Modelos.Reserva
import com.example.uber_cliente_proyecto.Providers.AuthProvider
import com.example.uber_cliente_proyecto.Providers.ConductorProvider
import com.example.uber_cliente_proyecto.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.ktx.toObject

import de.hdodenhof.circleimageview.CircleImageView

class ModalBottomSheetTripInfo: BottomSheetDialogFragment() {

    private var driver: Conductor? = null
    private lateinit var booking: Reserva
    val driverProvider = ConductorProvider()
    val authProvider = AuthProvider()
    var textViewClientName: TextView? = null
    var textViewOrigin: TextView? = null
    var textViewDestination: TextView? = null
    var imageViewPhone: ImageView? = null
    var circleImageClient: CircleImageView? = null

    val REQUEST_PHONE_CALL = 30


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_bottom_sheet_trip_info, container, false)

        textViewClientName = view.findViewById(R.id.textViewClientName)
        textViewOrigin = view.findViewById(R.id.textViewOrigin)
        textViewDestination = view.findViewById(R.id.textViewDestination)
        imageViewPhone = view.findViewById(R.id.imageViewPhone)
        circleImageClient = view.findViewById(R.id.circleImageClient)

//        getDriver()
        val data = arguments?.getString("booking")
        booking = Reserva.fromJson(data!!)!!

        textViewOrigin?.text = booking.origen
        textViewDestination?.text = booking.destino
        imageViewPhone?.setOnClickListener {
            if (driver?.celular != null) {

                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
                }

                call(driver?.celular!!)
            }

        }

        getDriverInfo()
        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PHONE_CALL) {
            if (driver?.celular != null) {
                call(driver?.celular!!)
            }
        }

    }

    private fun call(phone: String) {

        val i = Intent(Intent.ACTION_CALL)
        i.data = Uri.parse("tel:$phone")

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        requireActivity().startActivity(i)
    }

    private fun getDriverInfo() {
        driverProvider.getDriver(booking.idDriver!!).addOnSuccessListener { document ->
            if (document.exists()) {
                driver = document.toObject(Conductor::class.java)
                textViewClientName?.text = "${driver?.name} ${driver?.apellido}"

                if (driver?.imagen != null) {
                    if (driver?.imagen != "") {
                        Glide.with(requireActivity()).load(driver?.imagen).into(circleImageClient!!)
                    }
                }
//                textViewUsername?.text = "${driver?.name} ${driver?.lastname}"
            }
        }
    }

    companion object {
        const val TAG = "ModalBottomSheetTripInfo"
    }


}