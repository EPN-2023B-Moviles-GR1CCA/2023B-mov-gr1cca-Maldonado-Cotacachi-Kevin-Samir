package com.example.uber_conductor.Fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.uber_conductor.Historial
import com.example.uber_conductor.MainActivity
import com.example.uber_conductor.Modelos.Conductor
import com.example.uber_conductor.Perfil
import com.example.uber_conductor.Providers.AuthProvider
import com.example.uber_conductor.Providers.ConductorProvider
import com.example.uber_conductor.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheetMenu: BottomSheetDialogFragment() {
    val driverProvider = ConductorProvider()
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

        getDriver()

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
        val i = Intent(activity, Historial::class.java)
        startActivity(i)
    }

    private fun goToMain() {
        authProvider.logout()
        val i = Intent(activity, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getDriver() {
        driverProvider.getDriver(authProvider.getId()).addOnSuccessListener { document ->
            if (document.exists()) {
                val driver = document.toObject(Conductor::class.java)
                textViewUsername?.text = "${driver?.nombre} ${driver?.apellido}"
            }
        }
    }

    companion object {
        const val TAG = "ModalBottomSheetMenu"
    }
}