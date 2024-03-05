package com.example.uber_conductor

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.uber_conductor.Modelos.Conductor
import com.example.uber_conductor.Providers.AuthProvider
import com.example.uber_conductor.Providers.ConductorProvider
import com.example.uber_conductor.databinding.ActivityPerfilBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File

class Perfil : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilBinding
    val driverProvider = ConductorProvider()
    val authProvider = AuthProvider()

    private var imageFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        getDriver()
        binding.imageViewBack.setOnClickListener { finish() }
        binding.btnUpdate.setOnClickListener { updateInfo() }
        binding.circleImageProfile.setOnClickListener { selectImage() }
    }
    private fun updateInfo() {

        val name = binding.textFieldName.text.toString()
        val lastname = binding.textFieldLastname.text.toString()
        val phone = binding.textFieldPhone.text.toString()
        val carBrand = binding.textFieldCarBrand.text.toString()
        val carColor = binding.textFieldCarColor.text.toString()
        val carPlate = binding.textFieldCarPlate.text.toString()

        val driver = Conductor(
            id = authProvider.getId(),
            nombre = name,
            apellido = lastname,
            celular =  phone,
            color = carColor,
            marca= carBrand,
            numeroPlaca = carPlate
        )

        if (imageFile != null) {
            driverProvider.uploadImage(authProvider.getId(), imageFile!!).addOnSuccessListener { taskSnapshot ->
                driverProvider.getImageUrl().addOnSuccessListener { url ->
                    val imageUrl = url.toString()
                    driver.imagen = imageUrl
                    driverProvider.update(driver).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this@Perfil, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                        }
                        else {
                            Toast.makeText(this@Perfil, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
                        }
                    }
                    Log.d("STORAGE", "$imageUrl")
                }
            }
        }
        else {
            driverProvider.update(driver).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this@Perfil, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(this@Perfil, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
                }
            }
        }


    }

    private fun getDriver() {
        driverProvider.getDriver(authProvider.getId()).addOnSuccessListener { document ->
            if (document.exists()) {
                val driver = document.toObject(Conductor::class.java)
                binding.textViewEmail.text = driver?.correo
                binding.textFieldName.setText(driver?.nombre)
                binding.textFieldLastname.setText(driver?.apellido)
                binding.textFieldPhone.setText(driver?.celular)
                binding.textFieldCarBrand.setText(driver?.marca)
                binding.textFieldCarColor.setText(driver?.color)
                binding.textFieldCarPlate.setText(driver?.numeroPlaca)

                if (driver?.imagen != null) {
                    if (driver.imagen != "") {
                        Glide.with(this).load(driver.imagen).into(binding.circleImageProfile)
                    }
                }
            }
        }
    }

    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

        val resultCode = result.resultCode
        val data = result.data

        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            imageFile = File(fileUri?.path)
            binding.circleImageProfile.setImageURI(fileUri)
        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(this, "Tarea cancelada", Toast.LENGTH_LONG).show()
        }

    }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }
    }

}