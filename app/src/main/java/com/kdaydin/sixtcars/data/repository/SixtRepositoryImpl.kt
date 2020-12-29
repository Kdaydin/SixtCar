package com.kdaydin.sixtcars.data.repository

import android.os.Handler
import android.os.Looper
import com.kdaydin.sixtcars.application.ApplicationConstants
import com.kdaydin.sixtcars.data.entities.SixtCar
import com.kdaydin.sixtcars.data.remote.HttpStatusCodeException
import com.kdaydin.sixtcars.data.remote.SixtApi
import com.kdaydin.sixtcars.data.remote.UseCaseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class SixtRepositoryImpl(
    private val sixtApi: SixtApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    SixtRepository {

    override suspend fun getCars(): UseCaseResult<List<SixtCar>> {
        return safeApiCall(dispatcher, { sixtApi.getCars().await() })
    }

    //BASE CALL FOR HANDLING ERRORS FROM SINGLE POINT
    private suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T,
        actionOnServiceError: (() -> Unit)? = null,
        isAsync: Boolean? = false,
        isUnnecessaryTimeoutControl: Boolean = false
    ): UseCaseResult<T> {
        return withContext(dispatcher) {
            /*runOnUiThread(Runnable {
                ApplicationConstants.lastActivity?.apply {
                    (this as BaseActivity<*, *>).showLoading()
                }
            })*/

            try {
                val result = apiCall.invoke()
                /*runOnUiThread(Runnable {
                    ApplicationConstants.lastActivity?.apply {
                        (this as BaseActivity<*, *>).hideLoading()
                    }
                })*/
                UseCaseResult.Success(result)
            } catch (e: IOException) {

                if (isAsync == true) {
                    /*runOnUiThread(Runnable {
                        ApplicationConstants.lastActivity?.apply {
                            (this as BaseActivity<*, *>).hideLoading()
                        }
                    })*/
                    UseCaseResult.GenericError(null, null)
                } else {
                    when (e.cause) {
                        is HttpStatusCodeException -> {
                            /*runOnUiThread(Runnable {
                                ApplicationConstants.lastActivity?.apply {
                                    (this as BaseActivity<*, *>).hideLoading()
                                }
                            })*/
                            when ((e.cause as HttpStatusCodeException).statusCode) {
                                400 -> {
                                    UseCaseResult.NetworkError(
                                        e.cause?.message ?: "",
                                        e.cause?.cause?.message ?: "",
                                        actionOnServiceError
                                    )
                                }

                                500 -> {
                                    /*if (!isUnnecessaryTimeoutControl && (e.cause?.message ?: "" == "SF003" || e.cause?.message ?: "" == "BF005")) {
                                        runOnUiThread(Runnable {
                                            ApplicationConstants.lastActivity?.apply {
                                                (this as BaseActivity<*, *>).onSessionTimeOut()
                                            }
                                        })*/
                                        UseCaseResult.GenericError(null, null)
                                    /*} else if (!isUnnecessaryTimeoutControl) {
                                        UseCaseResult.NetworkError(
                                            e.cause?.message ?: "",
                                            when (SavedDataManager.languageCode?.toUpperCase()) {
                                                "EN" -> "The operation can not be performed for technical reasons. Please try again later."
                                                "TR" -> "Teknik nedenlerden dolayı işleminiz gerçekleştirilemiyor. Lütfen tekrar deneyiniz."
                                                "KA" -> "ოპერაციის ჩატარება ტექნიკური მიზეზების გამო შეუძლებელია. გთხოვთ სცადოთ მოგვიანებით."
                                                else -> "The operation can not be performed for technical reasons. Please try again later."
                                            },
                                            actionOnServiceError
                                        )
                                    } else {
                                        UseCaseResult.GenericError(null, null)
                                    }*/
                                }

                                else -> {
                                    UseCaseResult.NetworkError(
                                        e.cause?.message ?: "",
                                        e.cause?.cause?.message ?: "",
                                        actionOnServiceError
                                    )
                                }
                            }

                        }

                        else -> {
                            /*runOnUiThread(Runnable {
                                ApplicationConstants.lastActivity?.apply {
                                    (this as BaseActivity<*, *>).hideLoading()
                                }
                            })*/
                            UseCaseResult.GenericError(null, null)
                        }
                    }
                }
            }
        }
    }


    private fun runOnUiThread(runnable: Runnable){
        if (Thread.currentThread() == Handler(Looper.getMainLooper()).looper.thread)
            runnable.run()
        else
            Handler(Looper.getMainLooper()).post(runnable)
    }
}