package com.shabelnikd.bilimtrack.utils

import android.util.Log
import retrofit2.Response


suspend fun <T> safeApiCall(
    call: suspend () -> Response<T>,
    errorMessage: String
): Result<Response<T>> {
    try {
        val result = call()
        return Result.success(result)
    } catch (e: Exception) {
        Log.e("AllSD", errorMessage, e)
        return Result.failure(e)
    }
}