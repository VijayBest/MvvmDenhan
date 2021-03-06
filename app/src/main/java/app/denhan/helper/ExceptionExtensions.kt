package app.denhan.helper

import app.denhan.data.repos.ApiResponseCode
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException

import java.net.ConnectException
import java.net.UnknownHostException

fun Exception.getStatusCode(): Int {
    return when (this) {
        is ConnectException -> ApiResponseCode.SERVER_CONNECTION_ERROR
        is UnknownHostException -> ApiResponseCode.NETWORK_NOT_AVAILABLE
        is JsonSyntaxException -> ApiResponseCode.SYNTAX_ERROR
        is HttpException -> ApiResponseCode.UN_AUTHORIZE
        else -> ApiResponseCode.UNKNOWN_ERROR
    }
}