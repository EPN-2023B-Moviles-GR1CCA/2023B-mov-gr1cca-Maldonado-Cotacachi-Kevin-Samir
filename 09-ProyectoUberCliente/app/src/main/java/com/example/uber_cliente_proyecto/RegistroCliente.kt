package com.example.uber_cliente_proyecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.example.uber_cliente_proyecto.Modelos.Cliente
import com.example.uber_cliente_proyecto.databinding.ActivityRegistroClienteBinding
import com.example.uber_cliente_proyecto.Providers.AuthProvider
import com.example.uber_cliente_proyecto.Providers.ClienteProvider

class RegistroCliente : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroClienteBinding
    val authProvider = AuthProvider()
    private val clientProvider = ClienteProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.btnGoToLogin.setOnClickListener { goToLogin() }
        binding.btnRegister.setOnClickListener { registro() }
    }
    private fun registro() {
        val name = binding.textFieldName.text.toString()
        val lastname = binding.textFieldLastname.text.toString()
        val email = binding.textFieldEmail.text.toString()
        val phone = binding.textFieldPhone.text.toString()
        val password = binding.textFieldPassword.text.toString()
        val confirmPassword = binding.textFieldConfirmPassword.text.toString()

        if (isValidForm(name, lastname, email, phone, password, confirmPassword)) {
            authProvider.registro(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val client = Cliente(
                        id = authProvider.getId(),
                        nombre = name,
                        apellido = lastname,
                        correo = email,
                        telefono = phone
                    )
                    clientProvider.create(client).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this@RegistroCliente, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            goToMap()
                        }
                        else {
                            Toast.makeText(this@RegistroCliente, "Hubo un error Almacenado los datos del usuario ${it.exception.toString()}", Toast.LENGTH_SHORT).show()
                            Log.d("FIREBASE", "Error: ${it.exception.toString()}")
                        }
                    }
                }
                else {
                    Toast.makeText(this@RegistroCliente, "Registro fallido ${it.exception.toString()}", Toast.LENGTH_LONG).show()
                    Log.d("FIREBASE", "Error: ${it.exception.toString()}")
                }
            }
        }

    }
    //Ir al mapa
    private fun goToMap() {
        val i = Intent(this, MapaCliente::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }
    private fun isValidForm(
        name: String,
        lastname: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): Boolean {

        if (name.isEmpty()) {
            Toast.makeText(this, "Debes ingresar tu nombre", Toast.LENGTH_SHORT).show()
            return false
        }
        if (lastname.isEmpty()) {
            Toast.makeText(this, "Debes ingresar tu apellido", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Debes ingresar tu correo electronico", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phone.isEmpty()) {
            Toast.makeText(this, "Debes ingresar tu telefono", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Debes ingresar la contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Debes ingresar la confirmacion de la contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "las contraseñas deben coincidir", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "la contraseña deben tener al menos 6 caracteres", Toast.LENGTH_LONG).show()
            return false
        }

        return true

    }
    //Ir al login
    private fun goToLogin() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}