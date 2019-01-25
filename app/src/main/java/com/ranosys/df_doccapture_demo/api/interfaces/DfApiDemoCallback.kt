package com.ranosys.df_doccapture_demo.api.interfaces

/**
 * @Class Interface for handling api response by implementing in fragments or activities
 * @author Ranosys Technologies
 * @Date 08-Jun-2018
 */
interface DfApiDemoCallback<T> {
    fun onException(error: Throwable)

    fun onError(error: String)

    fun onSuccess(t: T)
}