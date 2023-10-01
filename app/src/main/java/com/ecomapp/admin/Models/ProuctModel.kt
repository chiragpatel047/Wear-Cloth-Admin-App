package com.ecomapp.febric.Models

data class ProuctModel(
    val productId : String? = null,
    val productTitle : String? = null,
    val productSubTitle : String? = null,
    val productDesc : String? = null,
    val productOldPrice : String? = null,
    val productPrice : String? = null,
    var productMainImage : String? = null,
    val noOfRating : Float? = null,
    val rate : Float? = null

)
