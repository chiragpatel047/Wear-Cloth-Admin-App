package com.ecomapp.admin.Models

data class PushNotification(
    val data : FirebaseNotificationModel,
    val to : String
)
