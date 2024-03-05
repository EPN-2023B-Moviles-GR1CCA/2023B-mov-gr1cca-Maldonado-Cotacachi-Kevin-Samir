package com.example.uber_conductor.Fragmentos

import android.Manifest
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
import com.bumptech.glide.Glide
import com.example.uber_conductor.Modelos.Cliente
import com.example.uber_conductor.Modelos.Reserva
import com.example.uber_conductor.Providers.AuthProvider
import com.example.uber_conductor.Providers.ClienteProvider
import com.example.uber_conductor.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


import de.hdodenhof.circleimageview.CircleImageView

class ModalBottomSheetTripInfo: BottomSheetDialogFragment() {

    private var client: Cliente? = null
    private lateinit var booking: Reserva
    val clientProvider = ClienteProvider()
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

        textViewOrigin?.text = booking.origin
        textViewDestination?.text = booking.destination
        imageViewPhone?.setOnClickListener {
            if (client?.celular != null) {

                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
                }

                call(client?.celular!!)
            }

        }

        getClientInfo()
        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PHONE_CALL) {
            if (client?.celular != null) {
                call(client?.celular!!)
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

    private fun getClientInfo() {
        clientProvider.getClientById(booking.idClient!!).addOnSuccessListener { document ->
            if (document.exists()) {
                client = document.toObject(Cliente::class.java)
                textViewClientName?.text = "${client?.nombre} ${client?.apellido}"

                if (client?.imagen != null) {
                    if (client?.imagen != "") {
                        Glide.with(requireActivity()).load(client?.imagen).into(circleImageClient!!)
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