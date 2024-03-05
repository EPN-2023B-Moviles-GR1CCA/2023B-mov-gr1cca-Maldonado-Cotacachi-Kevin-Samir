import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.uber_conductor.MapaViaje
import com.example.uber_conductor.MapaConductor
import com.example.uber_conductor.Modelos.Reserva
import com.example.uber_conductor.Providers.AuthProvider
import com.example.uber_conductor.Providers.GeoProvider
import com.example.uber_conductor.Providers.ReservaProvider
import com.example.uber_conductor.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheetBooking: BottomSheetDialogFragment() {
    lateinit var bundle: Bundle
    private lateinit var textViewOrigin: TextView
    private lateinit var textViewDestination: TextView
    private lateinit var textViewTimeAndDistance: TextView
    private lateinit var btnAccept: Button
    private lateinit var btnCancel: Button
    private val bookingProvider = ReservaProvider()
    private val geoProvider = GeoProvider()
    private val authProvider = AuthProvider()
    private lateinit var booking: Reserva

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_bottom_sheet_booking, container, false)

        textViewOrigin = view.findViewById(R.id.textViewOrigin)
        textViewDestination = view.findViewById(R.id.textViewDestination)
        textViewTimeAndDistance = view.findViewById(R.id.textViewTimeAndDistance)
        btnAccept = view.findViewById(R.id.btnAccept)
        btnCancel = view.findViewById(R.id.btnCancel)

        val data = bundle?.getString("booking")
        booking = Reserva.fromJson(data!!)!!
        Log.d("ARGUMENTS", "Booking: ${booking?.toJson()}")

        textViewOrigin.text = booking?.origin
        textViewDestination.text = booking?.destination
        textViewTimeAndDistance.text = "${String.format("%.1f", booking?.time)} Min - ${String.format("%.1f", booking?.km)} Km"

        btnAccept.setOnClickListener { acceptBooking(booking?.idClient!!) }
        btnCancel.setOnClickListener { cancelBooking(booking?.idClient!!) }

        return view
    }

    private fun cancelBooking(idClient: String) {
        bookingProvider.updateStatus(idClient, "cancel").addOnCompleteListener {
            (activity as? MapaConductor)?.timer?.cancel()
            dismiss()
        }
    }

    private fun acceptBooking(idClient: String) {
        bookingProvider.updateStatus(idClient, "accept").addOnCompleteListener {
            (activity as? MapaConductor)?.timer?.cancel()
            if (it.isSuccessful) {
                (activity as? MapaConductor)?.easyWayLocation?.endUpdates()
                geoProvider.removeLocation(authProvider.getId())
                goToMapTrip()
            }
        }
    }

    private fun goToMapTrip() {
        val i = Intent(context, MapaViaje::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context?.startActivity(i)
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? MapaConductor)?.timer?.cancel()
    }
}
