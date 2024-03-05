package com.example.uber_cliente_proyecto.Providers

import com.example.uber_cliente_proyecto.Modelos.FCMBody
import com.example.uber_cliente_proyecto.Modelos.FCMResponse
import com.example.uber_cliente_proyecto.api.IFCMApi
import com.example.uber_cliente_proyecto.api.RetrofitClient
import retrofit2.Call

class NotificacionProvider {
    private val URL = "https://fcm.googleapis.com"

    fun sendNotification(body: FCMBody): Call<FCMResponse> {
        return RetrofitClient.getClient(URL).create(IFCMApi::class.java).send(body)
    }
}