package com.example.uber_conductor.Providers

import com.example.uber_conductor.Modelos.FCMBody
import com.example.uber_conductor.Modelos.FCMResponse
import com.example.uber_conductor.api.IFCMApi
import com.example.uber_conductor.api.RetrofitClient
import retrofit2.Call


class NotificacionProvider {
    private val URL = "https://fcm.googleapis.com"

    fun sendNotification(body: FCMBody): Call<FCMResponse> {
        return RetrofitClient.getClient(URL).create(IFCMApi::class.java).send(body)
    }
}