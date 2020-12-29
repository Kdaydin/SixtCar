package com.kdaydin.sixtcars.data.remote

import com.kdaydin.sixtcars.application.ApplicationConstants
import retrofit2.Response

sealed class UseCaseResult<out T> {

    class Success<T>(val data: T) : UseCaseResult<T>()

    data class ServiceError(
        val statusCode: Int? = null,
        val errorCode: String? = null,
        val errorMsg: String? = null
    ) : UseCaseResult<Nothing>()

    data class GenericError(val code: Int? = null, val error: String? = null) :
        UseCaseResult<Nothing>()

    data class NetworkError(
        val type: String,
        val message: String,
        val action: (() -> Unit)? = null
    ) : UseCaseResult<Nothing>() {

        init {
            /*ApplicationConstants.lastActivity?.supportFragmentManager?.let {
                val dialog = RavenDialog(
                    type = RavenDialogType.ERROR,
                    header = "Error",
                    message = message,
                    positiveButton = "OK",
                    positiveAction = action
                )
                dialog.isCancelable = false
                dialog.show(it, "Interceptor")
            }*/
        }

    }

    class Error(ex: Throwable) : UseCaseResult<Nothing>()


}
