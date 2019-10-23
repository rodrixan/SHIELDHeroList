package com.rodrixan.projects.technicaltests.shieldherolist.common.data

/**
 * Used as base data
 */
sealed class AppInternalData<out T : Any> {
    object Loading : AppInternalData<Nothing>()
    data class Success<out T : Any>(val data: T) : AppInternalData<T>()
    data class Error(val message: String = "Oops, something went wrong!",
                     val code: Int = -1) : AppInternalData<Nothing>()
}