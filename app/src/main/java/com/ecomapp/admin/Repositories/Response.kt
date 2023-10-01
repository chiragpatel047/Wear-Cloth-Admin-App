package com.ecomapp.febric.Repositories

sealed class Response<T>(val data: T? = null, val errorMsg : String? = null) {

    class Sucess<T>(data: T? = null) : Response<T>(data = data)
    class Error<T>(errorMsg : String) : Response<T>(errorMsg = errorMsg)
    class Loading<T>() : Response<T>()

}