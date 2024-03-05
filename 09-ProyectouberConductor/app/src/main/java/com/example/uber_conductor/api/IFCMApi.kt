package com.example.uber_conductor.api


import com.example.uber_conductor.Modelos.FCMBody
import com.example.uber_conductor.Modelos.FCMResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IFCMApi {

    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAA4IOzpIo:APA91bEbxCf1lCnzb4qKScrkr9UyURRLBvPL8jL8p0h1ISFgrMKlZhYEh_-UfHaGLyYgvav8LANnxHW7vYU7nWpJrlRz4pJLkn0EvZddimsMmCVI5-z-3zoao9qAHyRQqDimyyVnXoje"
    )
    @POST("fcm/send")
    fun send(@Body body: FCMBody): Call<FCMResponse>


}