package com.example.uber_cliente_proyecto.Fragmentos

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.uber_cliente_proyecto.HistorialAc
import com.example.uber_cliente_proyecto.MainActivity
import com.example.uber_cliente_proyecto.Modelos.Cliente
import com.example.uber_cliente_proyecto.Perfil
import com.example.uber_cliente_proyecto.Providers.AuthProvider
import com.example.uber_cliente_proyecto.Providers.ClienteProvider
import com.example.uber_cliente_proyecto.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ModalBottomSheetMenu: BottomSheetDialogFragment() {

    val clientProvider = ClienteProvider()
    val authProvider = AuthProvider()

    var textViewUsername: TextView? = null
    var linearLayoutLogout: LinearLayout? = null
    var linearLayoutProfile: LinearLayout? = null
    var linearLayoutHistory: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_bottom_sheet_menu, container, false)

        textViewUsername = view.findViewById(R.id.textViewUsername)
        linearLayoutLogout = view.findViewById(R.id.linearLayoutLogout)
        linearLayoutProfile = view.findViewById(R.id.linearLayoutProfile)
        linearLayoutHistory = view.findViewById(R.id.linearLayoutHistory)

        getClient()

        linearLayoutLogout?.setOnClickListener { goToMain() }
        linearLayoutProfile?.setOnClickListener { goToProfile() }
        linearLayoutHistory?.setOnClickListener { goToHistories() }
        return view
    }

    private fun goToProfile() {
        val i = Intent(activity, Perfil::class.java)
        startActivity(i)
    }

    private fun goToHistories() {
        val i = Intent(activity, HistorialAc::class.java)
        startActivity(i)
    }

    private fun goToMain() {
        authProvider.logout()
        val i = Intent(activity, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getClient() {
        clientProvider.getClientById(authProvider.getId()).addOnSuccessListener { document ->
            if (document.exists()) {
                val client = document.toObject(Cliente::class.java)
                textViewUsername?.text = "${client?.nombre} ${client?.apellido}"
            }
        }
    }

    companion object {
        const val TAG = "ModalBottomSheetMenu"
    }


}